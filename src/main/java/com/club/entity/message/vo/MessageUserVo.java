package com.club.entity.message.vo;

import com.club.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zyh
 * @date 2025/12/21 16:40
 * websocket推送消息值对象（普通学生），用于普通学生 -> 社团管理员
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "websocket推送消息值对象（普通学生），用于普通学生 -> 社团管理员")
public class MessageUserVo extends User {

    @Schema(description = "消息标题")
    private String title;

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "社团id（一般来说社团id和活动id传一个就行了）")
    private Integer clubId;

    @Schema(description = "活动id")
    private Integer activityId;
}
