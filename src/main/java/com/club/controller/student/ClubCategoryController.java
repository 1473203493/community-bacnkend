package com.club.controller.student;

import com.club.entity.vo.Result;
import com.club.entity.vo.ResultCodeEnum;
import com.club.service.ClubCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 社团分类控制器
 * @author zyh
 * @date 2025/11/11
 */
@RestController
@RequestMapping("/api/club")
@Tag(name = "系统与公共", description = "系统公共接口")
public class ClubCategoryController {

    @Autowired
    private ClubCategoryService clubCategoryService;

    /**
     * 查询社团分类
     * @return 社团分类列表
     */
    @GetMapping("/category")
    @Operation(summary = "查询社团分类", description = "获取所有社团分类列表")
    public Result<?> getClubCategories() {
        try {
            // 调用服务层获取社团分类列表
            return clubCategoryService.getClubCategories();
        } catch (Exception e) {
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "查询社团分类失败");
        }
    }
}