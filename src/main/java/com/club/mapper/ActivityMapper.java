package com.club.mapper;

import com.club.entity.Activity;
import com.club.entity.request.ActivityQueryDto;
import com.club.entity.vo.ActivityVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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

    // 学生端查询活动列表
    List<Map<String, Object>> selectActivityListForStudent(@Param("params") Map<String, Object> params);

    // 学生端查询活动总数
    int countActivityListForStudent(@Param("params") Map<String, Object> params);

    // 学生端查询活动详情
    Map<String, Object> selectActivityDetailForStudent(@Param("activityId") Long activityId);

    /**
     * 查询社团最近的N个活动
     */
    List<Map<String, Object>> selectRecentActivitiesByClubId(@Param("clubId") Long clubId, @Param("limit") Integer limit);

    /**
     * 管理员查询活动列表
     */
    // 在 ActivityMapper.java 中更新方法签名
    List<ActivityVO> selectActivityListForAdmin(@Param("queryDto") ActivityQueryDto queryDto, @Param("offset") int offset);


    /**
     * 统计符合条件的活动数量
     */
    int countActivityListForAdmin(@Param("queryDto") ActivityQueryDto queryDto);
}