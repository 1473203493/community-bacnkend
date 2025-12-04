package com.club.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "活动报名人员信息")
public class ActivitySignupUserVO {
    @Schema(description = "报名ID")
    private Integer signupId;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "学号")
    private String studentNo;

    @Schema(description = "脱敏邮箱")
    private String maskedEmail;

    @Schema(description = "报名时间")
    private LocalDateTime signupTime;

    @Schema(description = "报名状态")
    private String status;
}