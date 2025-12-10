package com.club.service;

import com.club.controller.student.AuthController;
import com.club.entity.vo.Result;
import com.club.entity.vo.UserLoginVo;

import java.util.Map;

/**
 * 认证服务接口
 * @author zyh
 * @date 2025/11/11
 */
public interface AuthService {

    /**
     * 发送邮箱验证码
     * @param email 邮箱地址
     * @param scene 场景（register/reset/changeEmail）
     * @return 发送结果
     */
    Result<String> sendEmailCode(String email, String scene);

    /**
     * 用户注册
     * @param request 注册请求参数
     * @return 注册结果
     */
    Result<UserLoginVo> register(AuthController.RegisterRequest request);

    /**
     * 账号密码登录
     * @param request 登录请求参数
     * @return 登录结果
     */
    Result<UserLoginVo> login(AuthController.LoginRequest request);

    /**
     * 微信快捷登录绑定邮箱
     * @param request 绑定邮箱请求参数
     * @return 绑定结果
     */
    Result<UserLoginVo> wechatBindMail(AuthController.WechatBindMailRequest request);

    /**
     * 微信快捷登录
     * @param code 微信登录凭证
     * @return 登录结果
     */
    Result<UserLoginVo> wechatLogin(String code);

    /**
     * 重置密码
     * @param request 重置密码请求参数
     * @return 重置结果
     */
    Result<String> resetPassword(AuthController.ResetPasswordRequest request);

    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    Result<?> getUserInfo(Long userId);

    /**
     * 用户登出
     * @return 登出结果
     */
    Result<?> logout();

    /**
     * 刷新token
     * @param tokenData token信息
     * @return 刷新结果
     */
    Result<?> refreshToken(Map<String, Object> tokenData);
}