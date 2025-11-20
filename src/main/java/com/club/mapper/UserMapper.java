package com.club.mapper;

import com.club.entity.User;
import org.apache.ibatis.annotations.*;
import com.club.entity.request.UserQueryDto;

import java.util.List;


@Mapper
public interface UserMapper {
    List<User> getUserList(UserQueryDto userQueryDto);
    /**
     * 根据用户ID查询用户信息
     * @param userId
     * @return
     */
    @Select("select * from user where user_id = #{userId}")
    User getUserById(Long userId);

    /**
     * 根据微信openid查询用户信息
     * @param openid
     * @return
     */
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    /**
     * 保存用户信息
     * @param user
     */
    /**
     * 保存用户信息
     * @param user
     */
    @Insert("insert into user (openid, student_no, name, email, password, role, status, created_at) values (#{openid}, #{studentNo}, #{name}, #{email}, #{password}, #{role}, #{status}, NOW())")
    void save(User user);


    @Update("update user set student_no = #{studentNo}, name = #{name}, email = #{email}, password = #{password}, role = #{role}, status = #{status} where user_id = #{userId}")
    void update(User user);

    @Delete("delete from user where openid = #{openid}")
    void removeUserInfo(String openid);

    @Update("update user set status=#{status} where user_id = #{userId}")
    void updateStatus(@Param("userId") Integer userId, @Param("status") String status);

}
