package com.club.service;

import com.club.entity.vo.Result;

/**
 * 入社申请服务接口
 * @author zyh
 * @date 2025/11/11
 */
public interface ClubApplyService {

    /**
     * 查询用户的入社申请列表
     * @param userId 用户ID
     * @return 入社申请列表
     */
    Result<?> getMyClubApplies(Long userId);

    /**
     * 撤销入社申请
     * @param applyId 申请ID
     * @param userId 用户ID
     * @return 操作结果
     */
    Result<?> cancelClubApply(Long applyId, Long userId);
}