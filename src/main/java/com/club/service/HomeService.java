package com.club.service;

import com.club.entity.vo.ClubSimpleVO;
import com.club.entity.vo.Result;

import java.util.List;

/**
 * 首页服务接口
 */
public interface HomeService {

    /**
     * 获取热门社团推荐（按时间排序）
     */
    Result<List<ClubSimpleVO>> getHotClubs();

    /**
     * 获取人气社团推荐（按人数前三排序）
     */
    Result<List<ClubSimpleVO>> getPopularClubs();

    /**
     * 获取首页信息
     * @param userId 当前用户ID（可能为null）
     * @return 首页信息对象，包含热门社团和人数最多社团
     */
    Object getHomeInfo(Long userId);
}