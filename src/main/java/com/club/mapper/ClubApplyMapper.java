package com.club.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 入社申请Mapper接口
 *
 * @author zyh
 * @date 2025/11/11
 */
@Mapper
public interface ClubApplyMapper {

    /**
     * 根据用户ID查询入社申请列表
     *
     * @param userId 用户ID
     * @return 入社申请列表
     */
    List<Map<String, Object>> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据申请ID和用户ID查询申请信息
     *
     * @param applyId 申请ID
     * @param userId 用户ID
     * @return 申请信息
     */
    Map<String, Object> selectByIdAndUserId(@Param("applyId") Long applyId, @Param("userId") Long userId);

    /**
     * 更新申请状态
     *
     * @param updateMap 更新参数
     * @return 更新结果
     */
    int updateStatus(Map<String, Object> updateMap);

    /**
     * 根据用户ID和社团ID统计申请数量
     *
     * @param userId 用户ID
     * @param clubId 社团ID
     * @return 申请数量
     */
    Integer countByUserIdAndClubId(@Param("userId") Long userId, @Param("clubId") Long clubId);

    /**
     * 插入入社申请记录
     *
     * @param applyData 申请数据
     * @return 插入结果
     */
    int insertClubApply(Map<String, Object> applyData);
}