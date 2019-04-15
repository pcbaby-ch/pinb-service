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
@Api(tags = "用户相关api", description = "用户相关api")
@RestController
@RequestMapping("user")
public class UserControl {

	@Autowired
	private UserService userService;

	@ApiOperation("查询用户集list")
	@ApiImplicitParams({ @ApiImplicitParam(name = "isOpenGroub",value="是否开启拼吧店铺", required = true, dataType = "string") })
	@ApiResponses(value = { @ApiResponse(code = 0000, message = "操作成功"),
			@ApiResponse(code = 00001, message = "参数[%s]非法"), @ApiResponse(code = 00002, message = "必要参数[%s]残缺"),
			@ApiResponse(code = 00006, message = "外部服务请求异常,%s") })
	@PostMapping("select")
	public Object select(@RequestBody UserVo userVo) {
		return userService.select(userVo);
	}

	@RequestMapping("add")
	public Object add(@RequestBody UserVo userVo) {

		return userService.add(userVo);
	}

	@RequestMapping("update")
	public Object update(@RequestBody UserVo userVo) {
		return userService.update(userVo);
	}

	@RequestMapping("getOpenid")
	public UserVo getOpenid(@RequestBody UserVo userVo) {
		return (UserVo) userService.getOpenid(userVo);
	}

}
