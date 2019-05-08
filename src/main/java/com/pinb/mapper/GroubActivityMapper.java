/**
 * 
 */
package com.pinb.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.pinb.entity.GroubActivity;

/**
 * 拼吧活动CRUD
 * 
 * @author chenzhao @date Apr 9, 2019
 */
@Repository
@Mapper
public interface GroubActivityMapper {

	@Select(value = "<script>select grouba_trace,ref_groub_trace,ref_user_wx_unionid,grouba_size,grouba_max_count,goods_name,goods_img,goods_price,grouba_discount_amount,grouba_isnew,grouba_expired_time,grouba_active_minute"
			+ " from groub_activity" + "<where>" + "<if test=\"refGroubTrace != null and refGroubTrace != '' \">"
			+ " and ref_groub_trace = #{refGroubTrace}" + "</if>"
			+ "<if test=\"refUserWxUnionid != null and refUserWxUnionid != '' \">"
			+ " and ref_user_wx_unionid = #{refUserWxUnionid}" + "</if>" + "</where></script>")
	public List<GroubActivity> select(@Param(value = "refGroubTrace") String refGroubTrace,
			@Param(value = "refUserWxUnionid") String refUserWxUnionid);

	@Select(value = "select grouba_trace,ref_groub_trace,ref_user_wx_unionid,grouba_size,grouba_max_count,goods_name,goods_img,goods_price,grouba_discount_amount,grouba_isnew,grouba_expired_time,grouba_active_minute"
			+ " from groub_activity" + " where grouba_trace = #{groubaTrace}")
	public GroubActivity selectOne(@Param(value = "groubaTrace") String groubaTrace);

	@Insert(value = "INSERT INTO groub_activity"
			+ " (grouba_trace,ref_groub_trace,ref_user_wx_unionid,grouba_size,grouba_max_count,goods_name,goods_img,goods_price,grouba_discount_amount,grouba_isnew,grouba_active_minute,latitude,longitude) "
			+ " VALUES (#{groubaTrace},#{refGroubTrace},#{refUserWxUnionid},#{groubaSize},#{groubaMaxCount},#{goodsName},#{goodsImg},#{goodsPrice},#{groubaDiscountAmount},#{groubaIsnew},#{groubaActiveMinute},#{latitude},#{longitude})")
	public int insert(GroubActivity groubActivity);

	@Update(value = "<script>UPDATE groub_activity SET  uptime=NOW()"
			+ "<if test=\"goodsName != null and goodsName != '' \">" + ",goods_name = #{goodsName}" + "</if>"
			+ "<if test=\"goodsImg != null and goodsImg != '' \">" + ",goods_img = #{goodsImg}" + "</if>"
			+ "<if test=\"goodsPrice != null and goodsPrice != '' \">" + ",goods_price = #{goodsPrice}" + "</if>"
			+ "<if test=\"groubaDiscountAmount != null and groubaDiscountAmount != '' \">"
			+ " and grouba_discount_amount = #{groubaDiscountAmount}" + "</if>"
			+ "<if test=\"groubaIsnew != null and groubaIsnew != '' \">" + ",grouba_isnew = #{groubaIsnew}" + "</if>"
			+ "<if test=\"groubaExpiredTime != null and groubaExpiredTime != '' \">"
			+ ",grouba_expired_time = #{groubaExpiredTime}" + "</if>"
			+ "<if test=\"groubaActiveMinute != null and groubaActiveMinute != '' \">"
			+ ",grouba_active_minute = #{groubaActiveMinute}" + "</if>" + " where grouba_trace=#{groubaTrace}</script>")
	public int update(GroubActivity groubActivity);

	@Delete(value = "<script>delete from groub_activity" + "<where>"
			+ "<if test=\"refGroubTrace != null and refGroubTrace != '' \">" + " and ref_groub_trace = #{refGroubTrace}"
			+ "</if>" + "<if test=\"refUserWxUnionid != null and refUserWxUnionid != '' \">"
			+ " and ref_user_wx_unionid = #{refUserWxUnionid}" + "</if>" + "</where></script>")
	public int delete(@Param(value = "refGroubTrace") String refGroubTrace,
			@Param(value = "refUserWxUnionid") String refUserWxUnionid);

	@Select(value = "<script>select grouba_trace,ref_groub_trace,ref_user_wx_unionid,grouba_size,grouba_max_count,goods_name,goods_img,goods_price,grouba_discount_amount,grouba_isnew,grouba_expired_time,grouba_active_minute"
			+ " from groub_activity" + "<where>" 
			+ "<if test=\"maxLat != null and maxLat != '' \">" + " and latitude between #{minLat} and #{maxLat}" + "</if>" 
			+ "<if test=\"maxLng != null and maxLng != '' \">" + " and longitude between #{minLng} and #{maxLng}" + "</if>" + "</where></script>")
	public GroubActivity selectNearGrouba(@Param(value = "maxLat") double maxLat,
			@Param(value = "minLat") double minLat, @Param(value = "maxLng") double maxLng,
			@Param(value = "minLng") double minLng);

}
