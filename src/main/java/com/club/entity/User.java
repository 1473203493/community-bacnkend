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
@Schema(description = "用户表（学生和社团管理员）")
public class User {

    @Schema(description = "用户ID")
    private Integer userId;

    @Schema(description = "微信openid")
    private String openid;

    @Schema(description = "学号或管理员编号")
    private String studentNo;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "密码（加密存储）")
    private String password;

    @Schema(description = "角色类型")
    private String role;

    @Schema(description = "账号状态")
    private String status;

    @Schema(description = "注册时间")
    private LocalDateTime createdAt;
}