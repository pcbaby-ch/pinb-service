/**
 * 
 */
package com.pinb.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.pinb.entity.GroubaOrder;

/**
 * 拼吧订单CRUD
 * 
 * @author chenzhao @date Apr 9, 2019
 */
@Repository
@Mapper
public interface GroubaOrderMapper {

	@Select(value = "<script>select order_trace,ref_groub_trace,ref_grouba_trace,order_expired_time,order_status,ref_user_wx_unionid,ref_user_img,order_succeed_time,order_share_count"
			+ " from grouba_order" + "<where>" + "<if test=\"orderTrace != null and orderTrace != '' \">"
			+ " and order_trace = #{orderTrace}" + "</if>"
			+ "<if test=\"refGroubTrace != null and refGroubTrace != '' \">" + " and ref_groub_trace = #{refGroubTrace}"
			+ "</if>" + "<if test=\"refGroubaTrace != null and refGroubaTrace != '' \">"
			+ " and ref_grouba_trace = #{refGroubaTrace}" + "</if>"
			+ "<if test=\"orderStatus != null and orderStatus != '' \">" + " and order_status = #{orderStatus}"
			+ "</if>" + "<if test=\"refUserWxUnionid != null and refUserWxUnionid != '' \">"
			+ " and ref_user_wx_unionid = #{refUserWxUnionid}" + "</if>" + "</where></script>")
	public List<GroubaOrder> select(@Param(value = "orderTrace") String orderTrace,
			@Param(value = "refGroubTrace") String refGroubTrace,
			@Param(value = "refGroubaTrace") String refGroubaTrace, @Param(value = "orderStatus") String orderStatus,
			@Param(value = "refUserWxUnionid") String refUserWxUnionid);

	@Select(value = "<script>select order_trace,ref_groub_trace,ref_grouba_trace,order_expired_time,order_status,ref_user_wx_unionid,ref_user_img,order_succeed_time,order_share_count"
			+ " from grouba_order"
			+ " where order_trace = #{orderTrace} and ref_user_wx_unionid=#{refUserWxUnionid}</script>")
	public GroubaOrder selectOne(@Param(value = "orderTrace") String orderTrace,
			@Param(value = "refUserWxUnionid") String refUserWxUnionid);

	@Insert(value = "<script>INSERT INTO grouba_order"
			+ " (order_trace,ref_groub_trace,ref_grouba_trace,order_expired_time,ref_user_wx_unionid,ref_user_img,user_ip) "
			+ " VALUES (#{orderTrace},#{refGroubTrace},#{refGroubaTrace},#{orderExpiredTime},#{refUserWxUnionid},#{refUserImg},#{userIp})</script>")
	public int insert(GroubaOrder groubaOrder);

	@Update(value = "<script>UPDATE grouba_order SET  uptime=NOW()"
			+ "<if test=\"orderStatus != null and orderStatus != '' \">" + ",order_status = #{orderStatus}" + "</if>"
			+ "<if test=\"orderShareCount != null and orderShareCount != '' \">"
			+ ",order_share_count = order_share_count+1" + "</if>"
			+ "<if test=\"joinTime != null and joinTime != '' \">" + ",join_time = NOW()" + "</if>"
			+ "<if test=\"joinSucceedTime != null and joinSucceedTime != '' \">" + ",join_succeed_time = NOW()"
			+ "</if>" + "<if test=\"consumeTime != null and consumeTime != '' \">" + ",consume_time = NOW()" + "</if>"
			+ "<if test=\"consumeSuccessTime != null and consumeSuccessTime != '' \">" + ",consume_success_time = NOW()"
			+ "</if>" + " where order_trace=#{orderTrace}</script>")
	public int update(GroubaOrder groubaOrder);

}
