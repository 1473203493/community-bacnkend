package com.club.entity.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Schema(description = "新增活动请求参数")
public class ActivityCreateRequestVO {

    @NotNull(message = "社团ID不能为空")
    @Schema(description = "社团ID", required = true)
    private Integer clubId;

    @NotBlank(message = "活动标题不能为空")
    @Schema(description = "活动标题", required = true)
    private String title;

    @NotBlank(message = "活动简介不能为空")
    @Schema(description = "活动简介", required = true)
    private String description;

    @Schema(description = "活动地点")
    private String location;

    @NotNull(message = "活动时间不能为空")
    @Schema(description = "活动时间", required = true)
    private LocalDateTime time;

    @Schema(description = "活动名额上限")
    private Integer quota = 0;

    @Schema(description = "是否需要报名审核")
    private Boolean needAudit = false;

    @NotNull(message = "创建人ID不能为空")
    @Schema(description = "活动创建人用户ID", required = true)
    private Integer creatorId;

    @NotNull(message = "活动开始时间不能为空")
    @Schema(description = "活动开始时间", required = true)
    private LocalDateTime startTime;

    @NotNull(message = "活动结束时间不能为空")
    @Schema(description = "活动结束时间", required = true)
    private LocalDateTime endTime;

}
