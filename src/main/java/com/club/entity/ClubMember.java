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

    //申请（加入）状态joinStatus，1.待确定，2.已加入（同意），3.拒绝
    public static final String JOIN_STATUS_WAIT = "1";
    public static final String JOIN_STATUS_JOINED = "2";
    public static final String JOIN_STATUS_REJECT = "3";


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