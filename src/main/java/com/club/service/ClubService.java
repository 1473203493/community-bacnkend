package com.club.service;

import com.club.entity.Club;
import com.club.entity.request.ClubApprovalDto;
import com.club.entity.vo.ClubCreateRequestVO;
import com.club.entity.vo.Result;

import java.util.List;
import java.util.Map;
import com.club.entity.request.ClubQueryDto;
import com.github.pagehelper.PageInfo;

public interface ClubService {
    PageInfo<Club> getClubList(ClubQueryDto queryDto);

    // 获取社团详情（用于审批）
    Club getClubDetail(Integer clubId);

    // 审批社团
    void approveClub(ClubApprovalDto approvalDto);

    Result<Void> createClub(ClubCreateRequestVO request);

    Result<List<Club>> listMyClubs(Integer userId);

    // 学生端获取社团列表
    Map<String, Object> getClubListForStudent(Map<String, Object> params);

    // 学生端获取社团详情
    Map<String, Object> getClubDetailForStudent(Long clubId, Long userId);

    // 申请加入社团
    void applyToJoinClub(Long userId, Long clubId, String remark);

    // 获取用户加入的社团列表（学生端）
    Map<String, Object> getMyClubs(Long userId);

}