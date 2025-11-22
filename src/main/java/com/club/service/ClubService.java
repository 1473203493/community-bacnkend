package com.club.service;

import com.club.entity.Club;
import com.club.entity.request.ClubApprovalDto;
import com.club.entity.vo.ClubCreateRequestVO;
import com.club.entity.vo.Result;

import java.util.List;
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

}