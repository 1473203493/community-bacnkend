package com.club.service.impl;

import com.club.entity.Activity;
import com.club.entity.ActivitySignup;
import com.club.entity.ClubMember;
import com.club.entity.vo.ActivitySignupUserVO;
import com.club.entity.vo.ActivitySignupVO;
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
    public Result<List<ActivitySignupVO>> listSignups(Integer activityId, Integer operatorId) {
        Activity activity = activityMapper.selectById(activityId);
        if (activity == null) return Result.build(null, 404, "活动不存在");

        // 2️⃣ 检查操作者是否是该社团的管理员(2)或领导(3)
        ClubMember operator = clubMemberMapper.selectMemberByUserIdAndClubId(activity.getClubId(), operatorId);
        if (operator == null || (!"2".equals(operator.getRole()) && !"3".equals(operator.getRole()))) {
            return Result.build(null, 403, "无权限查看报名列表");
        }

        List<ActivitySignupVO> list = activitySignupMapper.selectSignupVOsByActivityId(activityId);
        return Result.build(list, 200, "查询成功");
    }

    @Override
    public Result<List<ActivitySignupVO>> listPendingSignups(Integer activityId, Integer operatorId) {
        Activity activity = activityMapper.selectById(activityId);
        if (activity == null) return Result.build(null, 404, "活动不存在");

        ClubMember operator = clubMemberMapper.selectMemberByUserIdAndClubId(activity.getClubId(), operatorId);
        if (operator == null || (!"2".equals(operator.getRole()) && !"3".equals(operator.getRole()))) {
            return Result.build(null, 403, "无权限查看未审核列表");
        }

        List<ActivitySignupVO> list = activitySignupMapper.selectPendingSignupVOs(activityId);
        return Result.build(list, 200, "查询成功");
    }

    @Override
    public Result<List<ActivitySignupVO>> listApprovedSignups(Integer activityId, Integer operatorId) {
        Activity activity = activityMapper.selectById(activityId);
        if (activity == null) return Result.build(null, 404, "活动不存在");

        ClubMember operator = clubMemberMapper.selectMemberByUserIdAndClubId(activity.getClubId(), operatorId);
        if (operator == null || (!"2".equals(operator.getRole()) && !"3".equals(operator.getRole()))) {
            return Result.build(null, 403, "无权限查看已通过列表");
        }

        List<ActivitySignupVO> list = activitySignupMapper.selectApprovedSignupVOs(activityId);
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
        ActivitySignup signup = activitySignupMapper.selectById(signupId);
        if (signup == null) return Result.build(null, 404, "报名记录不存在");

        // 2️⃣ 查询报名所属活动
        Activity activity = activityMapper.selectById(signup.getActivityId());
        if (activity == null) return Result.build(null, 404, "活动不存在");

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

    @Override
    public Result<List<ActivitySignupUserVO>> getSignupUsersForAdmin(Integer activityId) {
        // 1. 检查活动是否存在
        Activity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            return Result.build(null, 404, "活动不存在");
        }

        // 2. 查询该活动的所有报名用户信息
        List<ActivitySignupUserVO> signupUsers = activitySignupMapper.selectSignupUsersByActivityId(activityId);

        // 3. 对邮箱进行脱敏处理
        signupUsers.forEach(vo -> {
            if (vo.getMaskedEmail() != null && !vo.getMaskedEmail().isEmpty()) {
                vo.setMaskedEmail(maskEmail(vo.getMaskedEmail()));
            }
        });

        return Result.build(signupUsers, 200, "查询成功");
    }

    // 邮箱脱敏工具方法
    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        String[] parts = email.split("@");
        String username = parts[0];
        if (username.length() <= 3) {
            return username + "***@" + parts[1];
        }
        return username.substring(0, 3) + "***@" + parts[1];
    }
}
