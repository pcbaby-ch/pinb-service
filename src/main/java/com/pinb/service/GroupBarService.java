/**
 * 
 */
package com.pinb.service;

import java.util.ArrayList;
import java.util.List;

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
import com.pinb.mapper.GroubaOrderMapper;
import com.pinb.mapper.GroupBarMapper;
import com.pinb.util.BeanUtil;
import com.pinb.util.IpUtils;

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
	GroubActivityService groubActivityService;
	@Autowired
	private UserService userService;

	/**
	 * 店铺入驻
	 * 
	 * @param reqStr
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public boolean add(String reqStr, HttpServletRequest request) {
		JSONObject reqJson = JSONObject.parseObject(reqStr);
		GroupBar groupBar = reqJson.getObject("groub", GroupBar.class);
		User user = reqJson.getObject("userinfo", User.class);
		if (StringUtils.isEmpty(user.getWxUnionid())) {
			user.setWxUnionid(user.getWxOpenid());
		}
		user.setPhone(groupBar.getGroubPhone());
		user.setHeadImg(reqJson.getJSONObject("userinfo").getString("avatarUrl"));
		user.setIsOpenGroub("1");
		user.setClientIp(IpUtils.getIpFromRequest(request));
		List<JSONObject> groubActivityList = reqJson.getObject("goodsList", List.class);

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
		logParams(groupBar);

		if (StringUtils.isEmpty(groupBar.getGroubTrace())) {
			groupBar.setGroubTrace(BusinessesFlowNum.getNum("G", RedisConst.groupBarTrace));
		}
		groupBar.setRefUserWxUnionid(user.getWxUnionid());
		groupBar.setIsOpen("1");
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
				userService.add(user);
			} catch (DuplicateKeyException e) {
				log.info("#已注册普通用户-店铺入驻,#GroubTrace:[{}]", groupBar.getGroubTrace());
				User userParams = new User();
				userParams.setWxUnionid(user.getWxUnionid());
				userParams.setIsOpenGroub("1");
				userService.update(user);
			}
		} else {
			// #店铺二次更新，则从库中获取用户位置信息
			user = userService.selectOne(user);
		}
		log.info("#保存用户信息end-保存商品信息start,#GroubTrace:[{}]", groupBar.getGroubTrace());
		boolean groubActivityResult = false;
		groubActivityService.delete(groupBar.getGroubTrace(), user.getWxUnionid());
		for (int i = 0; i < groubActivityList.size(); i++) {
			GroubActivity groubActivity = groubActivityList.get(i).toJavaObject(GroubActivity.class);
			groubActivity.setGroubaTrace(BusinessesFlowNum.getNum("GA", RedisConst.groubActivityTrace));
			groubActivity.setRefGroubTrace(groupBar.getGroubTrace());
			groubActivity.setRefUserWxUnionid(user.getWxUnionid());
			groubActivity.setProvince(user.getProvince());
			groubActivity.setCity(user.getCity());
			groubActivity.setLatitude(user.getLatitude());
			groubActivity.setLongitude(user.getLongitude());
			// #如果此商品没有设置图片，则直接忽略跳过
			if (StringUtils.isEmpty(groubActivity.getGoodsImg())) {
				continue;
			}
			groubActivityResult = groubActivityService.add(groubActivity);
			log.info("#保存商品信息end,#GroubTrace:[{}]", groupBar.getGroubTrace());
		}
		if (groubResult && groubActivityResult)

		{
			log.info("#店铺入驻成功,#GroubTrace:[{}]", groupBar.getGroubTrace());
		}
		return true;
	}

	private void logParams(GroupBar groupBar) {
		log.debug("#入参校验通过,#GroubTrace:[{}]", groupBar.getGroubTrace());
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
	 * 加载指定商铺的基本信息+商品信息（如果是分享来源，则需要去除分享订单对应的商品）+ 分享活动商品（带订单信息）
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
		groupBar = groupBarMapper.selectOne(groupBar.getRefUserWxUnionid());
		if (BeanUtil.checkFieldValueNull(groupBar)) {
			throw new ServiceException("#店铺基础信息查询失败");
		}
		GroubActivity shareGroubActivity = null;
		// #查询商品信息
		List<GroubActivity> goodsList = groubActivityService.select(groupBar.getGroubTrace(),
				groupBar.getRefUserWxUnionid());
		if (!StringUtils.isEmpty(groupBar.getOrderTrace())) {
			// 去除分享订单对应的商品
			GroubaOrder shareOrder = groubaOrderMapper.selectOne(groupBar.getOrderTrace(),
					groupBar.getRefUserWxUnionid());
			List<GroubActivity> goodsListTemp = new ArrayList<>();
			goodsListTemp.addAll(goodsList);
			for (int i = 0; i < goodsListTemp.size(); i++) {
				GroubActivity groubActivity = goodsListTemp.get(i);
				if (groubActivity.getGroubaTrace().equals(shareOrder.getRefGroubaTrace())) {
					shareGroubActivity = goodsList.get(i);
					goodsList.remove(i);
					break;
				}
			}
			// #整合分享订单信息到分享订单对应活动商品下
			if (!(shareGroubActivity == null || BeanUtil.checkFieldValueNull(shareGroubActivity))) {
				shareGroubActivity.setUserImgs(shareOrder.getUserImgs());
				shareGroubActivity.setRelationOrderTrace(shareOrder.getOrderTrace());
				shareGroubActivity.setOrderRelationUser(shareOrder.getRefUserWxUnionid());
			}
		}
		JSONObject resp = new JSONObject();
		resp.put("groubInfo", groupBar);
		resp.put("goodsList", goodsList);
		resp.put("shareGoods", shareGroubActivity);

		return resp;
	}

}
