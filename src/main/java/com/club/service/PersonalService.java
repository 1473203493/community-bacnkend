package com.club.service;

import com.club.entity.vo.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 学生端个人中心服务接口
 */
public interface PersonalService {

    /**
     * 获取个人信息
     *
     * @param userId 用户ID
     * @return 个人信息结果
     */
    Result<?> getPersonalInfo(Long userId);

    /**
     * 更新个人信息
     *
     * @param userId 用户ID
     * @param params 更新参数
     * @return 更新结果
     */
    Result<?> updatePersonalInfo(Long userId, Map<String, Object> params);

    /**
     * 修改密码
     *
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改结果
     */
    Result<?> changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 上传头像
     *
     * @param userId 用户ID
     * @param file 头像文件
     * @return 上传结果
     */
    Result<?> uploadAvatar(Long userId, MultipartFile file);

    /**
     * 获取我的社团列表
     *
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 社团列表
     */
    Result<?> getMyClubs(Long userId, Integer pageNum, Integer pageSize);
}