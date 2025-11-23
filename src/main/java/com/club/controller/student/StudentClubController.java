package com.club.controller.student;

import com.club.entity.vo.Result;
import com.club.entity.vo.ResultCodeEnum;
import com.club.service.ClubService;
import com.club.util.AuthContextUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 学生端社团控制器
 * @author zyh
 * @date 2025/11/11
 */
@RestController
@RequestMapping("/api/club")
@Tag(name = "社团模块", description = "社团相关接口")
public class StudentClubController {

    @Autowired
    private ClubService clubService;

    /**
     * 查询社团列表
     * 查询条件：名称、类别、排序（热门/最新）
     * @param name 社团名称（可选）
     * @param category 社团类别（可选）
     * @param sort 排序方式：hot（热门）、newest（最新）
     * @param page 当前页码
     * @param limit 每页数量
     * @return 社团列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询社团列表", description = "根据条件查询社团列表")
    public Result<?> getClubList(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "newest") String sort,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) {
        try {
            // 参数验证
            if (page < 1) {
                page = 1;
            }
            if (limit < 1 || limit > 100) {
                limit = 10;
            }

            // 调用服务层获取社团列表
            Map<String, Object> params = Map.of(
                    "name", name,
                    "category", category,
                    "sort", sort,
                    "page", page,
                    "limit", limit
            );
            Map<String, Object> result = clubService.getClubListForStudent(params);
            return Result.build(result, ResultCodeEnum.SUCCESS.getCode(), "查询成功");
        } catch (Exception e) {
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "查询社团列表失败");
        }
    }

    /**
     * 社团详情
     * 返回：基础信息、负责人信息、成员前5、最近活动
     * @param clubId 社团ID
     * @return 社团详情
     */
    @GetMapping("/detail/{clubId}")
    @Operation(summary = "获取社团详情", description = "获取社团详细信息")
    public Result<?> getClubDetail(@PathVariable Long clubId) {
        try {
            // 参数验证
            if (clubId == null || clubId <= 0) {
                return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "社团ID无效");
            }

            // 获取当前用户ID（可能为null，表示未登录用户）
            Long userId = null;
            try {
                userId = AuthContextUtil.getCurrentUserId();
            } catch (Exception e) {
                // 未登录用户也可以查看社团详情
            }

            // 调用服务层获取社团详情
            Map<String, Object> clubDetail = clubService.getClubDetailForStudent(clubId, userId);
            return Result.build(clubDetail, ResultCodeEnum.SUCCESS.getCode(), "查询成功");
        } catch (Exception e) {
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "查询社团详情失败");
        }
    }

    /**
     * 申请加入社团
     * @param clubId 社团ID
     * @param remark 申请备注
     * @return 申请结果
     */
    @PostMapping("/apply")
    @Operation(summary = "申请加入社团", description = "提交加入社团的申请")
    public Result<String> applyToJoinClub(@RequestParam Long clubId, @RequestParam(required = false) String remark) {
        try {
            // 参数验证
            if (clubId == null || clubId <= 0) {
                return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "社团ID无效");
            }

            // 获取当前用户ID
            Long userId = AuthContextUtil.getCurrentUserId();

            // 调用服务层处理申请
            clubService.applyToJoinClub(userId, clubId, remark);
            return Result.build(null, ResultCodeEnum.SUCCESS.getCode(), "申请提交成功，请等待审核");
        } catch (Exception e) {
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "申请提交失败");
        }
    }

    /**
     * 查看本人加入的社团
     * @return 用户加入的社团列表
     */
    @GetMapping("/my")
    @Operation(summary = "查看本人加入的社团", description = "查看当前用户已加入的社团列表")
    public Result<?> getMyClubs() {
        try {
            // 获取当前用户ID
            Long userId = AuthContextUtil.getCurrentUserId();

            // 调用服务层获取用户加入的社团列表
            Map<String, Object> result = clubService.getMyClubs(userId);
            return Result.build(result, ResultCodeEnum.SUCCESS.getCode(), "查询成功");
        } catch (Exception e) {
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "查询我的社团失败");
        }
    }
}