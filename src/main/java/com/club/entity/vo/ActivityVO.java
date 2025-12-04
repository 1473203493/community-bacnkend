package com.club.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "活动列表展示VO")
public class ActivityVO {
    @Schema(description = "活动ID")
    private Integer activityId;

    @Schema(description = "活动标题")
    private String title;

    @Schema(description = "举办社团名称")
    private String clubName;

    @Schema(description = "活动时间")
    private LocalDateTime time;

    @Schema(description = "活动地点")
    private String location;

    @Schema(description = "报名人数")
    private Integer signupCount;

    @Schema(description = "活动状态")
    private String status;

    @Schema(description = "活动状态文本")
    private String statusText;
}