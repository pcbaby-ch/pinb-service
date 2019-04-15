/**
 * 
 */
package com.pinb.service;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinb.common.ServiceException;
import com.pinb.entity.User;
import com.pinb.enums.RespCode;
import com.pinb.mapper.UserMapper;
import com.pinb.util.CheckUtil;
import com.pinb.util.HttpUtil;
import com.pinb.util.RespUtil;
import com.pinb.vo.UserVo;

/**
 * 拼吧-用户
 * 
 * @author chenzhao @date Apr 15, 2019
 */
@Service
public class UserService {

	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	@Autowired
	UserMapper userMapper;

	public boolean add(User user) {
		// #入参校验
		if (StringUtils.isEmpty(user.getUserWxOpenid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "getUserWxOpenid");
		}
		if (StringUtils.isEmpty(user.getUserWxUnionid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "getUserWxUnionid");
		}
		if (StringUtils.isEmpty(user.getUserPhone())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "getUserPhone");
		}
		if (StringUtils.isEmpty(user.getUserImg())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "getUserImg");
		}
		if (!("0".equals(user.getIsOpenGroub()) || "1".equals(user.getIsOpenGroub()))) {
			throw new ServiceException(RespCode.PARAM_ILLEGAL, "IsOpenGroub");
		}
		log.info("#入参校验通过,#UserWxUnionid:[{}]", user.getUserWxUnionid());
		int count = userMapper.insert(user);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean update(User user) {
		// #入参校验
		if (StringUtils.isEmpty(user.getUserWxUnionid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "UserWxUnionid");
		}
		if (!StringUtils.isEmpty(user.getCreditScoreGroub()) && CheckUtil.isPositiveInteger(user.getCreditScoreGroub())) {
			throw new ServiceException(RespCode.PARAM_ILLEGAL, "CreditScoreGroub");
		}
		if (!StringUtils.isEmpty(user.getCreditScoreUser()) && CheckUtil.isPositiveInteger(user.getCreditScoreUser())) {
			throw new ServiceException(RespCode.PARAM_ILLEGAL, "CreditScoreUser");
		}
		log.info("#入参校验通过,#UserWxUnionid:[{}]", user.getUserWxUnionid());
		int count = userMapper.update(user);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	public Object select(User user) {
		// #入参校验
		if (StringUtils.isEmpty(user.getIsOpenGroub())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "getIsOpenGroub");
		}
		log.info("#入参校验通过,#UserWxUnionid:[{}]", user.getUserWxUnionid());
		Page<?> page = PageHelper.startPage(user.getPage(), user.getRows());
		userMapper.select(user.getIsOpenGroub());
		return RespUtil.listResp(page);
	}

	public Object getOpenid(UserVo user) {
		// #入参校验
		if (StringUtils.isEmpty(user.getAppid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "appid");
		}
		if (StringUtils.isEmpty(user.getSecret())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "secret");
		}
		if (StringUtils.isEmpty(user.getJsCode())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "jsCode");
		}
		if (StringUtils.isEmpty(user.getGrantType())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "grantType");
		}
		log.info("#入参校验通过");
		Page<?> page = PageHelper.startPage(user.getPage(), user.getRows());
		String url = "https://api.weixin.qq.com/sns/jscode2session";
		HashMap<String, Object> wxreq = new HashMap<>();
		wxreq.put("appid", user.getAppid());
		wxreq.put("secret", user.getSecret());
		wxreq.put("js_code", user.getJsCode());
		wxreq.put("grant_type", user.getGrantType());

		String wxresp = HttpUtil.doGet(url, wxreq);

		return RespUtil.dataResp(JSONObject.parseObject(wxresp));
	}

}
