package com.club.mapper;

import com.club.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {


    /**
     * 根据用户名查询管理员信息
     * @param username
     * @return
     */
    @Select("select * from admin where username = #{username}")
    Admin selectByUsername(String username);
}