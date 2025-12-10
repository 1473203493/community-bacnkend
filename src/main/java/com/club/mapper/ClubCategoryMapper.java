package com.club.mapper;

import com.club.entity.ClubCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
    List<ClubCategory> selectAllByAdmin();

    /**
     * 根据ID查询分类
     */
    ClubCategory selectByIdByAdmin(@Param("categoryId") Integer categoryId);

    /**
     * 新增分类
     */
    int insertByAdmin(ClubCategory clubCategory);

    /**
     * 更新分类
     */
    int updateByAdmin(ClubCategory clubCategory);

    /**
     * 删除分类
     */
    int deleteByIdByAdmin(@Param("categoryId") Integer categoryId);

    /**
     * 检查分类是否已被社团使用
     */
    int countClubsByCategoryIdByAdmin(@Param("categoryId") Integer categoryId);

    /**
     * 检查分类名称是否已存在
     */
    int countByNameByAdmin(@Param("name") String name, @Param("excludeId") Integer excludeId);
}