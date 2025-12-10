// 审批操作DTO
package com.club.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "社团审批参数")
public class ClubApprovalDto {
    @Schema(description = "社团ID")
    private Integer clubId;

    @Schema(description = "审批结果：2-同意，4-拒绝")
    private String status;

    @Schema(description = "驳回理由，状态为拒绝时必填")
    private String rejectReason;
}