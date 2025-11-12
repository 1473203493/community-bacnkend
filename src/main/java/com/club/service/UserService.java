package com.club.service;

import com.club.entity.User;
import com.club.entity.vo.UserLoginVo;

public interface UserService {

    /**
     * 更新用户信息
     */
    void updateUserInfo(User user);

    /**
     * 微信登录
     */
    UserLoginVo wxLogin(String code);

    /**
     * 根据openid查询用户
     */
    User getByOpenid(String openid);


    /**
     * 微信登出
     * @param openid
     */
    void removeUserInfo(String openid);
}