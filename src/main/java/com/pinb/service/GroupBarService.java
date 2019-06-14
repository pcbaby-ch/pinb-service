/**
 * 
 */
package com.pinb.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinb.common.BusinessesFlowNum;
import com.pinb.common.ServiceException;
import com.pinb.constant.RedisConst;
import com.pinb.entity.GroubActivity;
import com.pinb.entity.GroubaOrder;
import com.pinb.entity.GroupBar;
import com.pinb.entity.User;
import com.pinb.enums.RespCode;
import com.pinb.mapper.GroubActivityCache;
import com.pinb.mapper.GroubaOrderMapper;
import com.pinb.mapper.GroupBarMapper;
import com.pinb.util.IpUtils;
import com.pinb.util.MapBeanUtil;
import com.pinb.util.PropertiesUtils;
import com.pinb.util.RespUtil;

/**
 * 拼吧-店铺
 * 
 * @author chenzhao @date Apr 15, 2019
 */
@Service
public class GroupBarService {

	private static final Logger log = LoggerFactory.getLogger(GroupBarService.class);

	@Autowired
	GroupBarMapper groupBarMapper;
	@Autowired
	GroubaOrderMapper groubaOrderMapper;
	@Autowired
	GroubActivityCache groubActivityCache;
	@Autowired
	GroubActivityService groubActivityService;
	@Autowired
	UserService userService;

	private int[] groubaSize = { 2, 3, 5, 6, 8, 9 };

