package com.club.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.club.entity.Admin;
import com.club.entity.request.AdminLoginDto;
import com.club.entity.vo.AdminLoginVo;
import com.club.entity.vo.ResultCodeEnum;
import com.club.exception.ClubDefinedException;
import com.club.mapper.AdminMapper;
import com.club.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private RedisTemplate<String , String> redisTemplate ;


    @Override
    public AdminLoginVo login(AdminLoginDto adminLoginDto) {


        // 校验验证码是否正确
        String captcha = adminLoginDto.getCaptcha();     // 用户输入的验证码
        String codeKey = adminLoginDto.getCodeKey();     // redis中验证码的数据key

        // 从Redis中获取验证码
        String redisCode = redisTemplate.opsForValue().get("admin:login:validatecode:" + codeKey);
        if(StrUtil.isEmpty(redisCode) || !StrUtil.equalsIgnoreCase(redisCode , captcha)) {
            throw new ClubDefinedException(ResultCodeEnum.VALIDATECODE_ERROR) ;
        }

        // 验证通过删除redis中的验证码
        redisTemplate.delete("admin:login:validatecode:" + codeKey) ;

        // 1. 根据用户名查询管理员信息
        Admin admin = adminMapper.selectByUsername(adminLoginDto.getUsername()) ;
        if (admin == null) {
            throw new ClubDefinedException(ResultCodeEnum.LOGIN_ERROR);
        }

        // 验证密码是否正确
        String inputPassword = adminLoginDto.getPassword();
        String md5InputPassword = DigestUtils.md5DigestAsHex(inputPassword.getBytes());
        if(!md5InputPassword.equals(admin.getPassword())) {
            throw new RuntimeException("用户名或者密码错误") ;
        }
        // 生成令牌，保存数据到Redis中
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate .opsForValue().set("admin:login:" + token, JSON.toJSONString(admin) , 30 , TimeUnit.MINUTES);

        // 构建响应结果对象
        AdminLoginVo adminLoginVo = new AdminLoginVo() ;
        adminLoginVo.setToken(token);
        adminLoginVo.setRefresh_token("");

        // 返回
        return adminLoginVo;

    }
}
