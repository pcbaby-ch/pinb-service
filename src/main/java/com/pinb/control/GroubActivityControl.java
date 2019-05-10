/**
 * 
 */
package com.pinb.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pinb.entity.GroubActivity;
import com.pinb.service.GroubActivityService;
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
@Api(tags = "拼吧-活动相关api", description = "活动拼团商品-list查询、活动拼团商品-新增、活动拼团商品-更新")
@RestController
@RequestMapping("groubActivity")
public class GroubActivityControl {

	@Autowired
	private GroubActivityService groubActivityService;

	@ApiOperation("活动拼团商品-list查询 {店铺所有商品}")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "refGroubTrace", value = "归属店铺", required = true, dataType = "string") })
	@PostMapping("select")
	public Object select(@RequestBody GroubActivity groubActivity) {
		return RespUtil.listResp(
				groubActivityService.select(groubActivity.getRefGroubTrace(), groubActivity.getRefUserWxUnionid()));
	}

	@ApiOperation("活动拼团商品-新增")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "YrefGroubTrace", value = "归属店铺", required = false, dataType = "string"),
			@ApiImplicitParam(name = "YrefUserWxUnionid", value = "归属店铺用户", required = false, dataType = "string"),
			@ApiImplicitParam(name = "YgroubaSize", value = "活动单团规模", required = false, dataType = "string"),
			@ApiImplicitParam(name = "YgroubaMaxCount", value = "活动成团个数上限", required = false, dataType = "string"),
			@ApiImplicitParam(name = "YgoodsName", value = "活动商品名称", required = false, dataType = "string"),
			@ApiImplicitParam(name = "YgoodsImg", value = "商品图片fileid", required = false, dataType = "string"),
			@ApiImplicitParam(name = "YgoodsPrice", value = "归属店铺，商品原价", required = false, dataType = "string"),
			@ApiImplicitParam(name = "YgroubaDiscountAmount", value = "商品折扣金额", required = false, dataType = "string"),
			@ApiImplicitParam(name = "YgroubaIsnew", value = "是否拉新团", required = false, dataType = "string"),
			@ApiImplicitParam(name = "groubaExpiredTime", value = "活动到期时间 {到期后，不能再开团、参团、分享}", required = false, dataType = "string") })
	@PostMapping("add")
	public Object add(@RequestBody GroubActivity groubActivity) {

		return RespUtil.baseResp(groubActivityService.add(groubActivity));
	}

	@ApiOperation("活动拼团商品-更新")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "YrefGroubTrace", value = "归属店铺", required = false, dataType = "string"),
			@ApiImplicitParam(name = "groubaSize", value = "以下参数中，用户没改的，不要传或者传null对象，EX:{'groubAddress':NULL}", required = false, dataType = "string"),
			@ApiImplicitParam(name = "groubaMaxCount,", value = "", required = false, dataType = "string"),
			@ApiImplicitParam(name = "goodsName", value = "", required = false, dataType = "string"),
			@ApiImplicitParam(name = "goodsImg", value = "", required = false, dataType = "string"),
			@ApiImplicitParam(name = "goodsPrice", value = "", required = false, dataType = "string"),
			@ApiImplicitParam(name = "groubaDiscountAmount", value = "", required = false, dataType = "string"),
			@ApiImplicitParam(name = "groubaIsnew", value = "", required = false, dataType = "string"), })
	@PostMapping("update")
	public Object update(@RequestBody GroubActivity groubActivity) {
		return RespUtil.baseResp(groubActivityService.update(groubActivity));
	}

	@PostMapping("selectNearGrouba")
	public Object selectNearGrouba(@RequestBody GroubActivity groubActivity) {
		return RespUtil.listResp(groubActivityService.selectNearGrouba(groubActivity));
	}

}
