package com.club.service.impl;

import com.club.entity.vo.UnifiedSearchRequestVO;
import com.club.mapper.ActivityMapper;
import com.club.mapper.ClubMapper;
import com.club.service.UnifiedSearchService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UnifiedSearchServiceImpl implements UnifiedSearchService {

    @Autowired
    private ClubMapper clubMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public PageInfo<?> unifiedSearch(UnifiedSearchRequestVO request) {

        if (request.getSearchType() == 1) {
            // 搜索社团
            PageHelper.startPage(request.getPageNum(), request.getPageSize());
            return new PageInfo<>(clubMapper.searchClubs(request.getKeyword(), request.getCategoryId()));

        } else if (request.getSearchType() == 2) {
            // 搜索活动
            PageHelper.startPage(request.getPageNum(), request.getPageSize());
            return new PageInfo<>(activityMapper.searchActivities(request.getKeyword(), request.getCategoryId()));

        } else {
            throw new IllegalArgumentException("不支持的搜索类型：" + request.getSearchType());
        }
    }
}
