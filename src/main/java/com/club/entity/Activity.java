package com.club.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "活动表")
public class Activity {

    @Schema(description = "活动ID")
    private Integer activityId;

    @Schema(description = "举办社团")
    private Integer clubId;

    @Schema(description = "活动标题")
    private String title;

    @Schema(description = "活动简介")
    private String description;

    @Schema(description = "活动地点")
    private String location;

    @Schema(description = "活动时间")
    private LocalDateTime time;

    @Schema(description = "名额上限")
    private Integer quota;

    @Schema(description = "是否需要报名审核")
    private Boolean needAudit;

    @Schema(description = "活动状态")
    private String status;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}