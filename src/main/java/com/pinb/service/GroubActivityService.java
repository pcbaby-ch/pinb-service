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

import com.pinb.common.BusinessesFlowNum;
import com.pinb.common.ServiceException;
import com.pinb.constant.RedisConst;
import com.pinb.entity.GroubActivity;
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
		return groubActivityMapper.insert(groubActivity) > 0;
	}

	public boolean update(GroubActivity groubActivity) {
		// #入参校验
		if (StringUtils.isEmpty(groubActivity.getGroubaTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GroubaTrace");
		}
		logParams(groubActivity);
		return groubActivityMapper.update(groubActivity) > 0;
	}

	private void logParams(GroubActivity groubActivity) {
		log.debug("#入参校验通过,#GroubaTrace:[{}]", groubActivity.getGroubaTrace());
	}

	public List<GroubActivity> select(String refGroubTrace, String refUserWxUnionid) {
		// #入参校验
		if (StringUtils.isEmpty(refGroubTrace) && StringUtils.isEmpty(refUserWxUnionid)) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refGroubTrace|refUserWxUnionid");
		}

		return groubActivityMapper.select(refGroubTrace, refUserWxUnionid);
	}

	public boolean delete(String refGroubTrace, String refUserWxUnionid) {
		// #入参校验
		if (StringUtils.isEmpty(refGroubTrace) && StringUtils.isEmpty(refUserWxUnionid)) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refGroubTrace|refUserWxUnionid");
		}
		return groubActivityMapper.delete(refGroubTrace, refUserWxUnionid) > 0;
	}

	public Object selectOne(GroubActivity groubActivity) {
		// #入参校验
		if (StringUtils.isEmpty(groubActivity.getGroubaTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "getGroubaTrace");
		}
		logParams(groubActivity);
		return RespUtil.dataResp(groubActivityMapper.selectOne(groubActivity.getGroubaTrace()));
	}

}
