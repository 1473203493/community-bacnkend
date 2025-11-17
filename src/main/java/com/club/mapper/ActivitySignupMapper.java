package com.club.mapper;

import com.club.entity.ActivitySignup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

}