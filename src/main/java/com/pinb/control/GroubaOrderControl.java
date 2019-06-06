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

import com.pinb.entity.GroubaOrder;
import com.pinb.service.GroubaOrderService;
import com.pinb.util.IpUtils;
import com.pinb.util.RespUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

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
	public Object selectMyOrder4user(@RequestBody GroubaOrder groubaOrder) {
		return RespUtil.dataResp(groubaOrderService.getMyOrder4user(groubaOrder));
	}

	@PostMapping("selectMyOrder4Shop")
	public Object selectMyOrder4Shop(@RequestBody GroubaOrder groubaOrder) {
		return RespUtil.dataResp(groubaOrderService.getMyOrder4Shop(groubaOrder));
	}

	@ApiOperation("开团下单")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "YrefGroubTrace,", value = "归属店铺", required = false, dataType = "string"),
			@ApiImplicitParam(name = "YrefGroubaTrace", value = "拼团订单到期时间", required = false, dataType = "string"),
			@ApiImplicitParam(name = "YorderExpiredTime", value = "拼团订单到期时间", required = false, dataType = "string"),
			@ApiImplicitParam(name = "YrefUserWxUnionid", value = "订单参团用户", required = false, dataType = "string"),
			@ApiImplicitParam(name = "YrefUserImg", value = "用户头像fileid", required = false, dataType = "string"), })
	@PostMapping("orderOpen")
	public Object orderOpen(@RequestBody GroubaOrder groubaOrder, HttpServletRequest req) throws Exception {
		groubaOrder.setClientIp(IpUtils.getIpFromRequest(req));
		return RespUtil.baseResp(groubaOrderService.orderOpen(groubaOrder));
	}

	@ApiOperation("已有团订单-参团")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "YorderTrace", value = "参团订单trace", required = false, dataType = "string"),
			@ApiImplicitParam(name = "YrefUserWxUnionid", value = "订单参团用户", required = false, dataType = "string"),
			@ApiImplicitParam(name = "YrefUserImg", value = "用户头像fileid", required = false, dataType = "string"), })
	@PostMapping("orderJoin")
	public Object orderJoin(@RequestBody GroubaOrder groubaOrder, HttpServletRequest req) throws Exception {
		groubaOrder.setClientIp(IpUtils.getIpFromRequest(req));
		return RespUtil.baseResp(groubaOrderService.orderJoin(groubaOrder));
	}

	@ApiOperation("已有团订单-扫码消费 {二维码设计成包含订单trace+消费用户数据的QR，店长扫QR时，便可从二维码中同时获得消费订单+用户}")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "YorderTrace", value = "消费订单trace", required = false, dataType = "string"),
			@ApiImplicitParam(name = "YrefUserWxUnionid", value = "消费用户", required = false, dataType = "string"),
			@ApiImplicitParam(name = "YrefGroubTrace", value = "扫码店长店铺trace", required = false, dataType = "string"), })
	@PostMapping("orderConsume")
	public Object orderConsume(@RequestBody GroubaOrder groubaOrder) {

		return RespUtil.baseResp(groubaOrderService.orderConsume(groubaOrder));
	}

}
