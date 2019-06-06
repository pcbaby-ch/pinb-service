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

import com.pinb.entity.User;


/**
 * 拼吧用户CRUD
 * @author chenzhao @date Apr 9, 2019
 */
@Repository
@Mapper
public interface UserMapper {
	
	@Select(value = "<script>select wx_unionid,wx_openid,phone,head_img,credit_score_user,groub_trace,is_open_groub,credit_score_groub,nickname,`city`,`province`,`latitude`,`longitude`"
			+ " from user" + "<where>"
			+ "<if test=\"isOpenGroub != null and isOpenGroub != '' \">" + " and is_open_groub = #{isOpenGroub}"+ "</if>"
			+ "</where></script>")
	public List<User> select(@Param(value = "isOpenGroub") String isOpenGroub);

	@Select(value = "<script>select  wx_unionid,wx_openid,phone,head_img,credit_score_user,groub_trace,is_open_groub,credit_score_groub,nickname,`city`,`province`,`latitude`,`longitude`"
			+ " from user" + "<where>"
			+ "<if test=\"wxUnionid != null and wxUnionid != '' \">" + " and wx_unionid = #{wxUnionid}"+ "</if>"
			+ "<if test=\"phone != null and phone != '' \">" + " and phone = #{phone}"+ "</if>"
			+ "</where></script>")
	public User selectOne(@Param(value = "wxUnionid") String wxUnionid,@Param(value = "phone") String phone);

	@Insert(value = "INSERT INTO user"
			+ " (`wx_unionid`,`wx_openid`,`phone`,`head_img`,groub_trace,`is_open_groub`,`register_ip`"
			+ ",`brand`,`model`,`system`,`platform`,`benchmark`,`nickname`,`gender`,`city`,`province`,`latitude`,`longitude`) "
			+ " VALUES (#{wxUnionid},#{wxOpenid},#{phone},#{headImg},#{groubTrace},#{isOpenGroub},#{clientIp}"
			+ ",#{brand},#{model},#{system},#{platform},#{benchmark},#{nickname},#{gender},#{city},#{province},#{latitude},#{longitude})")
	public int insert(User user);
	
	@Update(value = "<script>UPDATE user SET  uptime=NOW(),upcount=upcount+1"
			+ "<if test=\"phone != null and phone != '' \">" + ",phone = #{phone}" + "</if>"
			+ "<if test=\"headImg != null and headImg != '' \">" + ",head_img = #{headImg}" + "</if>"
			+ "<if test=\"creditScoreUser != null and creditScoreUser != '' \">" + ",credit_score_user = #{creditScoreUser}" + "</if>"
			+ "<if test=\"groubTrace != null and groubTrace != '' \">" + ",groub_trace = #{groubTrace}" + "</if>"
			+ "<if test=\"isOpenGroub != null and isOpenGroub != '' \">" + ",is_open_groub = #{isOpenGroub}" + "</if>"
			+ "<if test=\"creditScoreGroub != null and creditScoreGroub != '' \">" + " and credit_score_groub = #{creditScoreGroub}"+ "</if>"
			+ "<if test=\"brand != null and brand != '' \">" + ",brand = #{brand}" + "</if>"
			+ "<if test=\"model != null and model != '' \">" + ",model = #{model}" + "</if>"
			+ "<if test=\"system != null and system != '' \">" + ",system = #{system}" + "</if>"
			+ "<if test=\"platform != null and platform != '' \">" + ",platform = #{platform}" + "</if>"
			+ "<if test=\"benchmark != null and benchmark != '' \">" + ",benchmark = #{benchmark}"+ "</if>"
			+ "<if test=\"nickname != null and nickname != '' \">" + ",nickname = #{nickname}" + "</if>"
			+ "<if test=\"city != null and city != '' \">" + ",city = #{city}" + "</if>"
			+ "<if test=\"province != null and province != '' \">" + ",province = #{province}" + "</if>"
			+ "<where>"
			+ "<if test=\"wxUnionid != null and wxUnionid != '' \">" + " and wx_unionid = #{wxUnionid}"+ "</if>"
			+ "<if test=\"phone != null and phone != '' \">" + " and phone = #{phone}"+ "</if>"
			+ "</where></script>")
	public int update(User user);
	
	@Update(value = "<script>UPDATE user SET  uptime=NOW(),upcount=upcount+1"
			+ "<if test=\"wxUnionid != null and wxUnionid != '' \">" + ",wx_unionid = #{wxUnionid}" + "</if>"
			+ " where phone = #{phone} and wxUnionid <![CDATA[<>]]> wxOpenid</script>")
	public int updateUnionid(User user);

}
