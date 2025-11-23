package com.club.service;

import com.club.entity.Activity;
import com.club.entity.vo.ActivityCreateRequestVO;
import com.club.entity.vo.Result;

import java.util.List;

/**
 * 活动服务接口
 * @author zyh
 * @date 2025/11/11
 */
public interface ActivityService {
    /**
     * 创建活动
     * @param request 活动创建请求
     * @return 操作结果
     */
    Result<Void> createActivity(ActivityCreateRequestVO request);

    /**
     * 获取社团活动列表
     * @param clubId 社团ID
     * @return 活动列表
     */
    Result<List<Activity>> listActivitiesByClub(Integer clubId);

    /**
     * 获取活动列表（学生端）
     * @param page 页码
     * @param size 每页数量
     * @param type 活动类型
     * @param keyword 关键词搜索
     * @return 活动列表
     */
    Result<?> getActivityListForStudent(Integer page, Integer size, String type, String keyword);

    /**
     * 获取活动详情（学生端）
     * @param activityId 活动ID
     * @return 活动详情
     */
    Result<?> getActivityDetailForStudent(Long activityId);

    /**
     * 报名参加活动
     * @param activityId 活动ID
     * @param userId 用户ID
     * @return 操作结果
     */
    Result<?> signUpActivity(Long activityId, Long userId);

    /**
     * 取消活动报名
     * @param activityId 活动ID
     * @param userId 用户ID
     * @return 操作结果
     */
    Result<?> cancelSignUpActivity(Long activityId, Long userId);

    /**
     * 获取用户参加的活动列表
     * @param userId 用户ID
     * @return 参加的活动列表
     */
    Result<?> getMyActivities(Long userId);
}