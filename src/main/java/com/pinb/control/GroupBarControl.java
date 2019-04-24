/**
 * 
 */
package com.pinb.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pinb.entity.GroupBar;
import com.pinb.service.GroupBarService;
import com.pinb.util.RespUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author chenzhao @date Apr 15, 2019
 *
 */
@Api(tags = "拼吧-店铺相关api", description = "店铺信息-查询、店铺入驻-新增、店铺信息-更新")
@RestController
@RequestMapping("groupBar")
public class GroupBarControl {

	@Autowired
	private GroupBarService groupBarService;

	@ApiOperation("暂，无需对接")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "refUserWxUnionid", value = "店铺所属用户（店长）", required = false, dataType = "string") })
	@PostMapping("select")
	public Object select(@RequestBody GroupBar groupBar) {
		return RespUtil.dataResp(groupBarService.select(groupBar));
	}

	@ApiOperation("店铺信息-查询")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "YrefUserWxUnionid", value = "店铺所属用户", required = false, dataType = "string"), })
	@PostMapping("selectOne")
	public Object selectOne(@RequestBody GroupBar groupBar) {
		return RespUtil.dataResp(groupBarService.selectOne(groupBar));
	}

	@ApiOperation("店铺入驻-新增")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "YrefUserWxUnionid", value = "店铺所属用户", required = false, dataType = "string"),
			@ApiImplicitParam(name = "YgroubName", value = "店铺名称，店铺图片(多张图片，逗号分隔)", required = false, dataType = "string"),
			@ApiImplicitParam(name = "YgroubImg", value = "店铺图片(多张图片，逗号分隔)", required = false, dataType = "string"),
			@ApiImplicitParam(name = "YgroubPhone", value = "店铺客服电话", required = false, dataType = "string"),
			@ApiImplicitParam(name = "YgroubAddress", value = "店铺地址", required = false, dataType = "string") })
	@PostMapping("add")
	public Object add(@RequestBody GroupBar groupBar) {

		return RespUtil.baseResp(groupBarService.add(groupBar));
	}

	@ApiOperation("店铺信息-更新")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "YrefUserWxUnionid", value = "店铺所属用户", required = false, dataType = "string"),
			@ApiImplicitParam(name = "groubName", value = "以下参数中，用户没改的，不要传或者传null对象，EX:{'groubAddress':NULL}", required = false, dataType = "string"),
			@ApiImplicitParam(name = "groubImg", value = "", required = false, dataType = "string"),
			@ApiImplicitParam(name = "groubPhone", value = "", required = false, dataType = "string"),
			@ApiImplicitParam(name = "groubAddress", value = "", required = false, dataType = "string") })
	@PostMapping("update")
	public Object update(@RequestBody GroupBar groupBar) {
		return RespUtil.baseResp(groupBarService.update(groupBar));
	}

}
