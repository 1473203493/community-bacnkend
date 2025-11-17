package com.club.controller.manager;




import com.club.entity.vo.ClubSimpleVO;
import com.club.entity.vo.Result;
import com.club.service.HomeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/home")
@Tag(name = "首页管理", description = "首页社团推荐接口")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @GetMapping("/hot-clubs")
    @Operation(summary = "获取热门社团推荐", description = "按创建时间倒序排列的前10个社团")
    public Result<List<ClubSimpleVO>> getHotClubs() {
        return homeService.getHotClubs();
    }

    @GetMapping("/popular-clubs")
    @Operation(summary = "获取人气社团推荐", description = "按成员数量倒序排列的前3个社团")
    public Result<List<ClubSimpleVO>> getPopularClubs() {
        return homeService.getPopularClubs();
    }
}