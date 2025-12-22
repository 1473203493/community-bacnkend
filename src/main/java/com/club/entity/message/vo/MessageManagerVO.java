package com.club.entity.message.vo;

import com.club.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author zyh
 * @date 2025/12/21 17:56、
 * websocket推送消息值对象（社团管理员）社团管理员 -> 平台管理员
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "websocket推送消息值对象（社团管理员）社团管理员 -> 平台管理员")
public class MessageManagerVO extends User {

    @Schema(description = "消息标题")
    private String title;

    @Schema(description = "消息内容")
    private String content;


    @Schema(description = "社团id")
    private Integer clubId;

    @Schema(description = "活动id")
    private Integer activityId;
}
