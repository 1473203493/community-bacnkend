package com.club.entity.vo;

import lombok.Data;

@Data
public class AuditSignupRequest {
    private Integer signupId;
    private Integer operatorId;
    private String status;  // 1 = 通过, 2 = 拒绝
    private String reason;  // 拒绝理由，可选


}
