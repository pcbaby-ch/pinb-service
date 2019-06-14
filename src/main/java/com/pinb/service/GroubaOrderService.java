/**
 * 
 */
package com.pinb.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinb.common.BusinessesFlowNum;
import com.pinb.common.ServiceException;
import com.pinb.constant.RedisConst;
import com.pinb.entity.GroubActivity;
import com.pinb.entity.GroubaOrder;
import com.pinb.enums.OrderStatus;
import com.pinb.enums.RespCode;
import com.pinb.mapper.GroubActivityCache;
import com.pinb.mapper.GroubaOrderMapper;
import com.pinb.util.DateUtil;
import com.pinb.util.MapBeanUtil;

/**
 * 拼吧-订单
 * 
 * @author chenzhao @date Apr 15, 2019
 */
@Service
public class GroubaOrderService {

	private static final Logger log = LoggerFactory.getLogger(GroubaOrderService.class);

	@Autowired
	GroubActivityCache groubActivityCache;
	@Autowired
	GroubaOrderMapper groubaOrderMapper;
	@Autowired
	GroubActivityService groubActivityService;
	@Autowired
	MsgSendService msgSendService;

	private int[] groubaSize = { 2,3,5,6, 8, 9 };

	/**
	 * 开团服务
	 * 
	 * @param groubaOrderVo
	 * @return
	 * @throws Exception
	 */
	public boolean orderOpen(GroubaOrder groubaOrderVo) throws Exception {
		// #入参校验
		if (StringUtils.isEmpty(groubaOrderVo.getRefGroubaTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refGroubaTrace");
		}
		if (StringUtils.isEmpty(groubaOrderVo.getRefGroubTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refGroubTrace");
		}
		if (StringUtils.isEmpty(groubaOrderVo.getOrderExpiredTime())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "orderExpiredTime");
		}
		if (StringUtils.isEmpty(groubaOrderVo.getRefUserWxUnionid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refUserWxUnionid");
		}
		if (StringUtils.isEmpty(groubaOrderVo.getRefUserWxOpenid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "RefUserWxOpenid");
		}
		if (StringUtils.isEmpty(groubaOrderVo.getRefUserImg())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refUserImg");
		}
		if (StringUtils.isEmpty(groubaOrderVo.getGoodsName())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "goodsName");
		}
		if (StringUtils.isEmpty(groubaOrderVo.getGoodsImg())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "goodsImg");
		}
		if (StringUtils.isEmpty(groubaOrderVo.getGoodsPrice())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "goodsPrice");
		}
		if (StringUtils.isEmpty(groubaOrderVo.getGroubaDiscountAmount())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "groubaDiscountAmount");
		}
		if (StringUtils.isEmpty(groubaOrderVo.getGroubaIsnew())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "groubaIsnew");
		}
		if (StringUtils.isEmpty(groubaOrderVo.getFormId())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "FormId");
		}
		logParams(groubaOrderVo);
		GroubActivity groubActivity = groubActivityCache.selectOne(groubaOrderVo.getRefGroubaTrace());
		int groubOrderCount = groubaOrderMapper.selectOpenGroubCount(null, groubaOrderVo.getRefGroubaTrace());
		if (groubOrderCount >= groubActivity.getGroubaMaxCount()) {
			throw new ServiceException(RespCode.order_openGrouba);
		}
		groubaOrderVo.setOrderTrace(BusinessesFlowNum.getNum("GO", RedisConst.groubaOrderTrace));
		groubaOrderVo.setRefUserWxOpenid(groubaOrderVo.getRefUserWxOpenid());
		groubaOrderVo.setGroubaSize(groubActivity.getGroubaSize());
		groubaOrderVo.setOrderExpiredTime(DateUtil.dfyyyy_MM_ddhhmmss.format(
				DateUtil.add(new Date(), Calendar.MINUTE, Integer.parseInt(groubaOrderVo.getOrderExpiredTime()))));
		groubaOrderVo.setLeader(groubaOrderVo.getRefUserWxUnionid());
		return groubaOrderMapper.insert(groubaOrderVo) > 0;
	}

	/**
	 * 参团服务
	 * 
	 * @param groubaOrderVo
	 * @return
	 * @throws Exception
	 */
	public boolean orderJoin(GroubaOrder groubaOrderVo) throws Exception {
		// #入参校验
		if (StringUtils.isEmpty(groubaOrderVo.getOrderTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "OrderTrace");
		}
		if (StringUtils.isEmpty(groubaOrderVo.getRefUserWxUnionid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refUserWxUnionid");
		}
		if (StringUtils.isEmpty(groubaOrderVo.getRefUserWxOpenid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "RefUserWxOpenid");
		}
		if (StringUtils.isEmpty(groubaOrderVo.getLeader())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "Leader");
		}
		if (StringUtils.isEmpty(groubaOrderVo.getRefUserImg())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refUserImg");
		}
		if (StringUtils.isEmpty(groubaOrderVo.getFormId())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "FormId");
		}
		logParams(groubaOrderVo);
		GroubaOrder orderLeader = groubaOrderMapper.selectOne(groubaOrderVo.getOrderTrace(), groubaOrderVo.getLeader());
		if (orderLeader == null) {
			throw new ServiceException(RespCode.order_unOpenOrder);
		}
		if (DateUtil.compareDate(orderLeader.getOrderExpiredTime(), new Date()) <= 0) {// 拼团有效时长已过
			throw new ServiceException(RespCode.order_joinTimeExpired);
		}
		int orderCount4User = groubaOrderMapper.selectOrderCount4User(orderLeader.getRefGroubaTrace(),
				groubaOrderVo.getRefUserWxUnionid());
		if (orderLeader.getGroubaIsnew() == 1 && orderCount4User >= 1) {// 老用户不能进拉新团
			throw new ServiceException(RespCode.order_joinedRepeat);
		}

		log.info("#参团业务校验通过,#orderTrace:[{}]", groubaOrderVo.getOrderTrace());
		GroubaOrder groubaOrderParams = new GroubaOrder();
		groubaOrderParams.setOrderTrace(groubaOrderVo.getOrderTrace());
		groubaOrderParams.setRefGroubaTrace(orderLeader.getRefGroubaTrace());
		groubaOrderParams.setRefGroubTrace(orderLeader.getRefGroubTrace());
		groubaOrderParams.setOrderExpiredTime(orderLeader.getOrderExpiredTime());
		groubaOrderParams.setRefUserWxUnionid(groubaOrderVo.getRefUserWxUnionid());
		groubaOrderParams.setLeader(orderLeader.getLeader());
		groubaOrderParams.setRefUserImg(groubaOrderVo.getRefUserImg());
		groubaOrderParams.setRefUserWxOpenid(groubaOrderVo.getRefUserWxOpenid());
		groubaOrderParams.setGroubaSize(orderLeader.getGroubaSize());
		groubaOrderParams.setGoodsName(orderLeader.getGoodsName());
		groubaOrderParams.setGoodsImg(orderLeader.getGoodsImg());
		groubaOrderParams.setGoodsPrice(orderLeader.getGoodsPrice());
		groubaOrderParams.setGroubaDiscountAmount(orderLeader.getGroubaDiscountAmount());
		groubaOrderParams.setGroubaIsnew(orderLeader.getGroubaIsnew());
		groubaOrderParams.setClientIp(groubaOrderVo.getClientIp());
		groubaOrderParams.setFormId(groubaOrderVo.getFormId());
		log.info("#成团处理>>>>>>>>>>>>>> A");
		GroubActivity groubActivity = groubActivityCache.selectOne(orderLeader.getRefGroubaTrace());
		synchronized (groubActivity) {
			log.info("#成团处理>>>>>>>>>>>>>> B1");
			int orderCount = groubaOrderMapper.selectCount(groubaOrderVo.getOrderTrace(),
					orderLeader.getRefGroubTrace());
			if (orderCount <= groubaSize[groubActivity.getGroubaSize()] - 1) {
				try {
					groubaOrderMapper.insert(groubaOrderParams);
					log.info("#成团处理>>>>>>>>>>>>>> B2");
				} catch (DuplicateKeyException e) {
					throw new ServiceException(RespCode.order_joinedGrouba);
				}
				if (orderCount == groubaSize[groubActivity.getGroubaSize()] - 1) {
					groubaOrderMapper.update(new GroubaOrder(orderLeader.getOrderTrace(), null,
							OrderStatus.join_success.getCode() + ""));
					log.info("#成团处理>>>>>>>>>>>>>> B3");
					// 异步发送成团通知，for团所有成员
					msgSendService.wxMsgSend4Joined(orderLeader.getGoodsName(), orderLeader.getLeader(),
							orderLeader.getGroubaSize(), orderLeader.getOrderTrace());
				}
			} else {
				throw new ServiceException(RespCode.order_groubaFull);
			}
		}
		return true;
	}

	private void logParams(GroubaOrder groubaOrder) {
		log.info("#入参校验通过:[{}]", JSONObject.toJSON(groubaOrder));
	}

	/**
	 * 扫描消费订单
	 * 
	 * @author chenzhao @date May 15, 2019
	 * @param groubaOrderVo
	 * @return
	 */
	public boolean orderConsume(GroubaOrder groubaOrderVo) {
		// #入参校验
		if (StringUtils.isEmpty(groubaOrderVo.getOrderTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "OrderTrace");
		}
		if (StringUtils.isEmpty(groubaOrderVo.getRefUserWxUnionid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refUserWxUnionid");
		}
		if (StringUtils.isEmpty(groubaOrderVo.getRefGroubTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refGroubTrace");
		}
		logParams(groubaOrderVo);
		// #根据消费二维码信息，查询消费订单
		GroubaOrder oldOrder = groubaOrderMapper.selectOne(groubaOrderVo.getOrderTrace(),
				groubaOrderVo.getRefUserWxUnionid());
		if (oldOrder == null || Integer.parseInt(oldOrder.getOrderStatus()) < OrderStatus.join_success.getCode()) {
			throw new ServiceException(RespCode.order_unJoinSuccess);
		}
		if (Integer.parseInt(oldOrder.getOrderStatus()) == OrderStatus.consume_success.getCode()) {
			throw new ServiceException(RespCode.order_unRepeatConsume);
		}
		if (!groubaOrderVo.getRefGroubTrace().equals(oldOrder.getRefGroubTrace())) {
			throw new ServiceException(RespCode.order_groubError);
		}

		GroubaOrder groubaOrderParams = new GroubaOrder();
		groubaOrderParams.setOrderTrace(groubaOrderVo.getOrderTrace());
		groubaOrderParams.setRefUserWxUnionid(groubaOrderVo.getRefUserWxUnionid());
		groubaOrderParams.setConsumeSuccessTime("udpate");
		groubaOrderParams.setOrderStatus(OrderStatus.consume_success.getCode() + "");

		int count = groubaOrderMapper.update(groubaOrderParams);
		// 异步发送消费通知，for当前消费成员
		msgSendService.wxMsgSend4Consumed(oldOrder.getGoodsName(), oldOrder.getRefUserWxOpenid(),
				oldOrder.getRefUserWxUnionid(), oldOrder.getRefGroubTrace(), oldOrder.getIntime(),
				oldOrder.getFormId());
		// 如果全团成员消费完成，则通知商家 TODO

		return count > 0;
	}

	/**
	 * 拼团准备消费
	 * 
	 * @author ack @date Jun 7, 2019
	 * @param groubaOrderVo
	 * @return
	 */
	public boolean orderConsumePrepare(GroubaOrder groubaOrderVo) {
		// #入参校验
		if (StringUtils.isEmpty(groubaOrderVo.getOrderTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "OrderTrace");
		}
		if (StringUtils.isEmpty(groubaOrderVo.getRefUserWxUnionid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refUserWxUnionid");
		}
		logParams(groubaOrderVo);
		// #根据消费二维码信息，查询消费订单
		GroubaOrder oldOrder = groubaOrderMapper.selectOne(groubaOrderVo.getOrderTrace(),
				groubaOrderVo.getRefUserWxUnionid());
		if (oldOrder == null || Integer.parseInt(oldOrder.getOrderStatus()) < OrderStatus.join_success.getCode()) {
			throw new ServiceException(RespCode.order_unJoinSuccess);
		}
		if (Integer.parseInt(oldOrder.getOrderStatus()) == OrderStatus.consume_success.getCode()) {
			throw new ServiceException(RespCode.order_unRepeatConsume);
		}

		GroubaOrder groubaOrderParams = new GroubaOrder();
		groubaOrderParams.setOrderTrace(groubaOrderVo.getOrderTrace());
		groubaOrderParams.setRefUserWxUnionid(groubaOrderVo.getRefUserWxUnionid());
		groubaOrderParams.setConsumeTime("udpate");
		groubaOrderParams.setOrderStatus(OrderStatus.consume.getCode() + "");
		groubaOrderParams.setFormId(groubaOrderVo.getFormId());

		int count = groubaOrderMapper.update(groubaOrderParams);
		return count > 0;
	}

	public Page<?> select(GroubaOrder groubaOrder) {
		// #入参校验
		if (StringUtils.isEmpty(groubaOrder.getRefGroubTrace()) && StringUtils.isEmpty(groubaOrder.getRefGroubaTrace())
				&& StringUtils.isEmpty(groubaOrder.getOrderStatus())
				&& StringUtils.isEmpty(groubaOrder.getRefUserWxUnionid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refroubTrace、refGroubaTrace、orderStatus，至少传一个");
		}
		logParams(groubaOrder);
		Page<?> page = PageHelper.startPage(groubaOrder.getPage(), groubaOrder.getRows());
		groubaOrderMapper.select(null, groubaOrder.getRefGroubTrace(), groubaOrder.getRefGroubaTrace(),
				groubaOrder.getOrderStatus(), null);
		return page;
	}

	/**
	 * 我的订单查询4普通用户
	 * 
	 * @param groubaOrder
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> getMyOrder4user(GroubaOrder groubaOrder) {
		// #入参校验
		if (StringUtils.isEmpty(groubaOrder.getRefUserWxUnionid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "RefUserWxUnionid");
		}
		logParams(groubaOrder);
		Page<?> page = PageHelper.startPage(groubaOrder.getPage(), groubaOrder.getRows());
		groubaOrderMapper.selectMyOrder4user(groubaOrder.getRefUserWxUnionid());
		List<GroubaOrder> groubaOrderList = (List<GroubaOrder>) page.getResult();
		StringBuffer orderTraces = new StringBuffer();
		List<GroubaOrder> userImgs = null;
		for (int i = 0; i < groubaOrderList.size(); i++) {
			orderTraces.append("'").append(groubaOrderList.get(i).getOrderTrace()).append("',");
		}
		if (orderTraces.length() > 0) {
			userImgs = groubaOrderMapper.selectMyOrder4userImgs(orderTraces.substring(0, orderTraces.lastIndexOf(",")),
					null);
		}
		log.info("#用户所有订单查询end-查询订单同团订单头像、状态start,#RefUserWxUnionid:[{}]", groubaOrder.getRefUserWxUnionid());
		Map<String, Object> orderImgMap = MapBeanUtil.objListToMap(userImgs, "orderTrace");
		for (int i = 0; i < groubaOrderList.size(); i++) {
			String orderTrace = groubaOrderList.get(i).getOrderTrace();
			if (orderImgMap.containsKey(orderTrace)) {
				GroubaOrder orderImgs = (GroubaOrder) orderImgMap.get(orderTrace);
				groubaOrderList.get(i).setOrderRefUsers(orderImgs.getOrderRefUsers());
				groubaOrderList.get(i).setUserImgs(orderImgs.getUserImgs());
				groubaOrderList.get(i).setOrdersStatus(orderImgs.getOrdersStatus());
				groubaOrderList.get(i).setShareOrder(orderImgs.getOrderTrace());
				groubaOrderList.get(i).setShareLeader(orderImgs.getLeader());
			}
		}
		log.info("#用户所有订单查询end-查询订单同团订单头像、状态end,#RefUserWxUnionid:[{}]，#groubaOrderList:[{}]",
				groubaOrder.getRefUserWxUnionid(), JSONObject.toJSON(groubaOrderList));
		// 组装相应报文
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("total", page.getTotal());
		map.put("rows", groubaOrderList);
		map.put("retCode", RespCode.SUCCESS.getCode());
		map.put("retMsg", RespCode.SUCCESS.getMsg());
		return map;
	}

	/**
	 * 我的订单查询4店长
	 * 
	 * @param groubaOrder
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> getMyOrder4Shop(GroubaOrder groubaOrder) {
		// #入参校验
		if (StringUtils.isEmpty(groubaOrder.getRefGroubTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "RefGroubTrace");
		}
		logParams(groubaOrder);
		Page<?> page = PageHelper.startPage(groubaOrder.getPage(), groubaOrder.getRows());
		groubaOrderMapper.selectMyOrder4Shop(groubaOrder.getRefGroubTrace());
		List<GroubaOrder> groubaOrderList = (List<GroubaOrder>) page.getResult();
		StringBuffer orderTraces = new StringBuffer();
		List<GroubaOrder> userImgs = null;
		for (int i = 0; i < groubaOrderList.size(); i++) {
			orderTraces.append("'").append(groubaOrderList.get(i).getOrderTrace()).append("',");
		}
		if (orderTraces.length() > 0) {
			userImgs = groubaOrderMapper.selectMyOrder4userImgs(orderTraces.substring(0, orderTraces.lastIndexOf(",")),
					null);
		}
		log.info("#用户所有订单查询end-查询订单同团订单头像、状态start,#RefUserWxUnionid:[{}]", groubaOrder.getRefUserWxUnionid());
		Map<String, Object> orderImgMap = MapBeanUtil.objListToMap(userImgs, "orderTrace");
		for (int i = 0; i < groubaOrderList.size(); i++) {
			String orderTrace = groubaOrderList.get(i).getOrderTrace();
			if (orderImgMap.containsKey(orderTrace)) {
				GroubaOrder orderImgs = (GroubaOrder) orderImgMap.get(orderTrace);
				groubaOrderList.get(i).setOrderRefUsers(orderImgs.getOrderRefUsers());
				groubaOrderList.get(i).setUserImgs(orderImgs.getUserImgs());
				groubaOrderList.get(i).setOrdersStatus(orderImgs.getOrdersStatus());
				groubaOrderList.get(i).setShareOrder(orderImgs.getOrderTrace());
				groubaOrderList.get(i).setShareLeader(orderImgs.getLeader());
			}
		}
		log.info("#用户所有订单查询end-查询订单同团订单头像、状态end,#RefUserWxUnionid:[{}]，#groubaOrderList:[{}]",
				groubaOrder.getRefUserWxUnionid(), JSONObject.toJSON(groubaOrderList));
		// 组装相应报文
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("total", page.getTotal());
		map.put("rows", groubaOrderList);
		map.put("retCode", RespCode.SUCCESS.getCode());
		map.put("retMsg", RespCode.SUCCESS.getMsg());
		return map;
	}

}
