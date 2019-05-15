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
import com.pinb.entity.GroubaOrder;
import com.pinb.enums.OrderStatus;
import com.pinb.enums.RespCode;
import com.pinb.mapper.GroubaOrderMapper;
import com.pinb.util.BeanUtil;
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
	GroubaOrderMapper groubaOrderMapper;
	@Autowired
	GroubActivityService groubActivityService;

	/**
	 * 开团服务
	 * 
	 * @param groubaOrder
	 * @return
	 * @throws Exception
	 */
	public boolean orderOpen(GroubaOrder groubaOrder) throws Exception {
		// #入参校验
		if (StringUtils.isEmpty(groubaOrder.getRefGroubaTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refGroubaTrace");
		}
		if (StringUtils.isEmpty(groubaOrder.getRefGroubTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refGroubTrace");
		}
		if (StringUtils.isEmpty(groubaOrder.getOrderExpiredTime())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "orderExpiredTime");
		}
		if (StringUtils.isEmpty(groubaOrder.getRefUserWxUnionid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refUserWxUnionid");
		}
		if (StringUtils.isEmpty(groubaOrder.getRefUserImg())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refUserImg");
		}
		if (StringUtils.isEmpty(groubaOrder.getGoodsName())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "goodsName");
		}
		if (StringUtils.isEmpty(groubaOrder.getGoodsImg())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "goodsImg");
		}
		if (StringUtils.isEmpty(groubaOrder.getGoodsPrice())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "goodsPrice");
		}
		if (StringUtils.isEmpty(groubaOrder.getGroubaDiscountAmount())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "groubaDiscountAmount");
		}
		if (StringUtils.isEmpty(groubaOrder.getGroubaIsnew())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "groubaIsnew");
		}
		logParams(groubaOrder);
		groubaOrder.setOrderTrace(BusinessesFlowNum.getNum("GO", RedisConst.groubaOrderTrace));
		groubaOrder.setOrderExpiredTime(DateUtil.dfyyyy_MM_ddhhmmss.format(
				DateUtil.add(new Date(), Calendar.MINUTE, Integer.parseInt(groubaOrder.getOrderExpiredTime()))));
		int count = groubaOrderMapper.insert(groubaOrder);
		groubActivityService.share(groubaOrder.getRefGroubaTrace());
		if (count > 0) {
			return true;
		} else {
			throw new ServiceException(RespCode.FAILURE);
		}
	}

	/**
	 * 参团服务
	 * 
	 * @param groubaOrder
	 * @return
	 */
	public boolean orderJoin(GroubaOrder groubaOrder) {
		// #入参校验
		if (StringUtils.isEmpty(groubaOrder.getOrderTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "OrderTrace");
		}
		if (StringUtils.isEmpty(groubaOrder.getRefUserWxUnionid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refUserWxUnionid");
		}
		if (StringUtils.isEmpty(groubaOrder.getShareUser())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "ShareUser");
		}
		if (StringUtils.isEmpty(groubaOrder.getRefUserImg())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refUserImg");
		}
		logParams(groubaOrder);
		// #判断是否开团+成团数上限 TODO?????????????
		GroubaOrder oldOrder = groubaOrderMapper.selectOne(groubaOrder.getOrderTrace(),
				groubaOrder.getRefUserWxUnionid());
		if (BeanUtil.checkFieldValueNull(oldOrder)) {
			throw new ServiceException(RespCode.order_unOpenOrder);
		}

		GroubaOrder groubaOrderParams = new GroubaOrder();
		groubaOrderParams.setOrderTrace(groubaOrder.getOrderTrace());
		groubaOrderParams.setRefGroubaTrace(oldOrder.getRefGroubaTrace());
		groubaOrderParams.setRefGroubTrace(oldOrder.getRefGroubTrace());
		groubaOrderParams.setOrderExpiredTime(oldOrder.getOrderExpiredTime());
		groubaOrderParams.setRefUserWxUnionid(groubaOrder.getShareUser());
		groubaOrderParams.setRefUserImg(groubaOrder.getRefUserImg());
		groubaOrderParams.setGoodsName(oldOrder.getGoodsName());
		groubaOrderParams.setGoodsImg(oldOrder.getGoodsImg());
		groubaOrderParams.setGoodsPrice(oldOrder.getGoodsPrice());
		groubaOrderParams.setGroubaDiscountAmount(oldOrder.getGroubaDiscountAmount());
		groubaOrderParams.setGroubaIsnew(oldOrder.getGroubaIsnew());
		int count;
		try {
			count = groubaOrderMapper.insert(groubaOrderParams);
		} catch (DuplicateKeyException e) {
			throw new ServiceException(RespCode.order_joinedGrouba);
		}
		groubActivityService.share(groubaOrder.getRefGroubaTrace());
		return count > 0;
	}

	private void logParams(GroubaOrder groubaOrder) {
		log.info("#入参校验通过:[{}]", JSONObject.toJSON(groubaOrder));
	}

	/**
	 * 扫描消费订单
	 * 
	 * @author chenzhao @date May 15, 2019
	 * @param groubaOrder
	 * @return
	 */
	public boolean orderConsume(GroubaOrder groubaOrder) {
		// #入参校验
		if (StringUtils.isEmpty(groubaOrder.getOrderTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "OrderTrace");
		}
		if (StringUtils.isEmpty(groubaOrder.getRefUserWxUnionid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refUserWxUnionid");
		}
		if (StringUtils.isEmpty(groubaOrder.getRefGroubTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refGroubTrace");
		}
		logParams(groubaOrder);
		// #根据消费二维码信息，查询消费订单
		GroubaOrder oldOrder = groubaOrderMapper.selectOne(groubaOrder.getOrderTrace(), groubaOrder.getWxUnionid());
		if (oldOrder == null || Integer.parseInt(oldOrder.getOrderStatus()) < OrderStatus.join_success.getCode()) {
			throw new ServiceException(RespCode.order_unJoinSuccess);
		}
		if (Integer.parseInt(oldOrder.getOrderStatus()) == OrderStatus.consume_success.getCode()) {
			throw new ServiceException(RespCode.order_unRepeatConsume);
		}
		if (!groubaOrder.getRefGroubTrace().equals(oldOrder.getRefGroubTrace())) {
			throw new ServiceException(RespCode.order_groubError);
		}

		GroubaOrder groubaOrderParams = new GroubaOrder();
		groubaOrderParams.setOrderTrace(groubaOrder.getOrderTrace());
		groubaOrderParams.setRefUserWxUnionid(groubaOrder.getRefUserWxUnionid());
		groubaOrderParams.setConsumeSuccessTime("udpate");
		groubaOrderParams.setOrderStatus(OrderStatus.consume_success.getCode() + "");

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
	public HashMap selectMyOrder4user(GroubaOrder groubaOrder) {
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
			userImgs = groubaOrderMapper.selectMyOrder4userImgs(orderTraces.substring(0, orderTraces.lastIndexOf(",")));
		}
		log.info("#用户所有订单查询end-查询订单同团订单头像、状态start,#RefUserWxUnionid:[{}]", groubaOrder.getRefUserWxUnionid());
		Map<String, Object> goMap = MapBeanUtil.objListToMap(userImgs, "orderTrace");
		for (int i = 0; i < groubaOrderList.size(); i++) {
			String orderTrace = groubaOrderList.get(i).getOrderTrace();
			if (goMap.containsKey(orderTrace)) {
				groubaOrderList.get(i).setUserImgs(((GroubaOrder) goMap.get(orderTrace)).getUserImgs());
				groubaOrderList.get(i).setOrdersStatus(((GroubaOrder) goMap.get(orderTrace)).getOrdersStatus());
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
