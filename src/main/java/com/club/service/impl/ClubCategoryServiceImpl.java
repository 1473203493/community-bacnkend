package com.club.service.impl;

import com.club.entity.ClubCategory;
import com.club.entity.vo.Result;
import com.club.entity.vo.ResultCodeEnum;
import com.club.mapper.ClubCategoryMapper;
import com.club.service.ClubCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 社团分类服务实现类
 * @author zyh
 * @date 2025/11/11
 */
@Service
public class ClubCategoryServiceImpl implements ClubCategoryService {

    private static final Logger logger = LoggerFactory.getLogger(ClubCategoryServiceImpl.class);

    @Autowired
    private ClubCategoryMapper clubCategoryMapper;

    @Override
    public Result<?> getClubCategories() {
        try {
            logger.info("开始查询社团分类列表");
            List<?> categories = clubCategoryMapper.selectAllByAdmin();
            logger.info("查询社团分类列表成功，共{}条记录", categories.size());
            return Result.build(categories, ResultCodeEnum.SUCCESS.getCode(), "查询成功");
        } catch (Exception e) {
            logger.error("查询社团分类列表失败", e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "查询失败");
        }
    }


    @Override
    public Result<ClubCategory> getCategoryById(Integer categoryId) {
        if (categoryId == null || categoryId <= 0) {
            return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "参数错误：分类ID不能为空且需大于0");
        }

        try {
            ClubCategory category = clubCategoryMapper.selectByIdByAdmin(categoryId);
            if (category == null) {
                return Result.build(null, ResultCodeEnum.BUSINESS_ERROR.getCode(), "分类不存在");
            }
            return Result.build(category, ResultCodeEnum.SUCCESS.getCode(), "查询成功");
        } catch (Exception e) {
            // 移除 log 日志输出
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "查询分类详情失败");
        }
    }

    @Override
    public Result<Void> addCategory(ClubCategory category) {
        if (category == null || category.getName() == null || category.getName().trim().isEmpty()) {
            return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "分类名称不能为空");
        }
        if (category.getOrderNo() == null || category.getOrderNo() < 0) {
            return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "排序序号不能为负数");
        }

        try {
            int count = clubCategoryMapper.countByNameByAdmin(category.getName(), null);
            if (count > 0) {
                return Result.build(null, ResultCodeEnum.BUSINESS_ERROR.getCode(), "分类名称已存在");
            }

            clubCategoryMapper.insertByAdmin(category);
            return Result.build(null, ResultCodeEnum.SUCCESS.getCode(), "新增分类成功");
        } catch (Exception e) {
            // 移除 log 日志输出
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "新增分类失败");
        }
    }

    @Override
    public Result<Void> updateCategory(ClubCategory category) {
        if (category == null || category.getCategoryId() == null || category.getCategoryId() <= 0) {
            return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "分类ID不能为空且需大于0");
        }
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "分类名称不能为空");
        }
        if (category.getOrderNo() == null || category.getOrderNo() < 0) {
            return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "排序序号不能为负数");
        }

        try {
            ClubCategory existCategory = clubCategoryMapper.selectByIdByAdmin(category.getCategoryId());
            if (existCategory == null) {
                return Result.build(null, ResultCodeEnum.BUSINESS_ERROR.getCode(), "分类不存在");
            }

            int count = clubCategoryMapper.countByNameByAdmin(category.getName(), category.getCategoryId());
            if (count > 0) {
                return Result.build(null, ResultCodeEnum.BUSINESS_ERROR.getCode(), "分类名称已存在");
            }

            clubCategoryMapper.updateByAdmin(category);
            return Result.build(null, ResultCodeEnum.SUCCESS.getCode(), "更新分类成功");
        } catch (Exception e) {
            // 移除 log 日志输出
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "更新分类失败");
        }
    }

    @Override
    public Result<Void> deleteCategory(Integer categoryId) {
        if (categoryId == null || categoryId <= 0) {
            return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "参数错误：分类ID不能为空且需大于0");
        }

        try {
            ClubCategory category = clubCategoryMapper.selectByIdByAdmin(categoryId);
            if (category == null) {
                return Result.build(null, ResultCodeEnum.BUSINESS_ERROR.getCode(), "分类不存在");
            }

            int clubCount = clubCategoryMapper.countClubsByCategoryIdByAdmin(categoryId);
            if (clubCount > 0) {
                return Result.build(null, ResultCodeEnum.BUSINESS_ERROR.getCode(), "该分类已关联社团，不能删除");
            }

            clubCategoryMapper.deleteByIdByAdmin(categoryId);
            return Result.build(null, ResultCodeEnum.SUCCESS.getCode(), "删除分类成功");
        } catch (Exception e) {
            // 移除 log 日志输出
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "删除分类失败");
        }
    }

    @Override
    public Result<List<ClubCategory>> getAllCategories() {
        try {
            List<ClubCategory> categories = clubCategoryMapper.selectAllByAdmin();
            return Result.build(categories, ResultCodeEnum.SUCCESS.getCode(), "查询成功");
        } catch (Exception e) {
            // 移除 log 日志输出
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "查询分类列表失败");
        }
    }
}