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

	@Select(value = "<script>select order_trace,ref_groub_trace,ref_grouba_trace,order_expired_time,order_status,ref_user_wx_unionid,grouba_size,leader,ref_user_img,form_id,join_succeed_time,ref_user_wx_openid,goods_name,goods_img,goods_price,grouba_discount_amount,grouba_isnew"
			+ " from grouba_order" + "<where>" + "<if test=\"orderTrace != null and orderTrace != '' \">"
			+ " and order_trace = #{orderTrace}" + "</if>"
			+ "<if test=\"refGroubTrace != null and refGroubTrace != '' \">" + " and ref_groub_trace = #{refGroubTrace}"
			+ "</if>" + "<if test=\"refGroubaTrace != null and refGroubaTrace != '' \">"
			+ " and ref_grouba_trace = #{refGroubaTrace}" + "</if>"
			+ "<if test=\"orderStatus != null and orderStatus != '' \">" + " and order_status = #{orderStatus}"
			+ "</if>" + "<if test=\"refUserWxUnionid != null and refUserWxUnionid != '' \">"
			+ " and ref_user_wx_unionid = #{refUserWxUnionid}" + "</if>" + "</where> order by id desc</script>")
	public List<GroubaOrder> select(@Param(value = "orderTrace") String orderTrace,
			@Param(value = "refGroubTrace") String refGroubTrace,
			@Param(value = "refGroubaTrace") String refGroubaTrace, @Param(value = "orderStatus") String orderStatus,
			@Param(value = "refUserWxUnionid") String refUserWxUnionid);
	/**
	 * 查询我的所有订单
	 * @param refUserWxUnionid
	 * @return
	 */
	@Select(value = "<script>select order_trace,ref_groub_trace,ref_grouba_trace,order_expired_time,order_status,ref_user_wx_unionid,leader,ref_user_img,form_id,join_succeed_time,ref_user_wx_unionid,grouba_size,goods_name,goods_img,goods_price,grouba_discount_amount,grouba_isnew"
			+ " from grouba_order" + "<where>" 
			+ "<if test=\"refUserWxUnionid != null and refUserWxUnionid != '' \">" + " and ref_user_wx_unionid = #{refUserWxUnionid}" + "</if>" + "</where> order by id desc</script>")
	public List<GroubaOrder> selectMyOrder4user(@Param(value = "refUserWxUnionid") String refUserWxUnionid);
	/**
	 * 查询相关订单同团的用户、头像、状态{order_trace,ref_grouba_trace,userImgs,ordersStatus,orderRefUsers}
	 * @param orderTraces
	 * @return
	 */
	@Select(value = "<script>select order_trace,ref_grouba_trace,leader,order_expired_time,GROUP_CONCAT(ref_user_img ORDER BY id) userImgs,GROUP_CONCAT(order_status ORDER BY id) ordersStatus,GROUP_CONCAT(ref_user_wx_unionid ORDER BY id) orderRefUsers"
			+ " from grouba_order" + "<where>"
			+ "<if test=\"orderTraces != null and orderTraces != '' \">" + " and order_trace in (${orderTraces})" + "</if>"
			+ "<if test=\"refGroubTrace != null and refGroubTrace != '' \">" + " and ref_groub_trace = #{refGroubTrace}" + "</if>"
			+ "</where> GROUP BY order_trace</script>")
	public List<GroubaOrder> selectMyOrder4userImgs(@Param(value = "orderTraces") String orderTraces,@Param(value = "refGroubTrace") String refGroubTrace);
	/**
	 * 查询某店铺下，我参与过的活动商品和订单{ref_grouba_trace,order_trace}
	 * @author chenzhao @date May 16, 2019
	 * @param refGroubTrace
	 * @param refUserWxUnionid
	 * @return
	 */
	@Select(value = "<script>select ref_grouba_trace,order_trace from grouba_order"
			+ " where ref_groub_trace=#{refGroubTrace} and ref_user_wx_unionid=#{refUserWxUnionid}"
			+ " GROUP BY ref_grouba_trace</script>")
	public List<GroubaOrder> selectMyOrder4Groub(@Param(value = "refGroubTrace") String refGroubTrace,@Param(value = "refUserWxUnionid") String refUserWxUnionid);
	
	@Select(value = "<script>select order_trace,ref_groub_trace,ref_grouba_trace,order_expired_time,order_status,ref_user_wx_unionid,leader,ref_user_img,form_id,join_succeed_time,ref_user_wx_unionid,grouba_size,goods_name,goods_img,goods_price,grouba_discount_amount,grouba_isnew"
			+ " from grouba_order"
			+ " where order_trace = #{orderTrace} and ref_user_wx_unionid=#{refUserWxUnionid}</script>")
	public GroubaOrder selectOne(@Param(value = "orderTrace") String orderTrace,
			@Param(value = "refUserWxUnionid") String refUserWxUnionid);
	
	@Select(value = "<script>select count(1)"
			+ " from grouba_order" + "<where>" 
			+ "<if test=\"orderTrace != null and orderTrace != '' \">" + " and order_trace = #{orderTrace}" + "</if>"
			+ "<if test=\"refGroubTrace != null and refGroubTrace != '' \">" + " and ref_groub_trace = #{refGroubTrace}" + "</if>"
			+ "</where></script>")
	public int selectCount(@Param(value = "orderTrace") String orderTrace,@Param(value = "refGroubTrace") String refGroubTrace);
	/**
	 * 查询某商品，某用户参团次数
	 * @author chenzhao @date May 17, 2019
	 * @param orderTrace
	 * @param refGroubTrace
	 * @return
	 */
	@Select(value = "<script>select count(1)"
			+ " from grouba_order" + "<where>" 
			+ "<if test=\"refUserWxUnionid != null and refUserWxUnionid != '' \">" + " and ref_user_wx_unionid = #{refUserWxUnionid}" + "</if>"
			+ "<if test=\"refGroubaTrace != null and refGroubaTrace != '' \">" + " and ref_grouba_trace = #{refGroubaTrace}" + "</if>"
			+ "</where></script>")
	public int selectOrderCount4User(@Param(value = "refGroubaTrace") String refGroubaTrace,@Param(value = "refUserWxUnionid") String refUserWxUnionid);
	/**
	 * 查询某商品已成团数
	 * @author chenzhao @date May 17, 2019
	 * @param orderTrace
	 * @param refGroubTrace
	 * @return
	 */
	@Select(value = "<script>select count(1)"
			+ " from grouba_order" + "<where>" 
			+ "<if test=\"refGroubTrace != null and refGroubTrace != '' \">" + " and ref_groub_trace = #{refGroubTrace}" + "</if>"
			+ "<if test=\"refGroubaTrace != null and refGroubaTrace != '' \">" + " and ref_grouba_trace = #{refGroubaTrace}" + "</if>"
			+ "</where></script>")
	public int selectOpenGroubCount(@Param(value = "refGroubTrace") String refGroubTrace,@Param(value = "refGroubaTrace") String refGroubaTrace);

	@Insert(value = "<script>INSERT INTO grouba_order"
			+ " (order_trace,ref_groub_trace,ref_grouba_trace,order_expired_time,ref_user_wx_unionid,leader,ref_user_img,client_ip"
			+ ",form_id,ref_user_wx_openid,grouba_size,goods_name,goods_img,goods_price,grouba_discount_amount,grouba_isnew) "
			+ " VALUES (#{orderTrace},#{refGroubTrace},#{refGroubaTrace},#{orderExpiredTime},#{refUserWxUnionid},#{leader},#{refUserImg},#{clientIp}"
			+ ",#{formId},#{refUserWxOpenid},#{groubaSize},#{goodsName},#{goodsImg},#{goodsPrice},#{groubaDiscountAmount},#{groubaIsnew})</script>")
	public int insert(GroubaOrder groubaOrder);

	@Update(value = "<script>UPDATE grouba_order SET  uptime=NOW()"
			+ "<if test=\"orderStatus != null and orderStatus != '' \">" + ",order_status = #{orderStatus}" + "</if>"
			+ "<if test=\"joinSucceedTime != null and joinSucceedTime != '' \">" + ",join_succeed_time = NOW()" + "</if>" 
			+ "<if test=\"consumeTime != null and consumeTime != '' \">" + ",consume_time = NOW()" + "</if>"
			+ "<if test=\"consumeSuccessTime != null and consumeSuccessTime != '' \">" + ",consume_success_time = NOW()" + "</if>"  
			+ "<where>"
			+ "<if test=\"orderTrace != null and orderTrace != '' \">" + " and order_trace = #{orderTrace}" + "</if>"
			+ "<if test=\"refUserWxUnionid != null and refUserWxUnionid != '' \">" + " and ref_user_wx_unionid = #{refUserWxUnionid}" + "</if>"
			+ "</where></script>")
	public int update(GroubaOrder groubaOrder);
	
	@Select(value = "<script>select order_trace,ref_groub_trace,ref_grouba_trace,order_expired_time,order_status,ref_user_wx_unionid,grouba_size,leader,ref_user_img,form_id,join_succeed_time,goods_name,goods_img,goods_price,grouba_discount_amount,grouba_isnew"
			+ " from grouba_order" + "<where>" + "<if test=\"orderTrace != null and orderTrace != '' \">"
			+ " and order_trace = #{orderTrace}" + "</if>"
			+ "</where></script>")
	public List<GroubaOrder> selectShareOrder(@Param(value = "orderTrace") String orderTrace);

}
