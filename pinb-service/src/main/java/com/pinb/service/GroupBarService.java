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
import com.pinb.entity.GroupBar;
import com.pinb.enums.RespCode;
import com.pinb.mapper.GroupBarMapper;
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

	public boolean add(GroupBar groupBar) {
		// #入参校验
		if (StringUtils.isEmpty(groupBar.getRefUserWxUnionid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "RefUserWxUnionid");
		}
		if (StringUtils.isEmpty(groupBar.getGroubName())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GroubName");
		}
		if (StringUtils.isEmpty(groupBar.getGroubImg())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GroubImg");
		}
		if (StringUtils.isEmpty(groupBar.getGroubPhone())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GroubPhone");
		}
		if (StringUtils.isEmpty(groupBar.getGroubAddress())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GroubAddress");
		}
		log.info("#入参校验通过,#GroubTrace:[{}]", groupBar.getGroubTrace());
		groupBar.setGroubTrace(BusinessesFlowNum.getNum("G", RedisConst.groupBarTrace));
		int count = groupBarMapper.insert(groupBar);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean update(GroupBar groupBar) {
		// #入参校验
		if (StringUtils.isEmpty(groupBar.getGroubTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GroubTrace");
		}
		log.info("#入参校验通过,#GroubTrace:[{}]", groupBar.getGroubTrace());
		int count = groupBarMapper.update(groupBar);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	public Object select(GroupBar groupBar) {
		// #入参校验
		if (StringUtils.isEmpty(groupBar.getRefUserWxUnionid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "RefUserWxUnionid");
		}
		log.info("#入参校验通过,#GroubTrace:[{}]", groupBar.getGroubTrace());
		Page<?> page = PageHelper.startPage(groupBar.getPage(), groupBar.getRows());
		groupBarMapper.select(groupBar.getRefUserWxUnionid());
		return RespUtil.listResp(page);
	}

	public Object selectOne(GroupBar groupBar) {
		// #入参校验
		if (StringUtils.isEmpty(groupBar.getRefUserWxUnionid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refUserWxUnionid");
		}
		log.info("#入参校验通过,#GroubTrace:[{}]", groupBar.getGroubTrace());
		return RespUtil.dataResp(groupBarMapper.selectOne(groupBar.getGroubTrace()));
	}

}
