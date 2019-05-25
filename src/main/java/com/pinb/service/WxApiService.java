/**
 * 
 */
package com.pinb.service;

import java.io.File;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.pinb.common.ServiceException;
import com.pinb.enums.RespCode;
import com.pinb.util.HttpUtil;
import com.pinb.util.PropertiesUtils;
import com.pinb.util.RespUtil;
import com.pinb.vo.UserVo;

/**
 * 拼吧-用户
 * 
 * @author chenzhao @date Apr 15, 2019
 */
@Service
public class WxApiService {

	private static final Logger log = LoggerFactory.getLogger(WxApiService.class);

	/**
	 * 获取用户openid
	 * 
	 * @author chenzhao @date Apr 26, 2019
	 * @param user 必传{appid、secret、jsCode、grantType}
	 * @return
	 */
	public JSONObject getOpenid(UserVo user) {
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
		String url = PropertiesUtils.getProperty("wxapiHost", "127.0.0.1:9668/pinb-mock") + "/sns/jscode2session";
		HashMap<String, Object> wxreq = new HashMap<>();
		wxreq.put("appid", user.getAppid());
		wxreq.put("secret", user.getSecret());
		wxreq.put("js_code", user.getJsCode());
		wxreq.put("grant_type", user.getGrantType());

		String wxresp = HttpUtil.doGet(url, wxreq);

		return JSONObject.parseObject(wxresp);
	}

	public JSONObject getAccessToken(String appid, String secret) {
		// #入参校验
		if (StringUtils.isEmpty(appid)) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "appid");
		}
		if (StringUtils.isEmpty(secret)) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "secret");
		}
		String url = PropertiesUtils.getProperty("wxapiHost", "127.0.0.1:9668/pinb-mock") + "/cgi-bin/token";
		HashMap<String, Object> wxreq = new HashMap<>();
		wxreq.put("appid", appid);
		wxreq.put("secret", secret);
		wxreq.put("grant_type", "client_credential");

		String wxresp = HttpUtil.doGet(url, wxreq);

		return JSONObject.parseObject(wxresp);
	}

	/**
	 * 获取小程序二维码
	 * 
	 * @param accessToken
	 * @param scene
	 * @return #为string类型：获取失败的json异常响应；#为byte[]时：获取成功
	 */
	public Object getUnlimited(String accessToken, String scene) {
		// #入参校验
		if (StringUtils.isEmpty(accessToken)) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "accessToken");
		}
		if (StringUtils.isEmpty(scene)) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "scene");
		}
		String url = PropertiesUtils.getProperty("wxapiHost", "127.0.0.1:9668/pinb-mock")
				+ "/wxa/getwxacodeunlimit?access_token=" + accessToken;
		HashMap<String, Object> wxreq = new HashMap<>();
		wxreq.put("scene", scene);
		wxreq.put("page", "");

		Object wxresp = HttpUtil.doPost4Buffer(url, JSONObject.toJSONString(wxreq));
		if (wxresp instanceof String) {
			return wxresp + "";
		} else {
			return wxresp;
		}
	}

}
