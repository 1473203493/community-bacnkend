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
@Schema(description = "操作日志表")
public class OperationLog {

    @Schema(description = "日志ID")
    private Integer logId;

    @Schema(description = "操作人")
    private Integer userId;

    @Schema(description = "操作内容")
    private String action;

    @Schema(description = "操作IP")
    private String ipAddress;

    @Schema(description = "操作时间")
    private LocalDateTime createdAt;
}