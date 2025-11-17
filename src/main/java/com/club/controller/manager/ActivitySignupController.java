package com.club.controller.manager;


import com.club.entity.ActivitySignup;
import com.club.entity.vo.AuditSignupRequest;
import com.club.entity.vo.Result;
import com.club.service.ActivitySignupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@Tag(name = "活动报名管理")
public class ActivitySignupController {

    @Autowired
    private ActivitySignupService activitySignupService;

    @GetMapping("/{activityId}/signups")
    @Operation(summary = "查看活动报名列表")
    public Result<List<ActivitySignup>> listSignups(
            @PathVariable Integer activityId,
            @RequestParam Integer operatorId
    ) {
        return activitySignupService.listSignups(activityId, operatorId);
    }

    @PostMapping("/signup/audit")
    @Operation(summary = "审核活动报名")
    public Result<Void> auditSignup(@RequestBody AuditSignupRequest request) {
        return activitySignupService.auditSignup(
                request.getSignupId(),
                request.getOperatorId(),
                request.getStatus(),
                request.getReason()
        );
    }


    @GetMapping("/{activityId}/signups/pending")
    @Operation(summary = "查看未审核报名")
    public Result<List<ActivitySignup>> listPendingSignups(
            @PathVariable Integer activityId,
            @RequestParam Integer operatorId
    ) {
        return activitySignupService.listPendingSignups(activityId, operatorId);
    }

    @GetMapping("/{activityId}/signups/approved")
    @Operation(summary = "查看已通过报名")
    public Result<List<ActivitySignup>> listApprovedSignups(
            @PathVariable Integer activityId,
            @RequestParam Integer operatorId
    ) {
        return activitySignupService.listApprovedSignups(activityId, operatorId);
    }

}
