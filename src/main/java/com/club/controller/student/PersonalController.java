package com.club.controller.student;

import com.club.entity.vo.Result;
import com.club.service.PersonalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 学生端个人中心控制器
 */
@RestController
@RequestMapping("/api/personal")
@Tag(name = "学生端个人中心")
@Slf4j
public class PersonalController {

    @Autowired
    private PersonalService personalService;

    /**
     * 获取个人信息
     *
     * @param request HTTP请求
     * @return 个人信息
     */
    @GetMapping("/info")
    @Operation(summary = "获取个人信息")
    public Result<?> getPersonalInfo(HttpServletRequest request) {
        try {
            // 从请求中获取用户ID（实际应用中可能从Token或Session中获取）
            Long userId = 1L; // 临时硬编码，实际应从认证上下文获取
            log.info("获取个人信息，userId: {}", userId);
            return personalService.getPersonalInfo(userId);
        } catch (Exception e) {
            log.error("获取个人信息失败", e);
            return Result.fail("获取个人信息失败");
        }
    }

    /**
     * 更新个人信息
     *
     * @param request HTTP请求
     * @param params 更新参数
     * @return 更新结果
     */
    @PutMapping("/info")
    @Operation(summary = "更新个人信息")
    public Result<?> updatePersonalInfo(HttpServletRequest request,
                                        @RequestBody Map<String, Object> params) {
        try {
            // 从请求中获取用户ID
            Long userId = 1L; // 临时硬编码
            log.info("更新个人信息，userId: {}, params: {}", userId, params);
            return personalService.updatePersonalInfo(userId, params);
        } catch (Exception e) {
            log.error("更新个人信息失败", e);
            return Result.fail("更新个人信息失败");
        }
    }

    /**
     * 修改密码
     *
     * @param request HTTP请求
     * @param params 密码参数
     * @return 修改结果
     */
    @PutMapping("/password")
    @Operation(summary = "修改密码")
    public Result<?> changePassword(HttpServletRequest request,
                                    @RequestBody Map<String, String> params) {
        try {
            // 从请求中获取用户ID
            Long userId = 1L; // 临时硬编码
            String oldPassword = params.get("oldPassword");
            String newPassword = params.get("newPassword");
            log.info("修改密码，userId: {}", userId);
            return personalService.changePassword(userId, oldPassword, newPassword);
        } catch (Exception e) {
            log.error("修改密码失败", e);
            return Result.fail("修改密码失败");
        }
    }

    /**
     * 上传头像
     *
     * @param request HTTP请求
     * @param file 头像文件
     * @return 上传结果
     */
    @PostMapping("/avatar")
    @Operation(summary = "上传头像")
    public Result<?> uploadAvatar(HttpServletRequest request,
                                  @Parameter(description = "头像文件") @RequestParam("file") MultipartFile file) {
        try {
            // 从请求中获取用户ID
            Long userId = 1L; // 临时硬编码
            log.info("上传头像，userId: {}, fileName: {}", userId, file.getOriginalFilename());
            return personalService.uploadAvatar(userId, file);
        } catch (Exception e) {
            log.error("上传头像失败", e);
            return Result.fail("上传头像失败");
        }
    }

    /**
     * 获取我的社团列表
     *
     * @param request HTTP请求
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 社团列表
     */
    @GetMapping("/clubs")
    @Operation(summary = "获取我的社团列表")
    public Result<?> getMyClubs(HttpServletRequest request,
                                @RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            // 从请求中获取用户ID
            Long userId = 1L; // 临时硬编码
            log.info("获取我的社团列表，userId: {}, pageNum: {}, pageSize: {}", userId, pageNum, pageSize);
            return personalService.getMyClubs(userId, pageNum, pageSize);
        } catch (Exception e) {
            log.error("获取我的社团列表失败", e);
            return Result.fail("获取我的社团列表失败");
        }
    }
}