package com.club.service;

import com.club.entity.ActivitySignup;
import com.club.entity.vo.ActivitySignupVO;
import com.club.entity.vo.ActivitySignupUserVO;
import com.club.entity.vo.Result;

import java.util.List;

public interface ActivitySignupService {

    // 查看报名列表
    Result<List<ActivitySignupVO>> listSignups(Integer activityId, Integer operatorId);
    //查看未审核和已经审核的
    Result<List<ActivitySignupVO>> listPendingSignups(Integer activityId, Integer operatorId);
    Result<List<ActivitySignupVO>> listApprovedSignups(Integer activityId, Integer operatorId);
    // 审核报名
    Result<Void> auditSignup(Integer signupId, Integer operatorId, String status, String reason);

    Result<List<ActivitySignupUserVO>> getSignupUsersForAdmin(Integer activityId);
}