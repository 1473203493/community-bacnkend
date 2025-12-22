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
@Schema(description = "活动报名表")
public class ActivitySignup {

    //报名状态status，1.待确定（社团管理员），2.同意，3.被拒绝，4.活动结束了
    public static final String STATUS_WAITING = "1";
    public static final String STATUS_AGREE = "2";
    public static final String STATUS_REJECT = "3";
    public static final String STATUS_END = "4";

    @Schema(description = "报名ID")
    private Integer signupId;

    @Schema(description = "活动ID")
    private Integer activityId;

    @Schema(description = "报名用户")
    private Integer userId;

    @Schema(description = "报名状态")
    private String status;

    @Schema(description = "审核理由/拒绝原因")
    private String reason;

    @Schema(description = "报名时间")
    private LocalDateTime createdAt;
}