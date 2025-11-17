package com.club.service.impl;

import com.club.entity.Activity;
import com.club.entity.ActivitySignup;
import com.club.entity.ClubMember;
import com.club.entity.vo.Result;
import com.club.mapper.ActivityMapper;
import com.club.mapper.ActivitySignupMapper;
import com.club.mapper.ClubMemberMapper;
import com.club.service.ActivitySignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivitySignupServiceImpl implements ActivitySignupService {

    @Autowired
    private ActivitySignupMapper activitySignupMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private ClubMemberMapper clubMemberMapper;

    // 邮件服务，可选实现
    // @Autowired
    // private EmailService emailService;

    /**
     * 查看某个活动的所有报名记录
     * @param activityId 活动ID
     * @param operatorId 操作者 userId
     * @return 报名记录列表
     */
    @Override
    public Result<List<ActivitySignup>> listSignups(Integer activityId, Integer operatorId) {
        // 1️⃣ 查询活动是否存在
        Activity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            return Result.build(null, 404, "活动不存在");
        }

        // 2️⃣ 检查操作者是否是该社团的管理员(2)或领导(3)
        ClubMember operator = clubMemberMapper.selectMemberByUserIdAndClubId(activity.getClubId(), operatorId);
        if (operator == null || (!"2".equals(operator.getRole()) && !"3".equals(operator.getRole()))) {
            return Result.build(null, 403, "无权限查看报名列表");
        }

        // 3️⃣ 查询报名列表
        List<ActivitySignup> list = activitySignupMapper.selectByActivityId(activityId);
        return Result.build(list, 200, "查询成功");
    }

    @Override
    public Result<List<ActivitySignup>> listPendingSignups(Integer activityId, Integer operatorId) {
        // 查询活动是否存在
        Activity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            return Result.build(null, 404, "活动不存在");
        }

        // 检查操作者是否有权限
        ClubMember operator = clubMemberMapper.selectMemberByUserIdAndClubId(activity.getClubId(), operatorId);
        if (operator == null || (!"2".equals(operator.getRole()) && !"3".equals(operator.getRole()))) {
            return Result.build(null, 403, "无权限查看报名列表");
        }

        // 查询未审核报名 status=1
        List<ActivitySignup> list = activitySignupMapper.selectPendingByActivityId(activityId);
        return Result.build(list, 200, "查询成功");
    }

    @Override
    public Result<List<ActivitySignup>> listApprovedSignups(Integer activityId, Integer operatorId) {
        // 查询活动是否存在
        Activity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            return Result.build(null, 404, "活动不存在");
        }

        // 检查操作者是否有权限
        ClubMember operator = clubMemberMapper.selectMemberByUserIdAndClubId(activity.getClubId(), operatorId);
        if (operator == null || (!"2".equals(operator.getRole()) && !"3".equals(operator.getRole()))) {
            return Result.build(null, 403, "无权限查看报名列表");
        }

        // 查询已通过报名 status=2
        List<ActivitySignup> list = activitySignupMapper.selectApprovedByActivityId(activityId);
        return Result.build(list, 200, "查询成功");
    }


    /**
     * 审核单条报名记录
     * @param signupId 报名记录ID
     * @param operatorId 操作者 userId
     * @param status 审核状态 1=通过 2=拒绝
     * @param reason 拒绝理由（status=2时必填）
     * @return 审核结果
     */
    @Override
    public Result<Void> auditSignup(Integer signupId, Integer operatorId, String status, String reason) {
        // 1️⃣ 查询报名记录是否存在
        ActivitySignup signup = activitySignupMapper.selectById(signupId);
        if (signup == null) {
            return Result.build(null, 404, "报名记录不存在");
        }

        // 2️⃣ 查询报名所属活动
        Activity activity = activityMapper.selectById(signup.getActivityId());
        if (activity == null) {
            return Result.build(null, 404, "活动不存在");
        }

        // 3️⃣ 权限验证：操作者必须是副部长(2)或部长(3)
        ClubMember operator = clubMemberMapper.selectMemberByUserIdAndClubId(activity.getClubId(), operatorId);
        if (operator == null || (!"2".equals(operator.getRole()) && !"3".equals(operator.getRole()))) {
            return Result.build(null, 403, "无权限审核");
        }

        // 4️⃣ 拒绝时必须填写拒绝理由
        if ("3".equals(status) && (reason == null || reason.trim().isEmpty())) {
            return Result.build(null, 400, "拒绝理由不能为空");
        }

        // 5️⃣ 更新报名记录的审核状态和理由
        int rows = activitySignupMapper.updateStatus(signupId, status, reason);
        if (rows <= 0) {
            return Result.build(null, 500, "审核失败");
        }

        // 6️⃣ 审核通过后更新活动已报名人数
        if ("2".equals(status)) { // 2 = 通过
            int approvedCount = activitySignupMapper.countApprovedByActivityId(activity.getActivityId());
            if (activity.getQuota() != null && approvedCount >= activity.getQuota()) {
                // 达到上限，关闭报名通道
                activityMapper.updateActivityStatus(activity.getActivityId(), "0"); // 0 表示报名关闭
            }
        }

        // 7️⃣ 发送邮件通知给报名者（可选实现）
        // emailService.sendAuditResult(signup.getUserId(), activity, status, reason);

        return Result.build(null, 200, "审核成功");
    }
}
