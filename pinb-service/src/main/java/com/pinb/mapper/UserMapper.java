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
	
	@Select(value = "<script>select user_wx_unionid,user_wx_openid,user_phone,user_img,credit_score_user,is_open_groub,credit_score_groub"
			+ " from user" + "<where>"
			+ "<if test=\"isOpenGroub != null and isOpenGroub != '' \">" + " and is_open_groub = #{isOpenGroub}"+ "</if>"
			+ "</where></script>")
	public List<User> select(@Param(value = "isOpenGroub") String isOpenGroub);

	@Select(value = "select  user_wx_unionid,user_wx_openid,user_phone,user_img,credit_score_user,is_open_groub,credit_score_groub"
			+ " from user" + " where user_wx_unionid = #{userWxUnionid}")
	public User selectOne(@Param(value = "userWxUnionid") String userWxUnionid);

	@Insert(value = "INSERT INTO user"
			+ " (user_wx_unionid,user_wx_openid,user_phone,user_img,credit_score_user,is_open_groub,credit_score_groub) "
			+ " VALUES (#{userWxUnionid},#{userWxOpenid},#{userPhone},#{userImg},#{creditScoreUser},#{isOpenGroub},#{creditScoreGroub})")
	public int insert(User user);
	
	@Update(value = "<script>UPDATE user SET  uptime=NOW()"
			+ "<if test=\"userPhone != null and userPhone != '' \">" + ",user_phone = #{userPhone}" + "</if>"
			+ "<if test=\"userImg != null and userImg != '' \">" + ",user_img = #{userImg}" + "</if>"
			+ "<if test=\"creditScoreUser != null and creditScoreUser != '' \">" + ",credit_score_user = #{creditScoreUser}" + "</if>"
			+ "<if test=\"isOpenGroub != null and isOpenGroub != '' \">" + ",is_open_groub = #{isOpenGroub}" + "</if>"
			+ "<if test=\"creditScoreGroub != null and creditScoreGroub != '' \">" + " and credit_score_groub = #{creditScoreGroub}"+ "</if>"
			+ " where groub_trace=#{groubTrace}</script>")
	public int update(User user);

}
