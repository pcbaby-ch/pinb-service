/**
 * 
 */
package com.pinb.service;


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
import com.pinb.util.WxUtil;
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
		if (StringUtils.isEmpty(user.getWxOpenid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "UserWxOpenid");
		}
		if (StringUtils.isEmpty(user.getPhone())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "UserPhone");
		}
		if (StringUtils.isEmpty(user.getHeadImg())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "UserImg");
		}
		if (!("0".equals(user.getIsOpenGroub()) || "1".equals(user.getIsOpenGroub()))) {
			throw new ServiceException(RespCode.PARAM_ILLEGAL, "IsOpenGroub");
		}
		logParams(user);
		if (StringUtils.isEmpty(user.getWxUnionid())) {
			user.setWxUnionid(user.getWxOpenid());
		}
		return userMapper.insert(user) > 0;
	}

	public boolean update(User user) {
		// #入参校验
		if (StringUtils.isEmpty(user.getWxUnionid()) && StringUtils.isEmpty(user.getPhone())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "userWxUnionid|phone");
		}
		if (!StringUtils.isEmpty(user.getCreditScoreGroub())
				&& CheckUtil.isPositiveInteger(user.getCreditScoreGroub())) {
			throw new ServiceException(RespCode.PARAM_ILLEGAL, "CreditScoreGroub");
		}
		if (!StringUtils.isEmpty(user.getCreditScoreUser()) && CheckUtil.isPositiveInteger(user.getCreditScoreUser())) {
			throw new ServiceException(RespCode.PARAM_ILLEGAL, "CreditScoreUser");
		}
		logParams(user);
		return userMapper.update(user) > 0;
	}

	public Page<?> select(User user) {
		// #入参校验
		if (StringUtils.isEmpty(user.getIsOpenGroub())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "getIsOpenGroub");
		}
		logParams(user);
		Page<?> page = PageHelper.startPage(user.getPage(), user.getRows());
		userMapper.select(user.getIsOpenGroub());
		return page;
	}

	public User selectOne(User user) {
		// #入参校验
		if (StringUtils.isEmpty(user.getWxUnionid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "UserWxUnionid");
		}
		logParams(user);
		return userMapper.selectOne(user.getWxUnionid(), null);
	}


	public JSONObject decoderWxData(UserVo user) {
		// #入参校验
		if (StringUtils.isEmpty(user.getEncryptedData())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "encryptedData");
		}
		if (StringUtils.isEmpty(user.getIv())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "iv");
		}
		if (StringUtils.isEmpty(user.getSessionKey())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "SessionKey");
		}
		logParams(user);
		return WxUtil.getUserInfo(user.getEncryptedData(), user.getSessionKey(), user.getIv());
	}

	public Object wxLogin(UserVo user) {
		JSONObject openidJson = WxApiService.getOpenid(user);
		user.setSessionKey(openidJson.getString("session_key"));
		user.setWxOpenid(openidJson.getString("openid"));
		/** unionid获取是否成功{unionid不为空&unionid不等于openid} ***************************/
		user.setWxUnionid(StringUtils.isEmpty(openidJson.getString("unionid")) ? openidJson.getString("openid")
				: openidJson.getString("unionid"));
		user.setIsOpenGroub("0");

		log.info("#根据unionid查用户是否存在?start #WxUnionid[{}]", user.getWxUnionid());
		User unionUser = userMapper.selectOne(user.getWxUnionid(), null);
		if (unionUser == null) {
			// 不存在uinion用户
			log.info("#入库新增用户unionid start #WxUnionid[{}]", user.getWxUnionid());
			userMapper.insert(user);
			return user;
		} else {
			log.info("#存在unionUser用户，直接返回用户信息、是否入驻 #WxUnionid[{}]", user.getWxUnionid());
			return unionUser;
		}
	}

	/**
	 *
	 * @author chenzhao @date Apr 26, 2019
	 * @param user
	 */
	private void logParams(User user) {
		log.debug("#入参校验通过:[{}]", JSONObject.toJSON(user));
	}

	public static void main(String[] args) {
		User user = new User();
		user.setCity("");
	}

}
