package com.club.entity.message.replyvo;

import com.club.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zyh
 * @date 2025/12/22 20:59
 * websocket推送回复消息(社团管理员 -> 普通学生)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "websocket推送回复消息(社团管理员 -> 普通学生)")
public class MessageManagerReplyVo extends User {

    @Schema(description = "消息标题")
    private String title;

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "社团id")
    private Integer clubId;

    @Schema(description = "活动id（如果是活动需要社团id和活动id都传， 如果是社团则只需传社团id）")
    private Integer activityId;

    @Schema(description = "审批结果（true:通过，false:拒绝）")
    private Boolean result;
}
