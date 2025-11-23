package com.club.service.impl;

import com.club.entity.vo.Result;
import com.club.entity.vo.ResultCodeEnum;
import com.club.service.PersonalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 学生端个人中心服务实现类
 */
@Slf4j
@Service
public class PersonalServiceImpl implements PersonalService {

    @Override
    public Result<?> getPersonalInfo(Long userId) {
        try {
            // TODO: 实现个人信息查询逻辑，从数据库获取用户信息
            log.info("获取个人信息，userId: {}", userId);

            // 构建模拟数据
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", userId);
            userInfo.put("username", "student_" + userId);
            userInfo.put("realName", "张三");
            userInfo.put("studentId", "20230001");
            userInfo.put("email", "student" + userId + "@example.com");
            userInfo.put("phone", "13800138000");
            userInfo.put("avatar", "/upload/avatars/default.jpg");
            userInfo.put("gender", "男");
            userInfo.put("grade", "2023级");
            userInfo.put("major", "计算机科学与技术");
            userInfo.put("college", "信息科学与工程学院");
            userInfo.put("createdAt", LocalDateTime.now().minusMonths(2));
            userInfo.put("updatedAt", LocalDateTime.now().minusDays(10));

            return Result.build(userInfo, ResultCodeEnum.SUCCESS.getCode(), "获取成功");
        } catch (Exception e) {
            log.error("获取个人信息失败，userId: {}", userId, e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "获取个人信息失败");
        }
    }

    @Override
    public Result<?> updatePersonalInfo(Long userId, Map<String, Object> params) {
        try {
            // TODO: 实现个人信息更新逻辑，更新数据库中的用户信息
            log.info("更新个人信息，userId: {}, params: {}", userId, params);

            // 验证参数
            if (params == null || params.isEmpty()) {
                return Result.build(null, ResultCodeEnum.PARAMS_ERROR.getCode(), "更新参数不能为空");
            }

            // 模拟更新操作
            Map<String, Object> updatedFields = new HashMap<>();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                // 过滤不允许更新的字段
                if (!"id".equals(entry.getKey()) && !"username".equals(entry.getKey()) && !"createdAt".equals(entry.getKey())) {
                    updatedFields.put(entry.getKey(), entry.getValue());
                }
            }

            // 返回更新成功结果
            Map<String, Object> result = new HashMap<>();
            result.put("updatedFields", updatedFields);
            result.put("updatedAt", LocalDateTime.now());

            return Result.build(result, ResultCodeEnum.SUCCESS.getCode(), "更新成功");
        } catch (Exception e) {
            log.error("更新个人信息失败，userId: {}, params: {}", userId, params, e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "更新个人信息失败");
        }
    }

    @Override
    public Result<?> changePassword(Long userId, String oldPassword, String newPassword) {
        try {
            // TODO: 实现密码修改逻辑
            log.info("修改密码，userId: {}", userId);

            // 验证参数
            if (oldPassword == null || newPassword == null) {
                return Result.build(null, ResultCodeEnum.PARAMS_ERROR.getCode(), "密码参数不能为空");
            }

            if (oldPassword.length() < 6 || newPassword.length() < 6) {
                return Result.build(null, ResultCodeEnum.PARAMS_ERROR.getCode(), "密码长度不能少于6位");
            }

            // 模拟旧密码验证
            if (!"123456".equals(oldPassword)) { // 实际应用中应从数据库验证
                return Result.build(null, ResultCodeEnum.BUSINESS_ERROR.getCode(), "原密码错误");
            }

            // 模拟密码修改操作
            // TODO: 实际应加密并更新数据库中的密码

            return Result.build(null, ResultCodeEnum.SUCCESS.getCode(), "密码修改成功");
        } catch (Exception e) {
            log.error("修改密码失败，userId: {}", userId, e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "修改密码失败");
        }
    }

    @Override
    public Result<?> uploadAvatar(Long userId, MultipartFile file) {
        try {
            // TODO: 实现头像上传逻辑
            log.info("上传头像，userId: {}, fileName: {}, fileSize: {}",
                    userId, file.getOriginalFilename(), file.getSize());

            // 验证文件
            if (file.isEmpty()) {
                return Result.build(null, ResultCodeEnum.PARAMS_ERROR.getCode(), "文件不能为空");
            }

            // 验证文件大小（5MB）
            long maxSize = 5 * 1024 * 1024;
            if (file.getSize() > maxSize) {
                return Result.build(null, ResultCodeEnum.PARAMS_ERROR.getCode(), "文件大小不能超过5MB");
            }

            // 验证文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return Result.build(null, ResultCodeEnum.PARAMS_ERROR.getCode(), "只支持图片文件");
            }

            // 生成新文件名
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
            String newFilename = "avatar_" + userId + "_" + System.currentTimeMillis() + suffix;

            // 模拟文件保存
            // TODO: 实际应保存到文件服务器或云存储
            String filePath = "D:/uploads/avatars/" + newFilename;
            File dest = new File(filePath);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            // file.transferTo(dest); // 实际应用中取消注释

            // 返回文件URL
            String fileUrl = "/upload/avatars/" + newFilename;
            Map<String, Object> result = new HashMap<>();
            result.put("fileUrl", fileUrl);
            result.put("filename", newFilename);
            result.put("size", file.getSize());
            result.put("uploadTime", LocalDateTime.now());

            return Result.build(result, ResultCodeEnum.SUCCESS.getCode(), "上传成功");
        } catch (Exception e) {
            log.error("上传头像失败，userId: {}", userId, e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "上传头像失败");
        }
    }

    @Override
    public Result<?> getMyClubs(Long userId, Integer pageNum, Integer pageSize) {
        try {
            // TODO: 实现我的社团列表查询逻辑
            log.info("获取我的社团列表，userId: {}, pageNum: {}, pageSize: {}", userId, pageNum, pageSize);

            // 验证分页参数
            if (pageNum < 1) pageNum = 1;
            if (pageSize < 1 || pageSize > 100) pageSize = 10;

            // 构建模拟数据
            Map<String, Object> result = new HashMap<>();
            List<Map<String, Object>> clubs = new ArrayList<>();

            Map<String, Object> club1 = new HashMap<>();
            club1.put("id", 1L);
            club1.put("name", "计算机协会");
            club1.put("logo", "/upload/logos/club1.jpg");
            club1.put("description", "致力于计算机技术交流与学习的学生社团");
            club1.put("joinTime", LocalDateTime.now().minusMonths(1));
            club1.put("role", "成员");
            club1.put("status", "active");
            clubs.add(club1);

            Map<String, Object> club2 = new HashMap<>();
            club2.put("id", 2L);
            club2.put("name", "摄影爱好者协会");
            club2.put("logo", "/upload/logos/club2.jpg");
            club2.put("description", "热爱摄影，记录美好生活的社团");
            club2.put("joinTime", LocalDateTime.now().minusMonths(2));
            club2.put("role", "技术部部长");
            club2.put("status", "active");
            clubs.add(club2);

            result.put("list", clubs);
            result.put("total", 2);
            result.put("pageNum", pageNum);
            result.put("pageSize", pageSize);
            result.put("totalPage", 1);

            return Result.build(result, ResultCodeEnum.SUCCESS.getCode(), "获取成功");
        } catch (Exception e) {
            log.error("获取我的社团列表失败，userId: {}, pageNum: {}, pageSize: {}", userId, pageNum, pageSize, e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "获取我的社团列表失败");
        }
    }
}