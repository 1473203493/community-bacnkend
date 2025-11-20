package com.club.mapper;

import com.club.entity.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ActivityMapper {
    /**
     * 搜索活动
     */
    List<?> searchActivities(@Param("keyword") String keyword,
                             @Param("categoryId") Integer categoryId);
    // 新增活动
    int insertActivity(Activity activity);

    /**
     * 查询某个社团的所有活动
     */
    List<Activity> selectByClubId(@Param("clubId") Integer clubId);

    // 查询单条活动
    Activity selectById(@Param("activityId") Integer activityId);


    // 更新活动状态（例如关闭报名）
    int updateActivityStatus(@Param("activityId") Integer activityId,
                             @Param("status") String status);
}