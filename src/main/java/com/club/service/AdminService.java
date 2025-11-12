package com.club.service;


import com.club.entity.request.AdminLoginDto;
import com.club.entity.vo.AdminLoginVo;

public interface AdminService {

    AdminLoginVo login(AdminLoginDto adminLoginDto);//管理员登录接口
}
