package com.club.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 社团创建申请Mapper接口
 * @author zyh
 * @date 2025/11/11
 */
@Mapper
public interface ClubCreateMapper {

    /**
     * 统计用户未审核的社团创建申请数量
     * @param userId 用户ID
     * @return 未审核申请数量
     */
    int countPendingRequestsByUserId(Long userId);

    /**
     * 根据社团名称统计申请数量
     * @param name 社团名称
     * @return 申请数量
     */
    int countByName(String name);

    /**
     * 插入社团创建申请记录
     * @param map 申请信息
     * @return 插入成功的记录数
     */
    int insert(Map<String, Object> map);

    /**
     * 查询用户的社团创建申请列表
     * @param userId 用户ID
     * @return 申请列表
     */
    List<Map<String, Object>> selectByUserId(Long userId);
}