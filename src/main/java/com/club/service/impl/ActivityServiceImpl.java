package com.club.service.impl;

import com.club.entity.Activity;
import com.club.entity.Club;
import com.club.entity.ClubMember;
import com.club.entity.vo.ActivityCreateRequestVO;
import com.club.entity.vo.Result;
import com.club.mapper.ActivityMapper;
import com.club.mapper.ClubMapper;
import com.club.mapper.ClubMemberMapper;
import com.club.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private ClubMapper clubMapper;

    @Autowired
    private ClubMemberMapper clubMemberMapper;

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
            ClubMember member = clubMemberMapper.selectMemberByUserIdAndClubId(request.getClubId(), request.getCreatorId());
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

}
