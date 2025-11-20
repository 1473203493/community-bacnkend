package com.club.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Schema(description = "操作日志表")
public class OperationLog {

    @Schema(description = "日志ID")
    private Integer logId;

    @Schema(description = "学生或社团管理员")
    private Integer userId;

    @Schema(description = "平台管理员")
    private Integer adminId;

    @Schema(description = "操作内容")
    private String action;

    @Schema(description = "操作IP")
    private String ipAddress;

    @Schema(description = "操作时间")
    private LocalDateTime createdAt;
}