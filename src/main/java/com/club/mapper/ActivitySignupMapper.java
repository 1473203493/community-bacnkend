package com.club.mapper;

import com.club.entity.ActivitySignup;
import com.club.entity.vo.ActivitySignupUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ActivitySignupMapper {

    // 查询某活动所有报名
    List<ActivitySignup> selectByActivityId(@Param("activityId") Integer activityId);

    // 查询单条报名
    ActivitySignup selectById(@Param("signupId") Integer signupId);

    // 更新报名审核状态
    int updateStatus(@Param("signupId") Integer signupId,
            @Param("status") String status,
            @Param("reason") String reason);

    // 统计某活动已通过报名人数
    int countApprovedByActivityId(@Param("activityId") Integer activityId);

    // 查询某活动未审核的报名
    List<ActivitySignup> selectPendingByActivityId(@Param("activityId") Integer activityId);

    // 查询某活动已通过的报名
    List<ActivitySignup> selectApprovedByActivityId(@Param("activityId") Integer activityId);

    /**
     * 根据活动ID和用户ID查询报名记录数量
     * @param activityId 活动ID
     * @param userId 用户ID
     * @return 记录数量
     */
    int countByActivityIdAndUserId(@Param("activityId") Long activityId, @Param("userId") Long userId);

    /**
     * 根据活动ID查询报名记录数量
     * @param activityId 活动ID
     * @return 记录数量
     */
    int countByActivityId(@Param("activityId") Long activityId);

    /**
     * 根据活动ID和用户ID查询报名记录
     * @param activityId 活动ID
     * @param userId 用户ID
     * @return 报名记录
     */
    Map<String, Object> selectByActivityIdAndUserId(@Param("activityId") Long activityId, @Param("userId") Long userId);

    /**
     * 插入报名记录
     * @param params 报名信息
     * @return 插入结果
     */
    int insert(Map<String, Object> params);

    /**
     * 更新报名状态
     * @param params 更新信息
     * @return 更新结果
     */
    int updateStatus(Map<String, Object> params);

    /**
     * 查询用户参加的活动列表
     * @param userId 用户ID
     * @return 活动列表
     */
    List<Map<String, Object>> selectUserActivities(@Param("userId") Long userId);

    // 添加方法
    List<ActivitySignupUserVO> selectSignupUsersByActivityId(@Param("activityId") Integer activityId);
}