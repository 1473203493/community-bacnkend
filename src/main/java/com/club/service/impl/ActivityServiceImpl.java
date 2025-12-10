package com.club.service.impl;

import com.club.entity.Activity;
import com.club.entity.Club;
import com.club.entity.ClubMember;
import com.club.entity.request.ActivityQueryDto;
import com.club.entity.vo.ActivityCreateRequestVO;
import com.club.entity.vo.ActivityVO;
import com.club.entity.vo.Result;
import com.club.entity.vo.ResultCodeEnum;
import com.club.mapper.ActivityMapper;
import com.club.mapper.ActivitySignupMapper;
import com.club.mapper.ClubMapper;
import com.club.mapper.ClubMemberMapper;
import com.club.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 活动服务实现类
 * @author zyh
 * @date 2025/11/11
 */

@Slf4j
@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private ClubMapper clubMapper;

    @Autowired
    private ClubMemberMapper clubMemberMapper;

    @Autowired
    private ActivitySignupMapper activitySignupMapper;


    @Override
    public Result<Void> createActivity(ActivityCreateRequestVO request) {
        try {
            // 1️⃣ 检查社团是否存在
            Club club = clubMapper.selectById(request.getClubId());
            if (club == null) {
                return Result.build(null, 400, "社团不存在");
            }
            // 2️⃣ 检查社团状态是否正常（2 = 正常）
            if (!"2".equals(club.getStatus())) {
                return Result.build(null, 403, "当前社团状态异常，无法创建活动");
            }

            // 3 检查创建人是否是该社团管理员或领导
            ClubMember member = clubMemberMapper.selectMemberByUserIdAndClubId(request.getClubId(),
                    request.getCreatorId());
            if (member != null) {
                log.info("创建人 userId={} 的角色 role={}", request.getCreatorId(), member.getRole());
            } else {
                log.info("创建人 userId={} 不是社团成员", request.getCreatorId());
            }

            if (member == null || (!"2".equals(member.getRole()) && !"3".equals(member.getRole()))) {
                return Result.build(null, 403, "无权限发布活动");
            }

            // 3️⃣ 构建活动实体
            Activity activity = new Activity();
            activity.setClubId(request.getClubId());
            activity.setTitle(request.getTitle());
            activity.setDescription(request.getDescription());
            activity.setLocation(request.getLocation());
            activity.setTime(request.getTime());
            activity.setQuota(request.getQuota() != null ? request.getQuota() : 0);
            activity.setNeedAudit(request.getNeedAudit() != null ? request.getNeedAudit() : false);
            activity.setStatus("1"); // 待平台管理员确认
            activity.setCreatedAt(LocalDateTime.now());
            activity.setStartTime(request.getStartTime());
            activity.setEndTime(request.getEndTime());

            // 4️⃣ 插入数据库
            int rows = activityMapper.insertActivity(activity);
            if (rows > 0) {
                return Result.build(null, 200, "活动创建成功，等待管理员审核");
            } else {
                return Result.build(null, 500, "活动创建失败");
            }

        } catch (Exception e) {
            log.error("新增活动异常", e);
            return Result.build(null, 500, "新增活动失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<Activity>> listActivitiesByClub(Integer clubId) {
        Club club = clubMapper.selectById(clubId);
        if (club == null) {
            return Result.build(null, 404, "社团不存在");
        }

        List<Activity> activities = activityMapper.selectByClubId(clubId);
        return Result.build(activities, 200, "查询成功");
    }

    @Override
    public Result<?> getActivityListForStudent(Integer page, Integer size, String type, String keyword) {
        try {
            log.info("查询活动列表，页码：{}, 每页数量：{}, 类型：{}, 关键词：{}", page, size, type, keyword);

            // 构建查询参数
            Map<String, Object> params = new HashMap<>();
            params.put("page", (page - 1) * size);
            params.put("size", size);
            params.put("type", type);
            params.put("keyword", keyword);

            // 查询活动列表
            List<Map<String, Object>> activities = activityMapper.selectActivityListForStudent(params);

            // 查询总数
            int total = activityMapper.countActivityListForStudent(params);

            // 构建分页结果
            Map<String, Object> result = new HashMap<>();
            result.put("list", activities);
            result.put("total", total);
            result.put("page", page);
            result.put("size", size);
            result.put("totalPages", (total + size - 1) / size);

            return Result.build(result, ResultCodeEnum.SUCCESS.getCode(), "查询成功");
        } catch (Exception e) {
            log.error("查询活动列表失败", e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "查询失败");
        }
    }

    @Override
    public Result<?> getActivityDetailForStudent(Long activityId) {
        try {
            log.info("获取活动详情，活动ID：{}", activityId);

            // 查询活动详情
            Map<String, Object> activity = activityMapper.selectActivityDetailForStudent(activityId);
            if (activity == null) {
                return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "活动不存在");
            }

            return Result.build(activity, ResultCodeEnum.SUCCESS.getCode(), "查询成功");
        } catch (Exception e) {
            log.error("获取活动详情失败，活动ID：{}", activityId, e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "查询失败");
        }
    }

    @Override
    public Result<?> signUpActivity(Long activityId, Long userId) {
        try {
            log.info("用户[{}]报名活动[{}]", userId, activityId);

            // 检查活动是否存在
            Map<String, Object> activity = activityMapper.selectActivityDetailForStudent(activityId);
            if (activity == null) {
                return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "活动不存在");
            }

            // 检查活动状态
            String status = activity.get("status") != null ? activity.get("status").toString() : "";
            if (!"2".equals(status)) {
                return Result.build(null, ResultCodeEnum.BUSINESS_ERROR.getCode(), "活动未审核通过或已结束");
            }

            // 检查报名时间是否在有效期内
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime signUpStartTime = (LocalDateTime) activity.get("signUpStartTime");
            LocalDateTime signUpEndTime = (LocalDateTime) activity.get("signUpEndTime");

            if (signUpStartTime != null && now.isBefore(signUpStartTime)) {
                return Result.build(null, ResultCodeEnum.BUSINESS_ERROR.getCode(), "报名尚未开始");
            }
            if (signUpEndTime != null && now.isAfter(signUpEndTime)) {
                return Result.build(null, ResultCodeEnum.BUSINESS_ERROR.getCode(), "报名已结束");
            }

            // 检查是否已报名
            int count = activitySignupMapper.countByActivityIdAndUserId(activityId, userId);
            if (count > 0) {
                return Result.build(null, ResultCodeEnum.BUSINESS_ERROR.getCode(), "您已报名该活动");
            }

            // 检查名额是否已满
            Integer quota = (Integer) activity.get("quota");
            if (quota != null && quota > 0) {
                int currentSignUpCount = activitySignupMapper.countByActivityId(activityId);
                if (currentSignUpCount >= quota) {
                    return Result.build(null, ResultCodeEnum.BUSINESS_ERROR.getCode(), "活动名额已满");
                }
            }

            // 插入报名记录
            Map<String, Object> signUpRecord = new HashMap<>();
            signUpRecord.put("activityId", activityId);
            signUpRecord.put("userId", userId);
            signUpRecord.put("status", "pending"); // 待审核
            signUpRecord.put("signUpTime", now);

            int result = activitySignupMapper.insert(signUpRecord);

            if (result > 0) {
                log.info("用户[{}]报名活动[{}]成功", userId, activityId);
                return Result.build(null, ResultCodeEnum.SUCCESS.getCode(), "报名成功");
            } else {
                log.warn("用户[{}]报名活动[{}]失败", userId, activityId);
                return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "报名失败，请稍后重试");
            }
        } catch (Exception e) {
            log.error("用户[{}]报名活动[{}]失败", userId, activityId, e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "报名失败，请稍后重试");
        }
    }

    @Override
    public Result<?> cancelSignUpActivity(Long activityId, Long userId) {
        try {
            log.info("用户[{}]取消活动[{}]报名", userId, activityId);

            // 检查活动是否存在
            Map<String, Object> activity = activityMapper.selectActivityDetailForStudent(activityId);
            if (activity == null) {
                return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "活动不存在");
            }

            // 检查报名记录
            Map<String, Object> signUp = activitySignupMapper.selectByActivityIdAndUserId(activityId, userId);
            if (signUp == null) {
                return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "您未报名该活动");
            }

            // 检查报名状态
            String status = signUp.get("status") != null ? signUp.get("status").toString() : "";
            if (!"pending".equals(status) && !"approved".equals(status)) {
                return Result.build(null, ResultCodeEnum.BUSINESS_ERROR.getCode(), "该报名状态不允许取消");
            }

            // 检查是否在允许取消的时间内
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime activityStartTime = (LocalDateTime) activity.get("startTime");
            if (activityStartTime != null && now.isAfter(activityStartTime)) {
                return Result.build(null, ResultCodeEnum.BUSINESS_ERROR.getCode(), "活动已开始，无法取消报名");
            }

            // 更新报名状态为已取消
            Map<String, Object> updateMap = new HashMap<>();
            updateMap.put("activityId", activityId);
            updateMap.put("userId", userId);
            updateMap.put("status", "cancelled");

            int result = activitySignupMapper.updateStatus(updateMap);

            if (result > 0) {
                log.info("用户[{}]取消活动[{}]报名成功", userId, activityId);
                return Result.build(null, ResultCodeEnum.SUCCESS.getCode(), "取消报名成功");
            } else {
                log.warn("用户[{}]取消活动[{}]报名失败", userId, activityId);
                return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "取消报名失败，请稍后重试");
            }
        } catch (Exception e) {
            log.error("用户[{}]取消活动[{}]报名失败", userId, activityId, e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "取消报名失败，请稍后重试");
        }
    }

    @Override
    public Result<?> getMyActivities(Long userId) {
        try {
            log.info("查询用户[{}]参加的活动列表", userId);

            // 查询用户参加的活动列表
            List<Map<String, Object>> activities = activitySignupMapper.selectUserActivities(userId);

            return Result.build(activities, ResultCodeEnum.SUCCESS.getCode(), "查询成功");
        } catch (Exception e) {
            log.error("查询用户[{}]参加的活动列表失败", userId, e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "查询失败，请稍后重试");
        }
    }

    @Override
    public Result<?> getActivityListForAdmin(ActivityQueryDto queryDto) {
        try {
            // 确保分页参数有效
            if (queryDto.getPageNum() == null || queryDto.getPageNum() < 1) {
                queryDto.setPageNum(1);
            }
            if (queryDto.getPageSize() == null || queryDto.getPageSize() < 1) {
                queryDto.setPageSize(10);
            }

            // 计算正确的偏移量
            int offset = (queryDto.getPageNum() - 1) * queryDto.getPageSize();

            // 查询数据
            List<ActivityVO> activities = activityMapper.selectActivityListForAdmin(queryDto, offset);
            int total = activityMapper.countActivityListForAdmin(queryDto);

            Map<String, Object> result = new HashMap<>();
            result.put("list", activities);
            result.put("total", total);
            result.put("pages", (total + queryDto.getPageSize() - 1) / queryDto.getPageSize());
            result.put("pageNum", queryDto.getPageNum());
            result.put("pageSize", queryDto.getPageSize());

            return Result.build(result, ResultCodeEnum.SUCCESS.getCode(), "查询成功");
        } catch (Exception e) {
            log.error("查询活动列表失败", e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "查询失败");
        }
    }

}
