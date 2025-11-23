package com.club.controller.student;

import com.club.entity.vo.Result;
import com.club.entity.vo.ResultCodeEnum;
import com.club.service.ClubCreateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 社团创建控制器
 * @author zyh
 * @date 2025/11/11
 */
@RestController
@RequestMapping("/api/club/create")
@Tag(name = "社团创建", description = "社团创建相关接口")
public class ClubCreateController {

    @Autowired
    private ClubCreateService clubCreateService;

    /**
     * 提交社团创建申请
     * @param name 社团名称
     * @param category 社团类别
     * @param purpose 社团宗旨
     * @param charter 社团章程
     * @param attachment 负责人证明附件
     * @param request 请求对象，用于获取当前用户信息
     * @return 操作结果
     */
    @PostMapping
    @Operation(summary = "提交社团创建申请", description = "提交新社团的创建申请")
    public Result<?> submitClubCreateRequest(
            @RequestParam String name,
            @RequestParam String category,
            @RequestParam String purpose,
            @RequestParam String charter,
            @RequestParam(required = false) MultipartFile attachment,
            HttpServletRequest request) {
        try {
            // 从请求中获取当前用户ID
            Long userId = Long.valueOf(request.getAttribute("userId").toString());

            // 构建请求参数
            Map<String, Object> params = new HashMap<>();
            params.put("name", name);
            params.put("category", category);
            params.put("purpose", purpose);
            params.put("charter", charter);
            params.put("userId", userId);

            return clubCreateService.submitClubCreateRequest(params, attachment);
        } catch (Exception e) {
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "提交社团创建申请失败");
        }
    }

    /**
     * 查询我发起的社团申请
     * @param request 请求对象，用于获取当前用户信息
     * @return 社团申请列表
     */
    @GetMapping("/my")
    @Operation(summary = "查询我发起的社团申请", description = "获取当前用户发起的所有社团创建申请")
    public Result<?> getMyClubCreateRequests(HttpServletRequest request) {
        try {
            // 从请求中获取当前用户ID
            Long userId = Long.valueOf(request.getAttribute("userId").toString());
            return clubCreateService.getMyClubCreateRequests(userId);
        } catch (Exception e) {
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "查询社团申请失败");
        }
    }
}