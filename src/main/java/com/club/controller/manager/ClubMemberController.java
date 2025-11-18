package com.club.controller.manager;

import com.club.entity.ClubMember;
import com.club.entity.vo.Result;
import com.club.service.ClubMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clubs")
@Tag(name = "社团成员管理")
public class ClubMemberController {

    @Autowired
    private ClubMemberService clubMemberService;

    @GetMapping("/{clubId}/members")
    @Operation(summary = "查看社团成员列表", description = "仅社团管理员/领导可查看")
    public Result<List<ClubMember>> listMembers(
            @PathVariable Integer clubId,
            @RequestParam Integer operatorId   // 当前操作者 userId
    ) {
        return clubMemberService.listMembers(clubId, operatorId);
    }

    // 调整成员角色
    @PutMapping("/{clubId}/members/{targetUserId}/role")
    @Operation(summary = "调整成员角色", description = "仅管理员/领导可操作")
    public Result<Void> updateMemberRole(
            @PathVariable Integer clubId,
            @RequestParam Integer operatorId,
            @PathVariable Integer targetUserId,
            @RequestParam String newRole,
            @RequestParam(required = false) String reason
    ) {
        return clubMemberService.updateMemberRole(clubId, operatorId, targetUserId, newRole, reason);
    }

}

