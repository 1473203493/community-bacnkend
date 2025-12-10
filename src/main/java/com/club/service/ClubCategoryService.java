package com.club.service;

import com.club.entity.ClubCategory;
import com.club.entity.vo.Result;

import java.util.List;

/**
 * 社团分类服务接口
 * @author zyh
 * @date 2025/11/11
 */
public interface ClubCategoryService {

    /**
     * 获取社团分类列表
     * @return 社团分类列表
     */
    Result<?> getClubCategories();

    /**
     * 根据ID获取分类
     */
    Result<ClubCategory> getCategoryById(Integer categoryId);

    /**
     * 新增分类
     */
    Result<Void> addCategory(ClubCategory category);

    /**
     * 更新分类
     */
    Result<Void> updateCategory(ClubCategory category);

    /**
     * 删除分类
     */
    Result<Void> deleteCategory(Integer categoryId);


    Result<List<ClubCategory>> getAllCategories();
}