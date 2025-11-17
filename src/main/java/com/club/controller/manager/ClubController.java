package com.club.controller.manager;

import com.club.entity.Club;
import com.club.entity.vo.ClubCreateRequestVO;
import com.club.entity.vo.Result;
import com.club.service.ClubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/clubs")
@Tag(name = "社团管理", description = "社团新增接口")
public class ClubController {

    @Autowired
    private ClubService clubService;

    @PostMapping
    @Operation(summary = "新增社团", description = "用户提交社团信息，等待平台管理员审核")
    public Result<Void> createClub(@RequestBody @Valid ClubCreateRequestVO request) {
        log.info("新增社团请求：{}", request);
        return clubService.createClub(request);
    }

    @GetMapping("/my")
    @Operation(summary = "查看我加入的社团")
    public Result<List<Club>> listMyClubs(@RequestParam Integer userId) {
        return clubService.listMyClubs(userId);
    }

}
