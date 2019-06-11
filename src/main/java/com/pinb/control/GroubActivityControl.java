/**
 * 
 */
package com.pinb.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.pinb.config.annotation.DecodeReq;
import com.pinb.entity.GroubActivity;
import com.pinb.service.GroubActivityService;
import com.pinb.util.RespUtil;

import io.swagger.annotations.Api;

/**
 * 
 * @author chenzhao @date Apr 15, 2019
 *
 */
@Api(tags = "拼吧-活动相关api", description = "活动拼团商品-list查询、活动拼团商品-新增、活动拼团商品-更新")
@RestController
@RequestMapping("groubActivity")
public class GroubActivityControl {

	@Autowired
	private GroubActivityService groubActivityService;

	@PostMapping("selectNearGrouba")
	public Object selectNearGrouba(@RequestBody @DecodeReq String reqStr) {
		return RespUtil
				.listResp(groubActivityService.selectNearGrouba(JSONObject.parseObject(reqStr, GroubActivity.class)));
	}

	@PostMapping("share")
	public Object share(@RequestBody @DecodeReq String reqStr) {
		groubActivityService.share(JSONObject.parseObject(reqStr, GroubActivity.class).getGroubaTrace());
		return RespUtil.baseResp(true);
	}

}
