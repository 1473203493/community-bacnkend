package com.club.controller.student;

import com.club.entity.vo.Result;
import com.club.entity.vo.ResultCodeEnum;
import com.club.entity.vo.UserLoginVo;
import com.club.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 学生认证相关接口
 * @author zyh
 * @date 2025/11/11
 */
@Slf4j
@Tag(name = "认证接口")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 发送邮箱验证码
     * @param email 邮箱地址
     * @param scene 场景（register/reset/changeEmail）
     * @return 结果
     */
    @Operation(summary = "发送邮箱验证码")
    @PostMapping("/sendEmailCode")
    public Result<String> sendEmailCode(@RequestParam String email, @RequestParam String scene) {
        log.info("发送邮箱验证码请求：email={}, scene={}", email, scene);
        return authService.sendEmailCode(email, scene);
    }

    /**
     * 用户注册
     * @param request 注册请求参数
     * @return 注册结果
     */
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<UserLoginVo> register(@RequestBody RegisterRequest request) {
        log.info("用户注册请求：{}", request);
        return authService.register(request);
    }

    /**
     * 账号密码登录
     * @param request 登录请求参数
     * @return 登录结果
     */
    @Operation(summary = "账号密码登录")
    @PostMapping("/login")
    public Result<UserLoginVo> login(@RequestBody LoginRequest request) {
        log.info("账号密码登录请求：{}", request);
        return authService.login(request);
    }

    /**
     * 微信快捷登录绑定邮箱
     * @param request 绑定邮箱请求参数
     * @return 绑定结果
     */
    @Operation(summary = "微信快捷登录绑定邮箱")
    @PostMapping("/wechatBindMail")
    public Result<UserLoginVo> wechatBindMail(@RequestBody WechatBindMailRequest request) {
        log.info("微信快捷登录绑定邮箱请求：{}", request);
        return authService.wechatBindMail(request);
    }

    /**
     * 微信快捷登录
     * @param code 微信登录凭证
     * @return 登录结果
     */
    @Operation(summary = "微信快捷登录")
    @PostMapping("/wechatLogin")
    public Result<UserLoginVo> wechatLogin(@RequestParam String code) {
        log.info("微信快捷登录请求：code={}", code);
        return authService.wechatLogin(code);
    }

    /**
     * 重置密码
     * @param request 重置密码请求参数
     * @return 重置结果
     */
    @Operation(summary = "重置密码")
    @PostMapping("/resetPassword")
    public Result<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        log.info("重置密码请求：{}", request);
        return authService.resetPassword(request);
    }

    /**
     * 获取当前登录用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    @Operation(summary = "获取当前用户信息")
    @GetMapping("/info")
    public Result<?> getUserInfo(@RequestParam Long userId) {
        log.info("获取用户信息请求：userId={}", userId);
        return authService.getUserInfo(userId);
    }

    /**
     * 登出接口
     * @return 登出结果
     */
    @Operation(summary = "登出")
    @PostMapping("/logout")
    public Result<?> logout() {
        log.info("用户登出请求");
        return authService.logout();
    }

    /**
     * 刷新token接口
     * @param tokenData token信息
     * @return 刷新结果
     */
    @Operation(summary = "刷新token")
    @PostMapping("/refresh")
    public Result<?> refreshToken(@RequestBody Map<String, Object> tokenData) {
        log.info("刷新token请求：{}", tokenData);
        return authService.refreshToken(tokenData);
    }

    // 请求参数类
    public static class RegisterRequest {
        private String email;
        private String password;
        private String confirmPassword;
        private String emailCode;
        private String studentNo;
        private String name;

        // getter and setter
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getConfirmPassword() {
            return confirmPassword;
        }

        public void setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
        }

        public String getEmailCode() {
            return emailCode;
        }

        public void setEmailCode(String emailCode) {
            this.emailCode = emailCode;
        }

        public String getStudentNo() {
            return studentNo;
        }

        public void setStudentNo(String studentNo) {
            this.studentNo = studentNo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class LoginRequest {
        private String email;
        private String password;

        // getter and setter
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class WechatBindMailRequest {
        private String openid;
        private String email;
        private String emailCode;

        // getter and setter
        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getEmailCode() {
            return emailCode;
        }

        public void setEmailCode(String emailCode) {
            this.emailCode = emailCode;
        }
    }

    public static class ResetPasswordRequest {
        private String email;
        private String emailCode;
        private String newPassword;
        private String confirmPassword;

        // getter and setter
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getEmailCode() {
            return emailCode;
        }

        public void setEmailCode(String emailCode) {
            this.emailCode = emailCode;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }

        public String getConfirmPassword() {
            return confirmPassword;
        }

        public void setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
        }
    }
}