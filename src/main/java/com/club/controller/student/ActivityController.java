package com.club.controller.student;

import com.club.entity.vo.Result;
import com.club.entity.vo.ResultCodeEnum;
import com.club.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 活动控制器
 * @author zyh
 * @date 2025/11/11
 */
@RestController
@RequestMapping("/api/activity")
@Tag(name = "活动模块", description = "活动相关接口")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    /**
     * 查询活动列表
     * @param page 页码
     * @param size 每页数量
     * @param type 活动类型（可选）
     * @param keyword 关键词搜索（可选）
     * @return 活动列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询活动列表", description = "分页查询活动列表，支持类型筛选和关键词搜索")
    public Result<?> getActivityList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword) {
        try {
            return activityService.getActivityListForStudent(page, size, type, keyword);
        } catch (Exception e) {
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "查询活动列表失败");
        }
    }

    /**
     * 获取活动详情
     * @param activityId 活动ID
     * @return 活动详情
     */
    @GetMapping("/detail/{activityId}")
    @Operation(summary = "获取活动详情", description = "根据活动ID获取活动详细信息")
    public Result<?> getActivityDetail(@PathVariable Long activityId) {
        try {
            return activityService.getActivityDetailForStudent(activityId);
        } catch (Exception e) {
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "获取活动详情失败");
        }
    }

    /**
     * 报名参加活动
     * @param activityId 活动ID
     * @param request 请求对象，用于获取当前用户信息
     * @return 操作结果
     */
    @PostMapping("/sign-up")
    @Operation(summary = "报名参加活动", description = "用户报名参加指定活动")
    public Result<?> signUpActivity(@RequestParam Long activityId, HttpServletRequest request) {
        try {
            // 从请求中获取当前用户ID
            Long userId = Long.valueOf(request.getAttribute("userId").toString());
            return activityService.signUpActivity(activityId, userId);
        } catch (Exception e) {
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "活动报名失败");
        }
    }

    /**
     * 取消活动报名
     * @param activityId 活动ID
     * @param request 请求对象，用于获取当前用户信息
     * @return 操作结果
     */
    @PostMapping("/cancel-sign-up")
    @Operation(summary = "取消活动报名", description = "用户取消已报名的活动")
    public Result<?> cancelSignUpActivity(@RequestParam Long activityId, HttpServletRequest request) {
        try {
            // 从请求中获取当前用户ID
            Long userId = Long.valueOf(request.getAttribute("userId").toString());
            return activityService.cancelSignUpActivity(activityId, userId);
        } catch (Exception e) {
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "取消活动报名失败");
        }
    }

    /**
     * 查询我参加的活动
     * @param request 请求对象，用于获取当前用户信息
     * @return 参加的活动列表
     */
    @GetMapping("/my")
    @Operation(summary = "查询我参加的活动", description = "获取当前用户已报名参加的活动列表")
    public Result<?> getMyActivities(HttpServletRequest request) {
        try {
            // 从请求中获取当前用户ID
            Long userId = Long.valueOf(request.getAttribute("userId").toString());
            return activityService.getMyActivities(userId);
        } catch (Exception e) {
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "查询参加的活动失败");
        }
    }
}