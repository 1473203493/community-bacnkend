package com.club.controller.manager;

import com.club.entity.vo.Result;
import com.club.entity.vo.UnifiedSearchRequestVO;
import com.club.service.UnifiedSearchService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/search")
@Tag(name = "统一搜索", description = "支持社团和活动的统一搜索")
public class UnifiedSearchController {

    @Autowired
    private UnifiedSearchService unifiedSearchService;

    @PostMapping("/unified")
    @Operation(summary = "统一搜索", description = "根据选择的标签搜索社团或活动")
    public Result<PageInfo<?>> unifiedSearch(@RequestBody UnifiedSearchRequestVO request) {
        try {
            log.info("统一搜索请求：类型={}, 关键词={}", request.getSearchType(), request.getKeyword());

            PageInfo<?> result = unifiedSearchService.unifiedSearch(request);

            return Result.build(result, 200, "success");

        } catch (Exception e) {
            log.error("统一搜索失败", e);
            return Result.build(null, 500, "搜索失败：" + e.getMessage());
        }
    }
}