	/**
	 * 店铺入驻 | 店铺信息+商品信息保存
	 * 
	 * @param reqStr
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public Object add(String reqStr, HttpServletRequest request) {
		JSONObject reqJson = JSONObject.parseObject(reqStr);
		GroupBar groupBar = reqJson.getObject("groub", GroupBar.class);
		User userVo = reqJson.getObject("userinfo", User.class);
		if (StringUtils.isEmpty(userVo.getWxUnionid())) {
			userVo.setWxUnionid(userVo.getWxOpenid());
		}
		groupBar.setRefUserWxUnionid(userVo.getWxUnionid());
		groupBar.setIsOpen("1");
		if (StringUtils.isEmpty(groupBar.getGroubTrace())) {
			groupBar.setGroubTrace(BusinessesFlowNum.getNum("G", RedisConst.groupBarTrace));
		}
		userVo.setPhone(groupBar.getGroubPhone());
		userVo.setHeadImg(reqJson.getJSONObject("userinfo").getString("avatarUrl"));
		userVo.setIsOpenGroub("1");
		userVo.setGroubTrace(groupBar.getGroubTrace());
		userVo.setClientIp(IpUtils.getIpFromRequest(request));
		List<JSONObject> groubActivityList = reqJson.getObject("goodsList", List.class);
		log.debug("#user:[{}],#groubActivityList:[{}]", JSONObject.toJSON(userVo),
				JSONObject.toJSON(groubActivityList));
		// #入参校验
//		if (StringUtils.isEmpty(groupBar.getGroubName())) {
//			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GroubName");
//		}
		if (StringUtils.isEmpty(groupBar.getGroubImg())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GroubImg");
		}
		if (StringUtils.isEmpty(groupBar.getGroubPhone())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GroubPhone");
		}
		if (StringUtils.isEmpty(groupBar.getGroubAddress())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GroubAddress");
		}
		if (StringUtils.isEmpty(groupBar.getProvince())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "Province");
		}
		if (StringUtils.isEmpty(groupBar.getCity())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "City");
		}
		logParams(groupBar);

		log.info("#保存店铺信息start,#GroubTrace:[{}]", groupBar.getGroubTrace());
		boolean isUpdateAction = false;
		boolean groubResult;
		try {
			groubResult = groupBarMapper.insert(groupBar) > 0;
		} catch (DuplicateKeyException e) {
			groubResult = update(groupBar);
			isUpdateAction = true;
			log.info("#店铺信息已存在，更新完成#GroubTrace:[{}]", groupBar.getGroubTrace());
		}
		log.info("#保存店铺信息end-保存用户信息start,#GroubTrace:[{}]", groupBar.getGroubTrace());
		if (!isUpdateAction) {
			try {
				userService.add(userVo);
			} catch (DuplicateKeyException e) {
				log.info("#已注册普通用户-店铺入驻,#GroubTrace:[{}]", groupBar.getGroubTrace());
				userOpenGroub(userVo);
			}
		} else {
			log.info("#已注册普通用户-店铺入驻,#GroubTrace:[{}]", groupBar.getGroubTrace());
			userOpenGroub(userVo);
		}
		log.info("#保存用户信息end-保存商品信息start,#GroubTrace:[{}]", groupBar.getGroubTrace());
		boolean groubActivityResult = false;
		groubActivityCache.delete(groupBar.getCity(), groupBar.getGroubTrace(), userVo.getWxUnionid());
		for (int i = 0; i < groubActivityList.size(); i++) {
			GroubActivity groubActivity = groubActivityList.get(i).toJavaObject(GroubActivity.class);
			groubActivity.setGroubaTrace(BusinessesFlowNum.getNum("GA", RedisConst.groubActivityTrace));
			groubActivity.setRefGroubTrace(groupBar.getGroubTrace());
			groubActivity.setRefUserWxUnionid(userVo.getWxUnionid());
			groubActivity.setProvince(groupBar.getProvince());
			groubActivity.setCity(groupBar.getCity());
			groubActivity.setLatitude(groupBar.getLatitude());
			groubActivity.setLongitude(groupBar.getLongitude());
			groubActivity.setAddress(groupBar.getGroubAddress());
			// #如果此商品没有设置图片，则直接忽略跳过
			if (StringUtils.isEmpty(groubActivity.getGoodsImg())) {
				continue;
			}
			if (StringUtils.isEmpty(groubActivity.getGoodsName())) {
				throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GoodsName");
			}
			if (StringUtils.isEmpty(groubActivity.getGoodsPrice())) {
				throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GoodsPrice");
			}
			if (StringUtils.isEmpty(groubActivity.getGroubaDiscountAmount())) {
				throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GroubaDiscountAmount");
			}
			groubActivityResult = groubActivityService.add(groubActivity);
			log.info("#保存商品信息end,#GroubTrace:[{}]", groupBar.getGroubTrace());
		}
		if (groubResult && groubActivityResult) {
			log.info("#店铺入驻成功,#GroubTrace:[{}]", groupBar.getGroubTrace());
		}
		return groupBar.getGroubTrace();
	}

	private void userOpenGroub(User userVo) {
		User userParams = new User();
		userParams.setWxUnionid(userVo.getWxUnionid());
		userParams.setIsOpenGroub("1");
		userParams.setGroubTrace(userVo.getGroubTrace());
		userService.update(userParams);
	}

	private void logParams(GroupBar groupBar) {
		log.debug("#入参校验通过:[{}]", JSONObject.toJSON(groupBar));
	}

	public boolean update(GroupBar groupBar) {
		// #入参校验
		if (StringUtils.isEmpty(groupBar.getGroubTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GroubTrace");
		}
		logParams(groupBar);
		return groupBarMapper.update(groupBar) > 0;
	}

	public Page<?> select(GroupBar groupBar) {
		// #入参校验
		if (StringUtils.isEmpty(groupBar.getRefUserWxUnionid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "RefUserWxUnionid");
		}
		logParams(groupBar);
		Page<?> page = PageHelper.startPage(groupBar.getPage(), groupBar.getRows());
		groupBarMapper.select(groupBar.getRefUserWxUnionid());
		return page;
	}

	/**
	 * 加载当前用户的商铺的基本信息+商品信息
	 * 
	 * @param groupBar
	 * @return
	 */
	public Object selectOne(GroupBar groupBar) {
		// #入参校验
		if (StringUtils.isEmpty(groupBar.getRefUserWxUnionid()) && StringUtils.isEmpty(groupBar.getGroubTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refUserWxUnionid | GroubTrace");
		}
		logParams(groupBar);
		groupBar = groupBarMapper.selectOne(groupBar.getRefUserWxUnionid(), null);
		if (groupBar == null) {
			throw new ServiceException(RespCode.groub_unExist);
		}
		// #查询商品信息
		List<GroubActivity> goodsList = groubActivityCache.selectOneGroub(groupBar.getGroubTrace(),
				groupBar.getRefUserWxUnionid());
		JSONObject resp = new JSONObject();
		resp.put("groubInfo", groupBar);
		resp.put("goodsList", goodsList);

		return resp;
	}

	/**
	 * 加载店铺的基本信息+商品信息+商品关联同团订单信息（头像、状态）{分享订单的信息可选}
	 * {GroubTrace、OrderTrace、OrderLeader、RefUserWxUnionid}
	 * 
	 * @author chenzhao @date May 14, 2019
	 * @param groupBar
	 * @return {返回goodsList + shareGoods（可参团 | 已参团）+ goodsList}
	 */
	public Object getOneShopShare(GroupBar groupBarVo) {
		// #入参校验
		if (StringUtils.isEmpty(groupBarVo.getGroubTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GroubTrace");
		}
		if (!StringUtils.isEmpty(groupBarVo.getOrderTrace())) {
			if (StringUtils.isEmpty(groupBarVo.getOrderLeader())) {
				throw new ServiceException(RespCode.PARAM_INCOMPLETE, "OrderLeader");
			}
		}
		if (StringUtils.isEmpty(groupBarVo.getRefUserWxUnionid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "RefUserWxUnionid");
		}
		logParams(groupBarVo);
		GroupBar groupBar = groupBarMapper.selectOne(null, groupBarVo.getGroubTrace());
		log.info("#店铺信息查询end-商品查询start,#groubTrace:[{}]", groupBarVo.getGroubTrace());
		if (groupBar == null) {
			throw new ServiceException("#店铺基础信息查询失败");
		}
		// #查询关联店铺下所有活动商品
		List<GroubActivity> goodsList = groubActivityCache.selectOneGroub(groupBarVo.getGroubTrace(), null);
		JSONObject resp = new JSONObject();
		log.info("#商品查询end-头像信息查询start,#groubTrace:[{}]", groupBarVo.getGroubTrace());
		GroubActivity shareGroubActivity = getGoodsImgs(groupBarVo, goodsList);
		resp.put("shareGoods", shareGroubActivity);// 被分享商品，携带被分享团的订单头像信息
		log.info("#头像信息查询end-END,#groubTrace:[{}]", groupBarVo.getGroubTrace());
		resp.put("groubInfo", groupBar);
		resp.put("goodsList", goodsList);// 普通商品，可能携带和我相关的订单头像信息
		return resp;
	}

	/**
	 * 获取店铺小程序二维码
	 * 
	 * @param groupBar
	 * @return 获取成功，返回二维码文件名
	 * @throws IOException
	 */
	public Object getShopQR(GroupBar groupBar) throws IOException {
		// #入参校验
		if (StringUtils.isEmpty(groupBar.getGroubTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "groubTrace");
		}

		// #获取accessToken
		String accessToken = WxApiService.getAccessToken(groupBar.getAppid(), groupBar.getSecret());
		// #获取小程序二维码
		Object wxQR = WxApiService.getUnlimited(accessToken, groupBar.getGroubTrace());
		if (wxQR instanceof String) {
			throw new ServiceException(RespCode.END);
		} else {
			// #保存QR字节流到指定目录
			String imagesPath = PropertiesUtils.getProperty("images.path", "/data/pinb/images/") + "shopQR/";
			String newFileName = groupBar.getGroubTrace() + ".jpg";
			if (System.getProperty("user.dir").contains(":")) {
				// windows 环境
				imagesPath = System.getProperty("user.dir").substring(0, 2) + imagesPath;
			}
			File newFile = new File(imagesPath + newFileName);
			if (newFile.exists()) {
				log.debug("#文件已存在服务器，上传成功,#path:[{}]", newFile.getPath());
				return RespUtil.dataResp(newFileName);
			}
			if (!newFile.getParentFile().exists()) { // 判断文件父目录是否存在
				newFile.getParentFile().mkdirs();
			}
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(newFile);
				fos.write((byte[]) wxQR);
			} catch (Exception e) {
				log.error("#店铺二维码保存失败");
			} finally {
				if (fos != null)
					fos.close();
			}
			return RespUtil.dataResp(newFileName);
		}
	}

	/**
	 * 获取商铺商品+分享商品下，相关团的订单头像、状态
	 * 
	 * @author chenzhao @date May 17, 2019
	 * @param groupBarVo
	 * @param goodsList
	 * @return
	 */
	private GroubActivity getGoodsImgs(GroupBar groupBarVo, List<GroubActivity> goodsList) {
		List<GroubaOrder> orderList = groubaOrderMapper.selectMyOrder4Groub(groupBarVo.getGroubTrace(),
				groupBarVo.getRefUserWxUnionid());
		GroubaOrder shareOrder = groubaOrderMapper.selectOne(groupBarVo.getOrderTrace(), groupBarVo.getOrderLeader());
		if ((orderList == null || orderList.isEmpty()) && shareOrder == null) {
			log.debug("#无任何订单，需要组装头像、状态信息");
			return null;
		}
		GroubActivity shareGoods = null;
		Set<Object> orderTraceSet = MapBeanUtil.objectsToSet(orderList, "orderTrace");
		String orderTraces = orderTraceSet == null ? "" : MapBeanUtil.setToStrs(orderTraceSet);
		if (shareOrder != null) {
			orderTraces = StringUtils.isEmpty(orderTraces) ? "'" + groupBarVo.getOrderTrace() + "'"
					: orderTraces + ",'" + groupBarVo.getOrderTrace() + "'";
		}
		List<GroubaOrder> orderUserImgs = groubaOrderMapper.selectMyOrder4userImgs(orderTraces, null);
		log.info("#所有数据查询end-组装响应数据start,#groubTrace:[{}]，#相关订单:[{}]，#分享订单:[{}]", groupBarVo.getGroubTrace(),
				JSONObject.toJSON(orderList), JSONObject.toJSON(shareOrder));
		Map<String, Object> groubaImgMap = MapBeanUtil.objListToMap(orderUserImgs, "refGroubaTrace");
		int removeIndex = -1;
		for (int i = 0; i < goodsList.size(); i++) {
			GroubActivity goods = goodsList.get(i);
			if (shareOrder != null && goods.getGroubaTrace().equals(shareOrder.getRefGroubaTrace())) {
				shareGoods = goods;
				removeIndex = i;
			}
			if (groubaImgMap.containsKey(goods.getGroubaTrace())) {// 整合店铺商品的订单头像信息
				GroubaOrder orderImgs = (GroubaOrder) groubaImgMap.get(goods.getGroubaTrace());
				goods.setOrderRefUsers(orderImgs.getOrderRefUsers());
				goods.setUserImgs(orderImgs.getUserImgs());
				goods.setOrdersStatus(orderImgs.getOrdersStatus());
				if (orderImgs.getOrderRefUsers().contains(groupBarVo.getRefUserWxUnionid())
						|| groubaSize[goods.getGroubaSize()] == orderImgs.getOrderRefUsers().split(",").length) {
					// #如果我已参团，或者团已满，
					goods.setIsJoined(true);
				}
				goods.setShareOrder(orderImgs.getOrderTrace());
				goods.setShareLeader(orderImgs.getLeader());
				goods.setOrderExpiredTime(orderImgs.getOrderExpiredTime());
			}
		}
		// #店铺商品list中，去除分享商品
		if (removeIndex >= 0) {
			goodsList.remove(removeIndex);
		}
		if (shareGoods != null && groubaImgMap.containsKey(shareGoods.getGroubaTrace())) {// 整合分享商品的订单头像信息
			GroubaOrder orderImgs = (GroubaOrder) groubaImgMap.get(shareGoods.getGroubaTrace());
			shareGoods.setOrderRefUsers(orderImgs.getOrderRefUsers());
			shareGoods.setUserImgs(orderImgs.getUserImgs());
			shareGoods.setOrdersStatus(orderImgs.getOrdersStatus());
			if (orderImgs.getOrderRefUsers().contains(groupBarVo.getRefUserWxUnionid())
					|| groubaSize[shareGoods.getGroubaSize()] == orderImgs.getOrderRefUsers().split(",").length) {
				// #如果我已参团，或者团已满，
				shareGoods.setIsJoined(true);
			}
			shareGoods.setShareOrder(shareOrder.getOrderTrace());
			shareGoods.setShareLeader(shareOrder.getLeader());
			shareGoods.setOrderExpiredTime(orderImgs.getOrderExpiredTime());
		}
		return shareGoods;
	}

}
