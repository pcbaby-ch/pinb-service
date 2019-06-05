/**
 * 
 */
package com.pinb.service;

import java.util.HashMap;
import java.util.UUID;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.pinb.common.ServiceException;
import com.pinb.config.RedisPool;
import com.pinb.constant.RedisConst;
import com.pinb.enums.RespCode;
import com.pinb.util.HttpUtil;
import com.pinb.util.PropertiesUtils;
import com.pinb.vo.UserVo;

/**
 * 拼吧-用户
 * 
 * @author chenzhao @date Apr 15, 2019
 */
public class WxApiService {

	/**
	 * 获取用户openid
	 * 
	 * @author chenzhao @date Apr 26, 2019
	 * @param user 必传{appid、secret、jsCode、grantType}
	 * @return
	 */
	public static JSONObject getOpenid(UserVo user) {
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
		JSONObject openidJson = JSONObject.parseObject(wxresp);
		if (StringUtils.isEmpty(openidJson.getString("openid"))) {
			openidJson.put("openid", "M" + UUID.randomUUID().toString().replaceAll("-", ""));
		}
		return openidJson;
	}

	public static String getAccessToken(String appid, String secret) {
		// #入参校验
		if (StringUtils.isEmpty(appid)) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "appid");
		}
		if (StringUtils.isEmpty(secret)) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "secret");
		}
		String accessToken = null;
		if (RedisPool.exists(RedisConst.accessToken)) {
			accessToken = RedisPool.get(RedisConst.accessToken);
		} else {
			String url = PropertiesUtils.getProperty("wxapiHost", "127.0.0.1:9668/pinb-mock") + "/cgi-bin/token";
			HashMap<String, Object> wxreq = new HashMap<>();
			wxreq.put("appid", appid);
			wxreq.put("secret", secret);
			wxreq.put("grant_type", "client_credential");

			String wxresp = HttpUtil.doGet(url, wxreq);
			accessToken = JSONObject.parseObject(wxresp).getString("access_token");
			if (!StringUtils.isEmpty(accessToken)) {
				RedisPool.set(RedisConst.accessToken, 6000, accessToken);
			}
		}
		return accessToken;
	}

	/**
	 * 获取小程序二维码
	 * 
	 * @param accessToken
	 * @param scene
	 * @return #为string类型：获取失败的json异常响应；#为byte[]时：获取成功
	 */
	public static Object getUnlimited(String accessToken, String scene) {
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

	/**
	 * 发送模板消息
	 * 
	 * @author chenzhao @date May 31, 2019
	 * @param appid
	 * @param secret
	 * @param touser
	 * @param templateId
	 * @return
	 */
	public static Object templateSend(String templateId, String touser, String formId, String groubTrace,
			JSONObject data) {
		// #入参校验
		if (StringUtils.isEmpty(touser)) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "touser");
		}
		if (StringUtils.isEmpty(templateId)) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "templateId");
		}
		String appid = PropertiesUtils.getProperty("wxappid", "wxappid");
		String secret = PropertiesUtils.getProperty("wxsecret", "wxsecret");
		// #获取accessToken
		String accessToken = getAccessToken(appid, secret);
		String url = PropertiesUtils.getProperty("wxapiHost", "127.0.0.1:9668/pinb-mock")
				+ "/cgi-bin/message/wxopen/template/send?access_token=" + accessToken;
		HashMap<String, Object> wxreq = new HashMap<>();
		wxreq.put("touser", touser);
		wxreq.put("template_id", templateId);
		wxreq.put("form_id", formId);
		wxreq.put("page", "/pages/index/index?groubTrace=" + groubTrace + "&pageParamsClear=true");
		wxreq.put("data", data);

		return HttpUtil.doPost(url, JSONObject.toJSONString(wxreq));
	}

	public static void main(String[] args) {
		System.out.println("M" + UUID.randomUUID().toString().replaceAll("-", ""));
	}

}
