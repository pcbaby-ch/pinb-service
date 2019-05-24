/**
 * 
 */
package com.pinb.mapper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.pinb.common.ServiceException;
import com.pinb.config.RedisPool;
import com.pinb.constant.RedisConst;
import com.pinb.entity.GroubActivity;
import com.pinb.enums.RespCode;
import com.pinb.util.PropertiesUtils;

/**
 * #DB数据redis缓存管理，缓存2类分级结构:1、city>province 2、refGroubTrace #缓存方案：先删除缓存，再更新DB
 * 
 * @author chenzhao @date May 24, 2019
 */
@Service
public class GroubActivityCache {
	private static final Logger log = LoggerFactory.getLogger(GroubActivityCache.class);

	private boolean openCache = Boolean.parseBoolean(PropertiesUtils.getProperty("openCache", "false"));

	@Autowired
	private GroubActivityMapper groubActivityMapper;

	public List<GroubActivity> selectOneGroub(String refGroubTrace, String refUserWxUnionid) {
		if (!openCache) {
			return groubActivityMapper.selectOneGroub(refGroubTrace, refUserWxUnionid);
		}
		// #入参校验
		if (StringUtils.isEmpty(refGroubTrace)) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "refGroubTrace");
		}
		List<GroubActivity> result = groubActivityMapper.selectOneGroub(refGroubTrace, refUserWxUnionid);
		String key = RedisConst.GroubActivityCache + refGroubTrace;
		if (RedisPool.exists(key)) {
			log.debug("#命中缓存,#refGroubTrace:[{}]", refGroubTrace);
			return JSONObject.parseArray(RedisPool.get(key), GroubActivity.class);
		} else {
			log.debug("#无缓存,#refGroubTrace:[{}]", refGroubTrace);
			RedisPool.set(key, result);
			return result;
		}
	}

	public GroubActivity selectOne(String groubaTrace) {
		log.debug("#不缓存,#groubaTrace:[{}]", groubaTrace);
		return groubActivityMapper.selectOne(groubaTrace);// 不缓存，直接取DB
	}

	public int insert(GroubActivity groubActivity) {
		if (!openCache) {
			return groubActivityMapper.insert(groubActivity);
		}
		delCache(groubActivity.getCity(), groubActivity.getRefGroubTrace());
		return groubActivityMapper.insert(groubActivity);
	}

	@Deprecated
	public int update(GroubActivity groubActivity) {
		if (!openCache) {
			return groubActivityMapper.update(groubActivity);
		}
		delCache(groubActivity.getCity(), groubActivity.getRefGroubTrace());
		return groubActivityMapper.update(groubActivity);
	}

	public int share(String groubaTrace) {
		log.debug("#不缓存,#groubaTrace:[{}]", groubaTrace);
		return groubActivityMapper.share(groubaTrace);// 分享数，不更新维护缓存
	}

	public int delete(String city, String refGroubTrace, String refUserWxUnionid) {
		if (!openCache) {
			return groubActivityMapper.delete(refGroubTrace, refUserWxUnionid);
		}
		delCache(city, refGroubTrace);
		log.debug("#缓存删除end-更新DB开始，#refGroubTrace:[{}]", refGroubTrace);
		return groubActivityMapper.delete(refGroubTrace, refUserWxUnionid);
	}

	public List<GroubActivity> selectNearGrouba(String province, String city, double minLat, double maxLat,
			double minLng, double maxLng, int page) {
		if (!openCache) {
			return groubActivityMapper.selectNearGrouba(province, city, minLat, maxLat, minLng, maxLng);
		}
		province = province == null ? "" : province;
		city = city == null ? "" : city;
		List<GroubActivity> result = groubActivityMapper.selectNearGrouba(province, city, minLat, maxLat, minLng,
				maxLng);
		String key = RedisConst.GroubActivityCache + city;
		String field = province + "_" + minLat + "_" + maxLat + "_" + minLng + "_" + maxLng + "_" + page;
		if (RedisPool.exists(key) && RedisPool.exists(key, field)) {
			log.debug("#命中缓存,#city:[{}],#[{}]", city, province);
			return JSONObject.parseArray(RedisPool.hget(key, field), GroubActivity.class);
		} else {
			log.debug("#无缓存,#city:[{}],#[{}]", city, province);
			RedisPool.hset(key, field, result, 60000);
			return result;
		}
	};

	public List<GroubActivity> selectNearGroubaTop100(String province, String city, int page) {
		if (!openCache) {
			return groubActivityMapper.selectNearGroubaTop100(province, city);
		}
		province = province == null ? "" : province;
		city = city == null ? "" : city;
		List<GroubActivity> result = groubActivityMapper.selectNearGroubaTop100(province, city);
		String key = RedisConst.GroubActivityCache + city;
		String field = province + "_" + page;
		if (RedisPool.exists(key) && RedisPool.exists(key, field)) {
			log.debug("#命中缓存,#city:[{}],#[{}]", city, province);
			return JSONObject.parseArray(RedisPool.hget(key, field), GroubActivity.class);
		} else {
			log.debug("#无缓存,#city:[{}],#[{}]", city, province);
			RedisPool.hset(key, field, result, 60000);
			return result;
		}
	};

	private void delCache(String city, String refGroubTrace) {
		if (StringUtils.isEmpty(city)) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "City");
		}
		if (StringUtils.isEmpty(refGroubTrace)) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "RefGroubTrace");
		}
		String keyGroub = RedisConst.GroubActivityCache + refGroubTrace;
		String keyCity = RedisConst.GroubActivityCache + city;
		RedisPool.del(keyGroub);// 先删除缓存，再变更DB
		RedisPool.delKeys(keyCity);// 先删除缓存，再变更DB
		log.debug("#缓存删除end-更新DB开始，#keyGroub:[{}],#keyCity:[{}]", keyGroub, keyCity);
	}

	public static void main(String[] args) {
		RedisPool.del("");
	}

}
