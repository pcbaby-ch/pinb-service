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

import com.pinb.entity.GroupBar;


/**
 * 拼吧店铺CRUD
 * @author chenzhao @date Apr 9, 2019
 */
@Repository
@Mapper
public interface GroupBarMapper {
	
	@Select(value = "<script>select groub_trace,ref_user_wx_unionid,groub_name,groub_img,groub_phone,groub_address"
			+ " from group_bar" + "<where>"
			+ "<if test=\"refUserWxUnionid != null and refUserWxUnionid != '' \">" + " and ref_user_wx_unionid = #{refUserWxUnionid}"+ "</if>"
			+ "</where></script>")
	public List<GroupBar> select(@Param(value = "refUserWxUnionid") String refUserWxUnionid);

	@Select(value = "select  groub_trace,ref_user_wx_unionid,groub_name,groub_img,groub_phone,groub_address"
			+ " from group_bar" + " where ref_user_wx_unionid = #{refUserWxUnionid}")
	public GroupBar selectOne(@Param(value = "refUserWxUnionid") String refUserWxUnionid);

	@Insert(value = "INSERT INTO group_bar"
			+ " ( groub_trace,ref_user_wx_unionid,groub_name,groub_img,groub_phone,groub_address) "
			+ " VALUES (#{groubTrace},#{refUserWxUnionid},#{groubName},#{groubImg},#{groubPhone},#{groubAddress})")
	public int insert(GroupBar groupBar);
	
	@Update(value = "<script>UPDATE group_bar SET  uptime=NOW()"
			+ "<if test=\"groubName != null and groubName != '' \">" + ",groub_name = #{groubName}" + "</if>"
			+ "<if test=\"groubImg != null and groubImg != '' \">" + ",groub_img = #{groubImg}" + "</if>"
			+ "<if test=\"groubPhone != null and groubPhone != '' \">" + ",groub_phone = #{groubPhone}" + "</if>"
			+ "<if test=\"groubAddress != null and groubAddress != '' \">" + " and groub_address = #{groubAddress}"+ "</if>"
			+ " where groub_trace=#{groubTrace}</script>")
	public int update(GroupBar groupBar);

}
