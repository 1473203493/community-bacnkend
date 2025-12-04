package com.club.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "活动查询条件DTO")
public class ActivityQueryDto {
    @Schema(description = "状态：1.待确定 2.同意 3.进行中 4.已结束 5.被拒绝")
    private String status;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "社团名称")
    private String clubName;

    @Schema(description = "页码")
    private Integer pageNum = 1;

    @Schema(description = "每页条数")
    private Integer pageSize = 10;
}