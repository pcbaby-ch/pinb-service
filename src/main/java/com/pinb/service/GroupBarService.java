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

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinb.common.BusinessesFlowNum;
import com.pinb.common.ServiceException;
import com.pinb.config.RedisPool;
import com.pinb.constant.RedisConst;
import com.pinb.entity.GroubActivity;
import com.pinb.entity.GroupBar;
import com.pinb.entity.User;
import com.pinb.enums.RespCode;
import com.pinb.mapper.GroupBarMapper;

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
	GroubActivityService groubActivityService;
	@Autowired
	private UserService userService;

	@SuppressWarnings("unchecked")
	public boolean add(String reqStr) {
		JSONObject reqJson = JSONObject.parseObject(reqStr);
		GroupBar groupBar = reqJson.getObject("groub", GroupBar.class);
		User user = reqJson.getObject("userinfo", User.class);
		List<GroubActivity> groubActivityList = reqJson.getObject("goodsList", List.class);

		// #入参校验
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
		logParams(groupBar);
		if (StringUtils.isEmpty(user.getWxUnionid())) {
			user.setWxUnionid(user.getWxOpenid());
		}

		String groubTrace = BusinessesFlowNum.getNum("G", RedisConst.groupBarTrace);
		groupBar.setGroubTrace(groubTrace);
		groupBar.setRefUserWxUnionid(user.getWxUnionid());
		log.info("#保存店铺信息start,#GroubTrace:[{}]", groupBar.getGroubTrace());
		boolean groubResult = groupBarMapper.insert(groupBar) > 0;
		log.info("#保存店铺信息end-保存用户信息start");
		boolean userResult = userService.add(user);
		log.info("#保存用户信息end-保存商品信息start");
		boolean groubActivityResult = false;
		for (int i = 0; i < groubActivityList.size(); i++) {
			GroubActivity groubActivity = groubActivityList.get(i);
			groubActivity.setGroubaTrace(BusinessesFlowNum.getNum("GA", RedisConst.groubActivityTrace));
			groubActivity.setRefGroubTrace(groubTrace);
			groubActivity.setRefUserWxUnionid(user.getWxUnionid());
			groubActivity.setLatitude(user.getLatitude());
			groubActivity.setLongitude(user.getLongitude());
			// #如果此商品没有设置图片，则直接忽略跳过
			if (StringUtils.isEmpty(groubActivity.getGoodsImg())) {
				continue;
			}
			groubActivityResult = groubActivityService.add(groubActivity);
		}
		if (groubResult && userResult && groubActivityResult) {
			log.info("#店铺入驻成功,#GroubTrace:[{}]", groupBar.getGroubTrace());
		}
		return true;
	}

	private void logParams(GroupBar groupBar) {
		log.info("#入参校验通过,#GroubTrace:[{}]", groupBar.getGroubTrace());
	}

	public boolean update(GroupBar groupBar) {
		// #入参校验
		if (StringUtils.isEmpty(groupBar.getGroubTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GroubTrace");
		}
		logParams(groupBar);
		int count = groupBarMapper.update(groupBar);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
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

	public Object selectOne(GroupBar groupBar) {
		// #入参校验
		if (StringUtils.isEmpty(groupBar.getRefUserWxUnionid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refUserWxUnionid");
		}
		logParams(groupBar);
		return groupBarMapper.selectOne(groupBar.getGroubTrace());
	}

}
