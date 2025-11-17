package com.club.service;

import com.club.entity.vo.UnifiedSearchRequestVO;
import com.github.pagehelper.PageInfo;

public interface UnifiedSearchService {

    /**
     * 统一搜索（根据类型搜索社团或活动）
     */
    PageInfo<?> unifiedSearch(UnifiedSearchRequestVO request);
}