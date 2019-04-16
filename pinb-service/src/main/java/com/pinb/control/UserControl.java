/**
 * 
 */
package com.pinb.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pinb.service.UserService;
import com.pinb.util.RespUtil;
import com.pinb.vo.UserVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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

	@ApiOperation("公共请求码&响应码-响应报文格式规约:{'retCode':10000,'retMsg':'操作成功','data':{'userName':'用户姓名','userPhone':'18516369668'}}")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userWxUnionid,userWxOpenid,page,rows", value = "公共请求参数,其中page,rows分页数据集才需要传", required = true, dataType = "string") })
	@ApiResponses(value = { @ApiResponse(code = 10000, message = "操作成功"),
			@ApiResponse(code = 10001, message = "参数[%s]非法"), @ApiResponse(code = 10002, message = "必要参数[%s]残缺"),
			@ApiResponse(code = 10006, message = "外部服务请求异常,%s"), @ApiResponse(code = 44444, message = "服务繁忙") })
	@PostMapping("select")
	public Object select(@RequestBody UserVo userVo) {
		return RespUtil.dataResp(userService.select(userVo));
	}

	@ApiOperation("获取微信openid、unionid")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "appid", value = "详情查看微信开发平台api：https://developers.weixin.qq.com/miniprogram/dev/api/getPhoneNumber.html >开放接口>登陆", required = false, dataType = "string"),
			@ApiImplicitParam(name = "secret", value = "", required = false, dataType = "string"),
			@ApiImplicitParam(name = "jsCode", value = "", required = false, dataType = "string"),
			@ApiImplicitParam(name = "grantType", value = "", required = false, dataType = "string") })
	@PostMapping("getOpenid")
	public Object getOpenid(@RequestBody UserVo userVo) {
		return RespUtil.dataResp(userService.getOpenid(userVo));
	}

	@ApiOperation("新用户首次授权后-注册用户")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userPhone", value = "用户手机号", required = true, dataType = "string"),
			@ApiImplicitParam(name = "userImg", value = "头像fileid", required = true, dataType = "string"),
			@ApiImplicitParam(name = "isOpenGroub", value = "是否店长（默认1）", required = true, dataType = "string"),
			@ApiImplicitParam(name = "userBrand", value = "以下时用户微信资料画像字段，详情查看微信开发平台api：https://developers.weixin.qq.com/miniprogram/dev/api/getPhoneNumber.html", required = false, dataType = "string"),
			@ApiImplicitParam(name = "userModel,", value = "用户微信资料画像字段", required = false, dataType = "string"),
			@ApiImplicitParam(name = "usersystem", value = "用户微信资料画像字段", required = false, dataType = "string"),
			@ApiImplicitParam(name = "userPlatform", value = "用户微信资料画像字段", required = false, dataType = "string"),
			@ApiImplicitParam(name = "userBenchmark", value = "用户微信资料画像字段", required = false, dataType = "string"),
			@ApiImplicitParam(name = "userNickname", value = "用户微信资料画像字段", required = false, dataType = "string"),
			@ApiImplicitParam(name = "userCity", value = "用户微信资料画像字段", required = false, dataType = "string"),
			@ApiImplicitParam(name = "userProvince", value = "用户微信资料画像字段", required = false, dataType = "string"),
			@ApiImplicitParam(name = "userWxinfoMd5", value = "用户微信资料画像MD5(userBrand+userModel+usersystem+userPlatform+userBenchmark+userNickname+userCity+userProvince)（用于前端比对，当用户微信资料画像局部变更，则调[用户信息更新]api，刷新用户微信画像）", required = false, dataType = "string") })
	@PostMapping("add")
	public Object add(@RequestBody UserVo userVo) {

		return RespUtil.baseResp(userService.add(userVo));
	}

	@ApiOperation("用户信息更新")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userWxUnionid", value = "用户微信unionid（用户id，微信无返回unionid时，unionid等同于openid）", required = true, dataType = "string"),
			@ApiImplicitParam(name = "userPhone", value = "用户手机号", required = false, dataType = "string"),
			@ApiImplicitParam(name = "userImg", value = "头像fileid", required = false, dataType = "string"),
			@ApiImplicitParam(name = "isOpenGroub", value = "是否店长（默认1）", required = false, dataType = "string"),
			@ApiImplicitParam(name = "userBrand", value = "以下时用户微信资料画像字段，详情查看微信开发平台api：https://developers.weixin.qq.com/miniprogram/dev/api/getPhoneNumber.html", required = false, dataType = "string"),
			@ApiImplicitParam(name = "userModel,", value = "用户微信资料画像字段", required = false, dataType = "string"),
			@ApiImplicitParam(name = "usersystem", value = "用户微信资料画像字段", required = false, dataType = "string"),
			@ApiImplicitParam(name = "userPlatform", value = "用户微信资料画像字段", required = false, dataType = "string"),
			@ApiImplicitParam(name = "userBenchmark", value = "用户微信资料画像字段", required = false, dataType = "string"),
			@ApiImplicitParam(name = "userNickname", value = "用户微信资料画像字段", required = false, dataType = "string"),
			@ApiImplicitParam(name = "userCity", value = "用户微信资料画像字段", required = false, dataType = "string"),
			@ApiImplicitParam(name = "userProvince", value = "用户微信资料画像字段", required = false, dataType = "string"),
			@ApiImplicitParam(name = "userWxinfoMd5", value = "用户微信资料画像MD5(userBrand+userModel+usersystem+userPlatform+userBenchmark+userNickname+userCity+userProvince)（用于前端比对，当用户微信资料画像局部变更，则调[用户信息更新]api，刷新用户微信画像）", required = false, dataType = "string") })
	@PostMapping("update")
	public Object update(@RequestBody UserVo userVo) {
		return RespUtil.baseResp(userService.update(userVo));
	}

}
