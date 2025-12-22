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

    //活动状态status
    //1.待确定（平台管理员），2.同意（平台管理员），3还在活动时间，4活动结束了，5.活动被拒绝（平台管理员）
    public static final String STATUS_WAIT = "1";
    public static final String STATUS_AGREE = "2";
    public static final String STATUS_DOING = "3";
    public static final String STATUS_END = "4";
    public static final String STATUS_REFUSE = "5";

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

    @Schema(description = "活动开始时间")
    private LocalDateTime startTime;

    @Schema(description = "活动结束时间")
    private LocalDateTime endTime;
}