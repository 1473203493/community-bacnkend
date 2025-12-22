package com.club.entity.message.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zyh
 * @date 2025/12/21 16:35
 * 普通学生消息通知传参
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "普通学生申请 消息通知传参")
public class MessageUserDto {

    @Schema(description = "用户或管理员id（接收方的id）")
    private Integer userOrAdminId;

//    @Schema(description = "学号或工号（因为也是唯一性的字段，当不好传id时，可以传学号或工号代替）")
//    private String studentOrAdminNo;

    @Schema(description = "（接收方的）角色   （1:普通学生， 2:社团管理员， 3:平台管理员) ")
    private String role;

    @Schema(description = "消息标题")
    private String title;

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "社团id（如果只是申请加入社团，则只需传社团id即可）")
    private Integer clubId;

    @Schema(description = "活动id（如果是申请加入活动，需要同时传哪个社团（社团id）和活动id）")
    private Integer activityId;
}
