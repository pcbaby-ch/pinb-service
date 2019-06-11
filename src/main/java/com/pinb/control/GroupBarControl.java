/**
 * 
 */
package com.pinb.control;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.pinb.config.annotation.DecodeReq;
import com.pinb.entity.GroupBar;
import com.pinb.service.GroupBarService;
import com.pinb.util.RespUtil;

import io.swagger.annotations.Api;

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

	@PostMapping("select")
	public Object select(@RequestBody @DecodeReq String reqStr) {
		return RespUtil.listResp(groupBarService.select(JSONObject.parseObject(reqStr, GroupBar.class)));
	}

	@PostMapping("selectOne")
	public Object selectOne(@RequestBody @DecodeReq String reqStr) {
		return RespUtil.dataResp(groupBarService.selectOne(JSONObject.parseObject(reqStr, GroupBar.class)));
	}

	@PostMapping("selectOneShare")
	public Object selectOneShare(@RequestBody @DecodeReq String reqStr) {
		return RespUtil.dataResp(groupBarService.getOneShopShare(JSONObject.parseObject(reqStr, GroupBar.class)));
	}

	@PostMapping("add")
	public Object add(@RequestBody @DecodeReq String reqStr, HttpServletRequest request) {

		return RespUtil.dataResp(groupBarService.add(reqStr, request));
	}

	@PostMapping("update")
	public Object update(@RequestBody @DecodeReq String reqStr) {
		return RespUtil.baseResp(groupBarService.update(JSONObject.parseObject(reqStr, GroupBar.class)));
	}

	@PostMapping("getShopQR")
	public Object getShopQR(@RequestBody @DecodeReq String reqStr) throws IOException {
		return RespUtil.dataResp(groupBarService.getShopQR(JSONObject.parseObject(reqStr, GroupBar.class)));
	}

}
