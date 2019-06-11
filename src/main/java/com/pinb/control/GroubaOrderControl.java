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
import com.pinb.entity.GroubaOrder;
import com.pinb.service.GroubaOrderService;
import com.pinb.util.IpUtils;
import com.pinb.util.RespUtil;

import io.swagger.annotations.Api;

/**
 * @author chenzhao @date Apr 10, 2019
 */
@Api(tags = "拼吧-订单相关api", description = "订单list数据集查询、开团下单、已有团订单-分享、已有团订单-参团、已有团订单-扫码消费")
@RestController
@RequestMapping("groubaOrder")
public class GroubaOrderControl {

	@Autowired
	private GroubaOrderService groubaOrderService;

	@PostMapping("selectMyOrder4user")
	public Object selectMyOrder4user(@RequestBody @DecodeReq String reqStr) {
		return RespUtil.dataResp(groubaOrderService.getMyOrder4user(JSONObject.parseObject(reqStr, GroubaOrder.class)));
	}

	@PostMapping("selectMyOrder4Shop")
	public Object selectMyOrder4Shop(@RequestBody @DecodeReq String reqStr) {
		return RespUtil.dataResp(groubaOrderService.getMyOrder4Shop(JSONObject.parseObject(reqStr, GroubaOrder.class)));
	}

	@PostMapping("orderOpen")
	public Object orderOpen(@RequestBody @DecodeReq String reqStr, HttpServletRequest req) throws Exception {
		GroubaOrder groubaOrder = JSONObject.parseObject(reqStr, GroubaOrder.class);
		groubaOrder.setClientIp(IpUtils.getIpFromRequest(req));
		return RespUtil.baseResp(groubaOrderService.orderOpen(groubaOrder));
	}

	@PostMapping("orderJoin")
	public Object orderJoin(@RequestBody @DecodeReq String reqStr, HttpServletRequest req) throws Exception {
		GroubaOrder groubaOrder = JSONObject.parseObject(reqStr, GroubaOrder.class);
		groubaOrder.setClientIp(IpUtils.getIpFromRequest(req));
		return RespUtil.baseResp(groubaOrderService.orderJoin(groubaOrder));
	}

	@PostMapping("orderConsume")
	public Object orderConsume(@RequestBody @DecodeReq String reqStr) {

		return RespUtil.baseResp(groubaOrderService.orderConsume(JSONObject.parseObject(reqStr, GroubaOrder.class)));
	}

	@PostMapping("orderConsumePrepare")
	public Object orderConsumePrepare(@RequestBody @DecodeReq String reqStr) {

		return RespUtil
				.baseResp(groubaOrderService.orderConsumePrepare(JSONObject.parseObject(reqStr, GroubaOrder.class)));
	}

}
