package com.club.service.impl;

import com.club.entity.User;
import com.club.entity.vo.UserLoginVo;
import com.club.mapper.UserMapper;
import com.club.properties.WxProperties;
import com.club.service.UserService;
import com.club.util.JwtUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.club.exception.ClubDefinedException;
import com.club.entity.vo.ResultCodeEnum;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.club.entity.request.UserQueryDto;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WxProperties wxProperties;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public UserLoginVo wxLogin(String code) {
        // 调用微信接口获取openid
        String openid = getOpenidFromWx(code);

        // 根据openid查询用户
        User user = userMapper.getByOpenid(openid);

        // 如果用户不存在，则注册新用户
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setCreatedAt(LocalDateTime.now());
            userMapper.save(user);
        }

        // 生成JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        String token = JwtUtil.createJWT("club-key", 7200000L, claims); // 例如：签发者为"zyh-app"，有效期1小时


        // 构造返回结果
        UserLoginVo userLoginVo = UserLoginVo.builder()
                .id(Long.valueOf(user.getUserId()))
                .openid(user.getOpenid())
                .token(token)
                .build();

        return userLoginVo;
    }

    /**
     * 调用微信接口获取openid
     * @param code 微信登录凭证
     * @return openid
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

    @Override
    public User getByOpenid(String openid) {
        return userMapper.getByOpenid(openid);
    }

    /**
     * 微信用户退出登录
     * @param openid 用户openid
     */
    @Override
    public void removeUserInfo(String openid) {
        userMapper.removeUserInfo(openid);
    }


    // 根据用户id修改用户信息
    @Override
    public void updateUserInfo(User user) {
        userMapper.update( user);
    }

    //admin用于获取用户列表 --wsx -2025.11.17
    @Override
    public PageInfo<User> getUserList(UserQueryDto userQueryDto) {
        PageHelper.startPage(userQueryDto.getPageNum(), userQueryDto.getPageSize());
        List<User> users= userMapper.getUserList(userQueryDto);
        return new PageInfo<>( users);
    }

    @Override
    public void updateUserStatus(Integer userId, String status) {
        // 验证状态值是否合法
//        if (!"1".equals(status) && !"2".equals(status)) {
//            throw new ClubDefinedException(ResultCodeEnum.PARAM_ERROR);
//        }

        // 验证用户是否存在
        User user = userMapper.getUserById(Long.valueOf(userId));
        if (user == null) {
            throw new ClubDefinedException(ResultCodeEnum.USER_NOT_EXIST);
        }

        // 更新用户状态
        userMapper.updateStatus(userId, status);
        log.info("更新用户状态成功，用户ID: {}, 新状态: {}", userId, status);
    }
}