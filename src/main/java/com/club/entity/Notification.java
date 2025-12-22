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
@Schema(description = "系统通知表")
public class Notification {

    @Schema(description = "通知ID")
    private Integer noticeId;

    @Schema(description = "学生或社团管理员")
    private Integer userId;

    @Schema(description = "平台管理员")
    private Integer adminId;

    @Schema(description = "（接收方的）角色   （1:普通学生， 2:社团管理员， 3:平台管理员) ")
    private String role;

    @Schema(description = "通知标题")
    private String title;

    @Schema(description = "通知内容")
    private String content;

    @Schema(description = "是否已读")
    private Boolean isRead;

    @Schema(description = "发送时间")
    private LocalDateTime createdAt;

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }
}