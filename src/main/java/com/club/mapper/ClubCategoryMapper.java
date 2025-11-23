package com.club.mapper;

import com.club.entity.ClubCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 社团分类Mapper接口
 *
 * @author zyh
 * @date 2025/11/11
 */
@Mapper
public interface ClubCategoryMapper {
    /**
     * 查询所有社团分类
     *
     * @return 社团分类列表
     */
    List<ClubCategory> selectAll();
}