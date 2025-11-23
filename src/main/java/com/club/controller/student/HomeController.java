package com.club.controller.student;

import com.club.entity.vo.Result;
import com.club.service.HomeService;
import com.club.util.AuthContextUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
@Tag(name = "首页展示", description = "首页相关接口")
@Slf4j
public class HomeController {

    @Autowired
    private HomeService homeService;

    /**
     * 获取首页信息
     * 返回：热门社团（按时间）、人数最多社团 TOP3
     * @return 首页信息
     */
    @GetMapping("/info")
    @Operation(summary = "获取首页信息", description = "获取首页信息，包括热门社团和人数最多社团")
    public Result<?> getHomeInfo() {
        try {
            // 获取当前用户ID（可能为null，表示未登录用户）
            Long userId = null;
            try {
                userId = AuthContextUtil.getCurrentUserId();
            } catch (Exception e) {
                // 未登录用户也可以访问首页
                log.info("未登录用户访问首页");
            }

            // 调用服务层获取首页信息
            Object homeInfo = homeService.getHomeInfo(userId);
            return Result.ok(homeInfo);
        } catch (Exception e) {
            log.error("获取首页信息失败", e);
            return Result.fail("获取首页信息失败");
        }
    }
}