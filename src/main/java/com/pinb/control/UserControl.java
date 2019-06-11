/**
 * 
 */
package com.pinb.control;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.pinb.config.annotation.DecodeReq;
import com.pinb.service.UserService;
import com.pinb.util.IpUtils;
import com.pinb.util.PropertiesUtils;
import com.pinb.util.RespUtil;
import com.pinb.vo.UserVo;

import io.swagger.annotations.Api;

/**
 * 
 * @author chenzhao @date Apr 15, 2019
 *
 */
@Api(tags = "用户相关api", description = "获取微信openid、新用户首次授权后-注册用户、用户信息更新")
@RestController
@RequestMapping("user")
public class UserControl {

	@Autowired
	private UserService userService;

	@RequestMapping("select")
	public Object select() {
		return PropertiesUtils.getProperty("server.port", "server.port");
	}

	@PostMapping("wxLogin")
	public Object wxLogin(@RequestBody @DecodeReq String reqStr, HttpServletRequest req) {
		UserVo userVo = JSONObject.parseObject(reqStr, UserVo.class);
		userVo.setClientIp(IpUtils.getIpFromRequest(req));

		return RespUtil.dataResp(userService.wxLogin(userVo));
	}

	@PostMapping("decoderWxData")
	public Object decoderWxData(@RequestBody @DecodeReq String reqStr) {
		return RespUtil.dataResp(userService.decoderWxData(JSONObject.parseObject(reqStr, UserVo.class)));
	}

	@PostMapping("add")
	public Object add(@RequestBody @DecodeReq String reqStr) {

		return RespUtil.baseResp(userService.add(JSONObject.parseObject(reqStr, UserVo.class)));
	}

	@PostMapping("update")
	public Object update(@RequestBody @DecodeReq String reqStr) {
		return RespUtil.baseResp(userService.update(JSONObject.parseObject(reqStr, UserVo.class)));
	}

}
