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
import com.pinb.entity.GroubActivity;
import com.pinb.entity.User;
import com.pinb.enums.RespCode;
import com.pinb.mapper.GroubActivityMapper;
import com.pinb.util.RespUtil;

/**
 * 拼吧-活动
 * 
 * @author chenzhao @date Apr 15, 2019
 */
@Service
public class GroubActivityService {

	private static final Logger log = LoggerFactory.getLogger(GroubActivityService.class);

	@Autowired
	GroubActivityMapper groubActivityMapper;

	public boolean add(GroubActivity groubActivity) {
		// #入参校验
		if (StringUtils.isEmpty(groubActivity.getRefGroubTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "RefGroubTrace");
		}
		if (StringUtils.isEmpty(groubActivity.getRefUserWxUnionid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "RefUserWxUnionid");
		}
		if (StringUtils.isEmpty(groubActivity.getGroubaSize())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GroubaSize");
		}
		if (StringUtils.isEmpty(groubActivity.getGroubaMaxCount())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GroubaMaxCount");
		}
		if (StringUtils.isEmpty(groubActivity.getGoodsName())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GoodsName");
		}
		if (StringUtils.isEmpty(groubActivity.getGoodsImg())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GoodsImg");
		}
		if (StringUtils.isEmpty(groubActivity.getGoodsPrice())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GoodsPrice");
		}
		if (StringUtils.isEmpty(groubActivity.getGroubaDiscountAmount())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GroubaDiscountAmount");
		}
		if (StringUtils.isEmpty(groubActivity.getGroubaIsnew())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GroubaIsnew");
		}
		log.info("#入参校验通过");
		groubActivity.setGroubaTrace(BusinessesFlowNum.getNum("GA", RedisConst.groubActivityTrace));
		int count = groubActivityMapper.insert(groubActivity);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean update(GroubActivity groubActivity) {
		// #入参校验
		if (StringUtils.isEmpty(groubActivity.getGroubaTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GroubaTrace");
		}
		log.info("#入参校验通过,#GroubaTrace:[{}]", groubActivity.getGroubaTrace());
		int count = groubActivityMapper.update(groubActivity);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	public Object select(GroubActivity groubActivity) {
		// #入参校验
		if (StringUtils.isEmpty(groubActivity.getRefGroubTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refGroubTrace");
		}
		log.info("#入参校验通过,#GroubaTrace:[{}]", groubActivity.getGroubaTrace());
		Page<?> page = PageHelper.startPage(groubActivity.getPage(), groubActivity.getRows());
		groubActivityMapper.select(groubActivity.getRefGroubTrace(), null);
		return RespUtil.listResp(page);
	}

	public Object selectOne(GroubActivity groubActivity) {
		// #入参校验
		if (StringUtils.isEmpty(groubActivity.getGroubaTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "getGroubaTrace");
		}
		log.info("#入参校验通过,#GroubaTrace:[{}]", groubActivity.getGroubaTrace());
		return RespUtil.dataResp(groubActivityMapper.selectOne(groubActivity.getGroubaTrace()));
	}

}
