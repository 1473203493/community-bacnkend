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
}