package com.club.controller.student;

import com.club.entity.vo.Result;
import com.club.entity.vo.ResultCodeEnum;
import com.club.service.ClubApplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 入社申请控制器
 * @author zyh
 * @date 2025/11/11
 */
@RestController
@RequestMapping("/api/club/apply")
@Tag(name = "入社申请", description = "入社申请相关接口")
public class ClubApplyController {

    @Autowired
    private ClubApplyService clubApplyService;

    /**
     * 查询自己的入社申请列表
     * @param request 请求对象，用于获取当前用户信息
     * @return 入社申请列表
     */
    @GetMapping("/my")
    @Operation(summary = "查询自己的入社申请列表", description = "获取当前用户的所有入社申请记录")
    public Result<?> getMyClubApplies(HttpServletRequest request) {
        try {
            // 从请求中获取当前用户ID
            Long userId = Long.valueOf(request.getAttribute("userId").toString());
            return clubApplyService.getMyClubApplies(userId);
        } catch (Exception e) {
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "查询入社申请列表失败");
        }
    }

    /**
     * 撤销入社申请
     * @param applyId 申请ID
     * @param request 请求对象，用于获取当前用户信息
     * @return 操作结果
     */
    @PostMapping("/cancel")
    @Operation(summary = "撤销入社申请", description = "撤销指定的入社申请")
    public Result<?> cancelClubApply(@RequestParam Long applyId, HttpServletRequest request) {
        try {
            // 从请求中获取当前用户ID
            Long userId = Long.valueOf(request.getAttribute("userId").toString());
            return clubApplyService.cancelClubApply(applyId, userId);
        } catch (Exception e) {
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "撤销入社申请失败");
        }
    }
}