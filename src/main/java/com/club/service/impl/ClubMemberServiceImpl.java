package com.club.service.impl;

import com.club.entity.ClubMember;
import com.club.entity.vo.Result;
import com.club.mapper.ClubMemberMapper;
import com.club.service.ClubMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubMemberServiceImpl implements ClubMemberService {

    @Autowired
    private ClubMemberMapper clubMemberMapper;

    /**
     * 查看成员列表
     */
    @Override
    public Result<List<ClubMember>> listMembers(Integer clubId, Integer operatorId) {

        // 1. 检查操作者是否是管理员或部长
        ClubMember operator = clubMemberMapper.selectMemberByUserIdAndClubId(clubId, operatorId);

        if (operator == null ||
                (!"2".equals(operator.getRole()) && !"3".equals(operator.getRole()))) {

            return Result.build(null, 403, "无权限查看成员列表");
        }

        // 2. 查询成员
        List<ClubMember> list = clubMemberMapper.selectMembersByClubId(clubId);
        return Result.build(list, 200, "查询成功");
    }


    /**
     * 调整成员角色
     */
    @Override
    public Result<Void> updateMemberRole(Integer clubId,
                                         Integer operatorId,
                                         Integer targetUserId,
                                         String newRole,
                                         String reason) {

        // 1. 权限验证：操作人必须是管理员2或 领导(3)
        ClubMember operator = clubMemberMapper.selectMemberByUserIdAndClubId(clubId, operatorId);

        if (operator == null ||
                (!"2".equals(operator.getRole()) && !"3".equals(operator.getRole()))) {

            return Result.build(null, 403, "无权限调整成员角色");
        }

        // 2. 查询目标成员是否存在
        ClubMember target = clubMemberMapper.selectMemberByUserIdAndClubId(clubId, targetUserId);
        if (target == null) {
            return Result.build(null, 404, "目标成员不存在");
        }

        //  若 reason 未传（null 或 空字符串），则保持旧原因
        if (reason == null || reason.trim().isEmpty()) {
            reason = target.getApplyReason();   // 保留原来的原因
        }
        // 3. 更新角色
        int rows = clubMemberMapper.updateMemberRole(clubId, targetUserId, newRole, reason);

        if (rows > 0) {
            return Result.build(null, 200, "角色调整成功");
        }

        return Result.build(null, 500, "角色调整失败");
    }
}
