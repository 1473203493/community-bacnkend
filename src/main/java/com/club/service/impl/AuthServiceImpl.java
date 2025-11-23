package com.club.service.impl;

import com.club.controller.student.AuthController;
import com.club.entity.User;
import com.club.entity.vo.Result;
import com.club.entity.vo.ResultCodeEnum;
import com.club.entity.vo.UserLoginVo;
import com.club.exception.ClubDefinedException;
import com.club.mapper.UserMapper;
import com.club.properties.WxProperties;
import com.club.service.AuthService;
import com.club.service.UserService;
import com.club.util.JwtUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 * @author zyh
 * @date 2025/11/11
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private WxProperties wxProperties;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 生成6位随机验证码
     */
    private String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    /**
     * 发送邮件（模拟实现，实际需要集成邮件服务）
     */
    private void sendEmail(String email, String subject, String content) {
        // TODO: 集成实际的邮件发送服务
        log.info("发送邮件到: {}, 主题: {}, 内容: {}", email, subject, content);
    }

    /**
     * 从微信获取openid
     */
    private String getOpenidFromWx(String code) {
        // 微信登录接口地址
        String wxLoginUrl = "https://api.weixin.qq.com/sns/jscode2session" +
                "?appid=" + wxProperties.getAppId() +
                "&secret=" + wxProperties.getAppSecret() +
                "&js_code=" + code +
                "&grant_type=authorization_code";

        try {
            // 发送请求
            ResponseEntity<String> response = restTemplate.getForEntity(wxLoginUrl, String.class);
            String responseBody = response.getBody();

            // 解析JSON获取openid
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            if (jsonNode.has("errcode")) {
                throw new ClubDefinedException(ResultCodeEnum.WX_ERROR);
            }

            return jsonNode.get("openid").asText();
        } catch (Exception e) {
            log.error("调用微信登录接口失败", e);
            throw new ClubDefinedException(ResultCodeEnum.WX_ERROR);
        }
    }

    /**
     * 生成JWT令牌
     */
    private String generateToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return JwtUtil.createJWT("club-key", 7200000L, claims); // 2小时有效期
    }

    @Override
    public Result<String> sendEmailCode(String email, String scene) {
        try {
            // 验证邮箱格式
            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "邮箱格式不正确");
            }

            // 验证场景参数
            if (!"register".equals(scene) && !"reset".equals(scene) && !"changeEmail".equals(scene)) {
                return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "场景参数不正确");
            }

            // 检查是否频繁发送
            String key = "email:code:" + scene + ":" + email;
            if (redisTemplate.hasKey(key)) {
                return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "验证码发送过于频繁，请稍后再试");
            }

            // 生成验证码
            String code = generateVerificationCode();

            // 存储验证码到Redis，5分钟有效期
            redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);

            // 发送邮件
            String subject = "社团管理系统验证码";
            String content = "您的验证码是：" + code + "，有效期5分钟，请勿泄露给他人。";
            sendEmail(email, subject, content);

            return Result.build(null, ResultCodeEnum.SUCCESS.getCode(), "验证码发送成功");
        } catch (Exception e) {
            log.error("发送邮箱验证码失败", e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "验证码发送失败");
        }
    }

    @Override
    public Result<UserLoginVo> register(AuthController.RegisterRequest request) {
        try {
            // 验证参数
            if (request.getPassword() == null || !request.getPassword().equals(request.getConfirmPassword())) {
                return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "两次输入的密码不一致");
            }

            // 验证邮箱验证码
            String key = "email:code:register:" + request.getEmail();
            String storedCode = redisTemplate.opsForValue().get(key);
            if (storedCode == null || !storedCode.equals(request.getEmailCode())) {
                return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "验证码错误或已过期");
            }

            // 检查邮箱是否已被注册
            User existingUser = userMapper.getByEmail(request.getEmail());
            if (existingUser != null) {
                return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "邮箱已被注册");
            }

            // 检查学号是否已被注册
            User existingUserByStudentNo = userMapper.getByStudentNo(request.getStudentNo());
            if (existingUserByStudentNo != null) {
                return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "学号已被注册");
            }

            // 创建新用户
            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword()); // 实际应该进行加密
            user.setStudentNo(request.getStudentNo());
            user.setName(request.getName());
            user.setRole("student");
            user.setStatus("1"); // 1表示正常
            user.setCreatedAt(LocalDateTime.now());

            // 保存用户
            userMapper.save(user);

            // 生成令牌
            String token = generateToken(Long.valueOf(user.getUserId()));

            // 清除验证码
            redisTemplate.delete(key);

            // 构建返回结果
            UserLoginVo loginVo = new UserLoginVo();
            loginVo.setId(Long.valueOf(user.getUserId()));
            loginVo.setToken(token);
            loginVo.setName(user.getName());
            loginVo.setEmail(user.getEmail());
            loginVo.setStudentNo(user.getStudentNo());

            return Result.build(loginVo, ResultCodeEnum.SUCCESS.getCode(), "注册成功");
        } catch (ClubDefinedException e) {
            return Result.build(null, e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("用户注册失败", e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "注册失败");
        }
    }

    @Override
    public Result<UserLoginVo> login(AuthController.LoginRequest request) {
        try {
            // 根据邮箱查询用户
            User user = userMapper.getByEmail(request.getEmail());
            if (user == null || !user.getPassword().equals(request.getPassword())) { // 实际应该进行密码验证
                return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "邮箱或密码错误");
            }

            // 检查用户状态
            if (!"1".equals(user.getStatus())) {
                return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "账号已被禁用");
            }

            // 生成令牌
            String token = generateToken(Long.valueOf(user.getUserId()));

            // 构建返回结果
            UserLoginVo loginVo = new UserLoginVo();
            loginVo.setId(Long.valueOf(user.getUserId()));
            loginVo.setToken(token);
            loginVo.setName(user.getName());
            loginVo.setEmail(user.getEmail());
            loginVo.setStudentNo(user.getStudentNo());
            loginVo.setOpenid(user.getOpenid());

            return Result.build(loginVo, ResultCodeEnum.SUCCESS.getCode(), "登录成功");
        } catch (Exception e) {
            log.error("账号密码登录失败", e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "登录失败");
        }
    }

    @Override
    public Result<UserLoginVo> wechatBindMail(AuthController.WechatBindMailRequest request) {
        try {
            // 验证邮箱验证码
            String key = "email:code:register:" + request.getEmail();
            String storedCode = redisTemplate.opsForValue().get(key);
            if (storedCode == null || !storedCode.equals(request.getEmailCode())) {
                return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "验证码错误或已过期");
            }

            // 根据openid查询用户
            User user = userMapper.getByOpenid(request.getOpenid());
            if (user == null) {
                return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "用户不存在");
            }

            // 检查邮箱是否已被其他用户绑定
            User existingUser = userMapper.getByEmail(request.getEmail());
            if (existingUser != null && !existingUser.getUserId().equals(user.getUserId())) {
                return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "邮箱已被其他用户绑定");
            }

            // 更新用户邮箱
            user.setEmail(request.getEmail());
            userMapper.update(user);

            // 生成令牌
            String token = generateToken(Long.valueOf(user.getUserId()));

            // 清除验证码
            redisTemplate.delete(key);

            // 构建返回结果
            UserLoginVo loginVo = new UserLoginVo();
            loginVo.setId(Long.valueOf(user.getUserId()));
            loginVo.setToken(token);
            loginVo.setName(user.getName());
            loginVo.setEmail(user.getEmail());
            loginVo.setStudentNo(user.getStudentNo());
            loginVo.setOpenid(user.getOpenid());

            return Result.build(loginVo, ResultCodeEnum.SUCCESS.getCode(), "邮箱绑定成功");
        } catch (Exception e) {
            log.error("微信绑定邮箱失败", e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "邮箱绑定失败");
        }
    }

    @Override
    public Result<UserLoginVo> wechatLogin(String code) {
        try {
            // 调用微信接口获取openid
            String openid = getOpenidFromWx(code);

            // 根据openid查询用户
            User user = userMapper.getByOpenid(openid);

            // 如果用户不存在，则创建新用户
            if (user == null) {
                user = new User();
                user.setOpenid(openid);
                user.setRole("student");
                user.setStatus("1");
                user.setCreatedAt(LocalDateTime.now());
                userMapper.save(user);
            }

            // 生成令牌
            String token = generateToken(Long.valueOf(user.getUserId()));

            // 构建返回结果
            UserLoginVo loginVo = new UserLoginVo();
            loginVo.setId(Long.valueOf(user.getUserId()));
            loginVo.setToken(token);
            loginVo.setName(user.getName());
            loginVo.setEmail(user.getEmail());
            loginVo.setStudentNo(user.getStudentNo());
            loginVo.setOpenid(user.getOpenid());

            return Result.build(loginVo, ResultCodeEnum.SUCCESS.getCode(), "登录成功");
        } catch (ClubDefinedException e) {
            return Result.build(null, e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("微信登录失败", e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "登录失败");
        }
    }

    @Override
    public Result<String> resetPassword(AuthController.ResetPasswordRequest request) {
        try {
            // 验证参数
            if (request.getNewPassword() == null || !request.getNewPassword().equals(request.getConfirmPassword())) {
                return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "两次输入的密码不一致");
            }

            // 验证邮箱验证码
            String key = "email:code:reset:" + request.getEmail();
            String storedCode = redisTemplate.opsForValue().get(key);
            if (storedCode == null || !storedCode.equals(request.getEmailCode())) {
                return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "验证码错误或已过期");
            }

            // 根据邮箱查询用户
            User user = userMapper.getByEmail(request.getEmail());
            if (user == null) {
                return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "用户不存在");
            }

            // 更新密码
            user.setPassword(request.getNewPassword()); // 实际应该进行加密
            userMapper.update(user);

            // 清除验证码
            redisTemplate.delete(key);

            return Result.build(null, ResultCodeEnum.SUCCESS.getCode(), "密码重置成功");
        } catch (Exception e) {
            log.error("重置密码失败", e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "密码重置失败");
        }
    }

    @Override
    public Result<?> getUserInfo(Long userId) {
        try {
            log.info("获取用户信息请求：userId={}", userId);

            // 根据用户ID查询用户
            User user = userMapper.getById(userId);
            if (user == null) {
                return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "用户不存在");
            }

            // 构建用户信息Map（不包含敏感信息）
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getUserId());
            userInfo.put("name", user.getName());
            userInfo.put("email", user.getEmail());
            userInfo.put("studentNo", user.getStudentNo());
            userInfo.put("role", user.getRole());
            userInfo.put("status", user.getStatus());
            userInfo.put("createdAt", user.getCreatedAt());

            return Result.build(userInfo, ResultCodeEnum.SUCCESS.getCode(), "获取成功");
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "获取用户信息失败");
        }
    }

    @Override
    public Result<?> logout() {
        try {
            log.info("用户登出请求");

            // TODO: 在实际项目中，这里应该从请求头或会话中获取token，并将其加入黑名单
            // 例如：String token = request.getHeader("Authorization");
            // redisTemplate.opsForValue().set("token:blacklist:" + token, "1", 7200, TimeUnit.SECONDS);

            return Result.build(null, ResultCodeEnum.SUCCESS.getCode(), "登出成功");
        } catch (Exception e) {
            log.error("用户登出失败", e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "登出失败");
        }
    }

    @Override
    public Result<?> refreshToken(Map<String, Object> tokenData) {
        try {
            log.info("刷新token请求：{}", tokenData);

            // 获取旧token
            String oldToken = (String) tokenData.get("token");
            if (oldToken == null) {
                return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "token不能为空");
            }

            // 验证token有效性
            Map<String, Object> claims = JwtUtil.parseJWT(oldToken);
            if (claims == null) {
                return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "无效的token");
            }

            // 获取用户ID并生成新token
            Long userId = Long.valueOf(claims.get("userId").toString());
            String newToken = generateToken(userId);

            // 构建返回结果
            Map<String, String> result = new HashMap<>();
            result.put("token", newToken);

            return Result.build(result, ResultCodeEnum.SUCCESS.getCode(), "token刷新成功");
        } catch (Exception e) {
            log.error("刷新token失败", e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "刷新token失败");
        }
    }
}