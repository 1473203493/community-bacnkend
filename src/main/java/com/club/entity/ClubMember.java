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
@Schema(description = "社团成员表")
public class ClubMember {

    @Schema(description = "成员ID")
    private Integer memberId;

    @Schema(description = "社团ID")
    private Integer clubId;

    @Schema(description = "用户ID")
    private Integer userId;

    @Schema(description = "成员角色")
    private String role;

    @Schema(description = "申请状态")
    private String joinStatus;

    @Schema(description = "申请理由")
    private String applyReason;

    @Schema(description = "加入时间")
    private LocalDateTime joinedAt;
}