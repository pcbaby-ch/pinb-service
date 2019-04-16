/**
 * 
 */
package com.pinb.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pinb.entity.GroubaOrder;
import com.pinb.service.GroubaOrderService;

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

	@ApiOperation("订单list数据集查询 {店铺所有成团订单、vip店长统计报表功能（TODO）}")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "refUserWxUnionid", value = "店铺所属用户", required = true, dataType = "string"),
			@ApiImplicitParam(name = "refroubTrace、refGroubaTrace、orderStatus", value = "归属店铺，以下字段至少传一个或多个", required = false, dataType = "string"),
			@ApiImplicitParam(name = "refGroubaTrace、orderStatus", value = "归属活动", required = false, dataType = "string"),
			@ApiImplicitParam(name = "orderStatus", value = "订单状态", required = false, dataType = "string"), })
	@PostMapping("select")
	public Object select(@RequestBody GroubaOrder groubaOrder) {
		return groubaOrderService.select(groubaOrder);
	}

	@ApiOperation("开团下单")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "refGroubTrace,", value = "归属店铺", required = true, dataType = "string"),
			@ApiImplicitParam(name = "refGroubaTrace", value = "拼团订单到期时间", required = true, dataType = "string"),
			@ApiImplicitParam(name = "orderExpiredTime", value = "拼团订单到期时间", required = true, dataType = "string"),
			@ApiImplicitParam(name = "refUserWxUnionid", value = "订单参团用户", required = true, dataType = "string"),
			@ApiImplicitParam(name = "refUserImg", value = "用户头像fileid", required = true, dataType = "string"), })
	@PostMapping("orderOpen")
	public Object orderOpen(@RequestBody GroubaOrder groubaOrder) {

		return groubaOrderService.orderOpen(groubaOrder);
	}

	@ApiOperation("已有团订单-分享 {前端分享成功后，累计分享计数}")
	@ApiImplicitParams({ @ApiImplicitParam(name = "orderTrace", value = "被分享团订单", required = true, dataType = "string"),
			@ApiImplicitParam(name = "refUserWxUnionid", value = "分享发起用户", required = true, dataType = "string") })
	@PostMapping("orderShare")
	public Object orderShare(@RequestBody GroubaOrder groubaOrder) {

		return groubaOrderService.orderShare(groubaOrder);
	}

	@ApiOperation("已有团订单-参团")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "orderTrace", value = "参团订单trace", required = true, dataType = "string"),
			@ApiImplicitParam(name = "refUserWxUnionid", value = "订单参团用户", required = true, dataType = "string"),
			@ApiImplicitParam(name = "refUserImg", value = "用户头像fileid", required = true, dataType = "string"), })
	@PostMapping("orderJoin")
	public Object orderJoin(@RequestBody GroubaOrder groubaOrder) {

		return groubaOrderService.orderJoin(groubaOrder);
	}

	@ApiOperation("已有团订单-扫码消费 {二维码设计成包含订单trace+消费用户数据的QR，店长扫QR时，便可从二维码中同时获得消费订单+用户}")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "orderTrace", value = "消费订单trace", required = true, dataType = "string"),
			@ApiImplicitParam(name = "refUserWxUnionid", value = "消费用户", required = true, dataType = "string"),
			@ApiImplicitParam(name = "refGroubTrace", value = "扫码店长店铺trace", required = true, dataType = "string"), })
	@PostMapping("orderConsume")
	public Object orderConsume(@RequestBody GroubaOrder groubaOrder) {

		return groubaOrderService.orderConsume(groubaOrder);
	}

}
