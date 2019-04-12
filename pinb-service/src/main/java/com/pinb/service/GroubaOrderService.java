/**
 * 
 */
package com.pinb.service;


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
import com.pinb.enums.RespCode;
import com.pinb.mapper.GroubaOrderMapper;
import com.pinb.util.RespUtil;

/**
 * @author chenzhao @date Apr 9, 2019
 */
@Service
public class GroubaOrderService {

	private static final Logger log = LoggerFactory.getLogger(GroubaOrderService.class);

	@Autowired
	GroubaOrderMapper groubaOrderMapper;

	public boolean orderAdd(GroubaOrder groubaOrder) throws Exception {
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

		groubaOrder.setOrderTrace(BusinessesFlowNum.getNum("O", RedisConst.groubaOrderTrace));
		int count = groubaOrderMapper.insert(groubaOrder);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean orderUpdate(GroubaOrder groubaOrder) throws Exception {
		// #入参校验
		if (StringUtils.isEmpty(groubaOrder.getOrderTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "OrderTrace");
		}

		int count = groubaOrderMapper.update(groubaOrder);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	public Object orderSelect(GroubaOrder groubaOrder) throws Exception {
		// #入参校验

		Page<?> page = PageHelper.startPage(groubaOrder.getPage(), groubaOrder.getRows());
		groubaOrderMapper.select(groubaOrder.getRefGroubTrace(), groubaOrder.getRefGroubaTrace(),
				groubaOrder.getOrderStatus(), groubaOrder.getRefUserWxUnionid());
		return RespUtil.listResp(page);
	}

}
