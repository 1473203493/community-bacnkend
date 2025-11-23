package com.club.service;

import com.club.entity.vo.Result;

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
}