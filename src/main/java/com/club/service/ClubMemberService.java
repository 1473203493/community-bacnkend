package com.club.service;

import com.club.entity.ClubMember;
import com.club.entity.vo.Result;

import java.util.List;

public interface ClubMemberService {

    Result<List<ClubMember>> listMembers(Integer clubId, Integer operatorId);

    Result<Void> updateMemberRole(Integer clubId,
                                  Integer operatorId,
                                  Integer targetUserId,
                                  String newRole,
                                  String reason);
}