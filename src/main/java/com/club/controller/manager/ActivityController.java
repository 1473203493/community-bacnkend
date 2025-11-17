package com.club.controller.manager;

import com.club.entity.Activity;
import com.club.entity.vo.ActivityCreateRequestVO;
import com.club.entity.vo.Result;
import com.club.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/activities")
@Tag(name = "活动管理", description = "活动新增接口")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @PostMapping
    @Operation(summary = "新增活动", description = "用户提交活动信息，等待平台管理员审核")
    public Result<Void> createActivity(@RequestBody @Valid ActivityCreateRequestVO request) {
        log.info("新增活动请求：{}", request);
        return activityService.createActivity(request);
    }

    @GetMapping("/club/{clubId}")
    @Operation(summary = "查看某社团的所有活动")
    public Result<List<Activity>> listActivitiesByClub(@PathVariable Integer clubId) {
        return activityService.listActivitiesByClub(clubId);
    }

}
