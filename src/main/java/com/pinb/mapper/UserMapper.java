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
	
	@Select(value = "<script>select user_wx_unionid,user_wx_openid,user_phone,user_img,credit_score_user,is_open_groub,credit_score_groub,user_wxinfo_md5"
			+ " from user" + "<where>"
			+ "<if test=\"isOpenGroub != null and isOpenGroub != '' \">" + " and is_open_groub = #{isOpenGroub}"+ "</if>"
			+ "</where></script>")
	public List<User> select(@Param(value = "isOpenGroub") String isOpenGroub);

	@Select(value = "select  user_wx_unionid,user_wx_openid,user_phone,user_img,credit_score_user,is_open_groub,credit_score_groub,user_wxinfo_md5"
			+ " from user" + " where user_wx_unionid = #{userWxUnionid}")
	public User selectOne(@Param(value = "userWxUnionid") String userWxUnionid);

	@Insert(value = "INSERT INTO user"
			+ " (user_wx_unionid,user_wx_openid,user_phone,user_img,credit_score_user,is_open_groub,credit_score_groub,user_ip"
			+ ",user_brand,user_model,user_system,user_platform,user_benchmark,user_nickname,user_city,user_province,user_wxinfo_md5) "
			+ " VALUES (#{userWxUnionid},#{userWxOpenid},#{userPhone},#{userImg},#{creditScoreUser},#{isOpenGroub},#{creditScoreGroub},#{userIp}"
			+ ",#{userBrand}#{userModel}#{usersystem}#{userPlatform}#{userBenchmark}#{userNickname}#{userCity}#{userProvince}#{userWxinfoMd5})")
	public int insert(User user);
	
	@Update(value = "<script>UPDATE user SET  uptime=NOW()"
			+ "<if test=\"userPhone != null and userPhone != '' \">" + ",user_phone = #{userPhone}" + "</if>"
			+ "<if test=\"userImg != null and userImg != '' \">" + ",user_img = #{userImg}" + "</if>"
			+ "<if test=\"creditScoreUser != null and creditScoreUser != '' \">" + ",credit_score_user = #{creditScoreUser}" + "</if>"
			+ "<if test=\"isOpenGroub != null and isOpenGroub != '' \">" + ",is_open_groub = #{isOpenGroub}" + "</if>"
			+ "<if test=\"creditScoreGroub != null and creditScoreGroub != '' \">" + " and credit_score_groub = #{creditScoreGroub}"+ "</if>"
			+ "<if test=\"userBrand != null and userBrand != '' \">" + ",user_brand = #{userBrand}" + "</if>"
			+ "<if test=\"userModel != null and userModel != '' \">" + ",user_model = #{userModel}" + "</if>"
			+ "<if test=\"usersystem != null and usersystem != '' \">" + ",user_system = #{usersystem}" + "</if>"
			+ "<if test=\"userPlatform != null and userPlatform != '' \">" + ",user_platform = #{userPlatform}" + "</if>"
			+ "<if test=\"userBenchmark != null and userBenchmark != '' \">" + " and user_benchmark = #{userBenchmark}"+ "</if>"
			+ "<if test=\"userNickname != null and userNickname != '' \">" + ",user_nickname = #{userNickname}" + "</if>"
			+ "<if test=\"userCity != null and userCity != '' \">" + ",user_city = #{userCity}" + "</if>"
			+ "<if test=\"userProvince != null and userProvince != '' \">" + ",user_province = #{userProvince}" + "</if>"
			+ "<if test=\"userWxinfoMd5 != null and userWxinfoMd5 != '' \">" + ",user_wxinfo_md5 = #{userWxinfoMd5}" + "</if>"
			+ " where groub_trace=#{groubTrace}</script>")
	public int update(User user);

}
