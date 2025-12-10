package com.club.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "活动报名返回信息（含用户姓名和活动标题）")
public class ActivitySignupVO {

    @Schema(description = "报名ID")
    private Integer signupId;

    @Schema(description = "活动ID")
    private Integer activityId;

    @Schema(description = "活动标题")
    private String activityTitle;   // 新增

    @Schema(description = "报名用户ID")
    private Integer userId;

    @Schema(description = "报名用户姓名")
    private String userName;        // 新增

    @Schema(description = "报名状态")
    private String status;

    @Schema(description = "审核理由/拒绝原因")
    private String reason;

    @Schema(description = "报名时间")
    private LocalDateTime createdAt;
}
