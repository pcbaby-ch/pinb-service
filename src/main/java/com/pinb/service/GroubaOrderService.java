/**
 * 
 */
package com.pinb.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinb.common.BusinessesFlowNum;
import com.pinb.common.ServiceException;
import com.pinb.constant.RedisConst;
import com.pinb.entity.GroubaOrder;
import com.pinb.enums.OrderStatus;
import com.pinb.enums.RespCode;
import com.pinb.mapper.GroubaOrderMapper;

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

	public boolean orderOpen(GroubaOrder groubaOrder) {
		// #入参校验
		if (StringUtils.isEmpty(groubaOrder.getRefGroubaTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "RefGroubaTrace");
		}
		if (StringUtils.isEmpty(groubaOrder.getRefGroubTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "RefGroubTrace");
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
		log.info("#入参校验通过,#OrderTrace:[{}]", groubaOrder.getOrderTrace());
		groubaOrder.setOrderTrace(BusinessesFlowNum.getNum("GO", RedisConst.groubaOrderTrace));
		int count = groubaOrderMapper.insert(groubaOrder);
		if (count > 0) {
			return true;
		} else {
			throw new ServiceException(RespCode.FAILURE);
		}
	}

	public boolean orderShare(GroubaOrder groubaOrder) {
		// #入参校验
		if (StringUtils.isEmpty(groubaOrder.getOrderTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "OrderTrace");
		}
		if (StringUtils.isEmpty(groubaOrder.getRefUserWxUnionid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refUserWxUnionid");
		}
		log.info("#入参校验通过,#OrderTrace:[{}]", groubaOrder.getOrderTrace());
		GroubaOrder groubaOrderParams = new GroubaOrder();
		groubaOrderParams.setOrderTrace(groubaOrder.getOrderTrace());
		groubaOrderParams.setRefUserWxUnionid(groubaOrder.getRefUserWxUnionid());
		groubaOrderParams.setOrderShareCount("update");

		int count = groubaOrderMapper.update(groubaOrderParams);
		if (count > 0) {
			return true;
		} else {
			throw new ServiceException(RespCode.FAILURE);
		}
	}

	public boolean orderJoin(GroubaOrder groubaOrder) {
		// #入参校验
		if (StringUtils.isEmpty(groubaOrder.getOrderTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "OrderTrace");
		}
		if (StringUtils.isEmpty(groubaOrder.getRefUserWxUnionid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refUserWxUnionid");
		}
		if (StringUtils.isEmpty(groubaOrder.getRefUserImg())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refUserImg");
		}
		log.info("#入参校验通过,#OrderTrace:[{}]", groubaOrder.getOrderTrace());
		// #判断是否开团+成团
		List<GroubaOrder> oldOrderList = groubaOrderMapper.select(groubaOrder.getOrderTrace(), null, null, null, null);
		GroubaOrder oldOrder = null;
		if (!oldOrderList.isEmpty()) {
			oldOrder = oldOrderList.get(0);
		} else {
			throw new ServiceException(RespCode.order_unOpenOrder);
		}

		GroubaOrder groubaOrderParams = new GroubaOrder();
		groubaOrderParams.setOrderTrace(groubaOrder.getOrderTrace());
		groubaOrderParams.setRefGroubaTrace(oldOrder.getRefGroubaTrace());
		groubaOrderParams.setRefGroubTrace(oldOrder.getRefGroubTrace());
		groubaOrderParams.setOrderExpiredTime(oldOrder.getOrderExpiredTime());
		groubaOrderParams.setRefUserWxUnionid(groubaOrder.getRefUserWxUnionid());
		groubaOrderParams.setRefUserImg(groubaOrder.getRefUserImg());
		int count = groubaOrderMapper.insert(groubaOrderParams);
		if (count > 0) {
			return true;
		} else {
			throw new ServiceException(RespCode.FAILURE);
		}
	}

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
		log.info("#入参校验通过,#OrderTrace:[{}]", groubaOrder.getOrderTrace());
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
		if (count > 0) {
			return true;
		} else {
			throw new ServiceException(RespCode.FAILURE);
		}
	}

	public Page<?> select(GroubaOrder groubaOrder) {
		// #入参校验
		if (StringUtils.isEmpty(groubaOrder.getRefGroubTrace()) && StringUtils.isEmpty(groubaOrder.getRefGroubaTrace())
				&& StringUtils.isEmpty(groubaOrder.getOrderStatus())
				&& StringUtils.isEmpty(groubaOrder.getRefUserWxUnionid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refroubTrace、refGroubaTrace、orderStatus，至少传一个");
		}
		log.info("#入参校验通过,#OrderTrace:[{}]", groubaOrder.getOrderTrace());
		Page<?> page = PageHelper.startPage(groubaOrder.getPage(), groubaOrder.getRows());
		groubaOrderMapper.select(null, groubaOrder.getRefGroubTrace(), groubaOrder.getRefGroubaTrace(),
				groubaOrder.getOrderStatus(), null);
		return page;
	}

	/**
	 * 查询分享订单
	 * 
	 * @author chenzhao @date May 8, 2019
	 * @param groubaOrder
	 * @return
	 */
	public List<GroubaOrder> selectShareOrder(GroubaOrder groubaOrder) {
		// #入参校验
		if (StringUtils.isEmpty(groubaOrder.getRefGroubTrace()) && StringUtils.isEmpty(groubaOrder.getRefGroubaTrace())
				&& StringUtils.isEmpty(groubaOrder.getOrderStatus())
				&& StringUtils.isEmpty(groubaOrder.getRefUserWxUnionid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refroubTrace、refGroubaTrace、orderStatus，至少传一个");
		}
		log.info("#入参校验通过,#OrderTrace:[{}]", groubaOrder.getOrderTrace());
		return groubaOrderMapper.select(null, groubaOrder.getRefGroubTrace(), groubaOrder.getRefGroubaTrace(),
				groubaOrder.getOrderStatus(), null);
	}

}
