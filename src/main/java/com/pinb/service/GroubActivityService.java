/**
 * 
 */
package com.pinb.service;

import java.util.HashMap;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinb.common.BusinessesFlowNum;
import com.pinb.common.ServiceException;
import com.pinb.constant.RedisConst;
import com.pinb.entity.GroubActivity;
import com.pinb.enums.RespCode;
import com.pinb.mapper.GroubActivityCache;
import com.pinb.util.MapDistance;

/**
 * 拼吧-活动
 * 
 * @author chenzhao @date Apr 15, 2019
 */
@Service
public class GroubActivityService {

	private static final Logger log = LoggerFactory.getLogger(GroubActivityService.class);

	@Autowired
	GroubActivityCache groubActivityCache;

	public boolean add(GroubActivity groubActivity) {
		// #入参校验
		if (StringUtils.isEmpty(groubActivity.getRefGroubTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "RefGroubTrace");
		}
		if (StringUtils.isEmpty(groubActivity.getRefUserWxUnionid())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "RefUserWxUnionid");
		}
		if (StringUtils.isEmpty(groubActivity.getGroubaSize())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GroubaSize");
		}
		if (StringUtils.isEmpty(groubActivity.getGroubaMaxCount())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GroubaMaxCount");
		}
		if (StringUtils.isEmpty(groubActivity.getGoodsName())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GoodsName");
		}
		if (StringUtils.isEmpty(groubActivity.getGoodsImg())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GoodsImg");
		}
		if (StringUtils.isEmpty(groubActivity.getGoodsPrice())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GoodsPrice");
		}
		if (StringUtils.isEmpty(groubActivity.getGroubaDiscountAmount())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GroubaDiscountAmount");
		}
		if (StringUtils.isEmpty(groubActivity.getGroubaIsnew())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GroubaIsnew");
		}
		if (StringUtils.isEmpty(groubActivity.getProvince())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "Province");
		}
		if (StringUtils.isEmpty(groubActivity.getCity())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "City");
		}
		logParams(groubActivity);
		groubActivity.setGroubaTrace(BusinessesFlowNum.getNum("GA", RedisConst.groubActivityTrace));
		return groubActivityCache.insert(groubActivity) > 0;
	}

	/**
	 * 活动商品分享计数
	 * 
	 * @param groubActivity
	 * @return
	 */
	@Async("getThreadPoolTaskExecutor")
	public Future<String> share(String groubaTrace) {
		groubActivityCache.share(groubaTrace);
		log.debug("#分享累计成功");
		Future<String> future = new AsyncResult<>("successs");
		return future;
	}

	private void logParams(GroubActivity groubActivity) {
		log.debug("#入参校验通过:[{}]", JSONObject.toJSON(groubActivity));
	}

	/**
	 * 查询附近的活动商品
	 * 
	 * @author chenzhao @date May 8, 2019
	 * @param groubActivity
	 * @return
	 */
	public Page<?> selectNearGrouba(GroubActivity groubActivity) {
		// #入参校验
//		if (StringUtils.isEmpty(groubActivity.getProvince())) {
//			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "Province");
//		}
//		if (StringUtils.isEmpty(groubActivity.getCity())) {
//			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "City");
//		}
//		if (StringUtils.isEmpty(groubActivity.getLatitude())) {
//			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "Latitude");
//		}
//		if (StringUtils.isEmpty(groubActivity.getLongitude())) {
//			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "Longitude");
//		}
		logParams(groubActivity);
		Page<?> page = PageHelper.startPage(groubActivity.getPage(), groubActivity.getRows());
		// #用户未指定位置时，显示所在城市分享数最高的前100个商品
		if (1==1) {//前期合作商户少，统一显示热度top100
			groubActivityCache.selectNearGroubaTop100(groubActivity.getPage());
			return page;
		}

		double myLat = Double.parseDouble(groubActivity.getLatitude());
		double myLng = Double.parseDouble(groubActivity.getLongitude());
		double range = 180 / Math.PI * 1 / 6372.797; // 里面的 1 就代表搜索 1km 之内，单位km
		double lngR = range / Math.cos(myLat * Math.PI / 180.0);
		double maxLat = myLat + range;
		double minLat = myLat - range;
		double maxLng = myLng + lngR;
		double minLng = myLng - lngR;
		HashMap<String, Object> map = MapDistance.getAround(groubActivity.getLatitude(), groubActivity.getLongitude(),
				"1000");
		log.debug(
				"#计算附近的商品，#minLat:[{}],#maxLat[{}],#minLng[{}],#maxLng[{}]，util计算结果,#minLat:[{}],#maxLat[{}],#minLng[{}],#maxLng[{}]",
				minLat, maxLat, minLng, maxLng, map.get("minLat"), map.get("maxLat"), map.get("minLng"),
				map.get("maxLng"));
		groubActivityCache.selectNearGrouba(groubActivity.getProvince(), groubActivity.getCity(), minLat, maxLat,
				minLng, maxLng, groubActivity.getPage());
		return page;
	}

	/**
	 * 查询商品详情页
	 * 
	 * @author chenzhao @date Jun 28, 2019
	 * @param groubActivity
	 * @return
	 */
	public Object selectOne(GroubActivity groubActivity) {
		// #入参校验
		if (StringUtils.isEmpty(groubActivity.getGroubaTrace())) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "GroubaTrace");
		}
		logParams(groubActivity);
		groubActivity = groubActivityCache.selectOne(groubActivity.getGroubaTrace());
		if (groubActivity == null) {
			throw new ServiceException(RespCode.grouba_unExist);
		}
		return groubActivity;
	}

}
