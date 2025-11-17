package com.club.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author zyh
 * @date 2025/11/12 15:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "平台管理员表")
public class Admin {

    @Schema(description = "用户ID")
    private Integer userId;

    @Schema(description = "学号或管理员编号")
    private String adminNo;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "密码（加密存储）")
    private String password;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "角色类型")
    private String role;

    @Schema(description = "账号状态")
    private String status;

    @Schema(description = "注册时间")
    private LocalDateTime createdAt;
}
