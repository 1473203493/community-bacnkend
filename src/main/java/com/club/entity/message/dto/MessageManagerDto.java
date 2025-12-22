package com.club.entity.message.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author zyh
 * @date 2025/12/21 18:36
 * 社团管理员消息通知传参
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "社团管理员申请 消息通知传参")
public class MessageManagerDto {

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

    @Schema(description = "社团名称（如果是创建社团则需要传回  社团名称，社团分类和社团描述，但不需要传回活动名称）")
    private String clubName;

    @Schema(description = "社团分类")
    private String clubCategory;

    @Schema(description = "社团简介")
    private String description;

    @Schema(description = "活动名称（如果是创建活动，则需要传回社团id，但不需要传社团名称和社团分类了；如果活动名称为空，说明是创建社团而不是创建活动）")
    private String activityName;

    @Schema(description = "活动简介")
    private String activityDescription;

    @Schema(description = "活动地点")
    private String activityLocation;

    @Schema(description = "活动开始时间")
    private LocalDateTime startTime;

    @Schema(description = "活动结束时间")
    private LocalDateTime endTime;

    @Schema(description = "名额上限")
    private Integer activityQuota;

    @Schema(description = "是否需要报名审核")
    private Boolean activityNeedAudit;

    @Schema(description = "社团id (申请新建活动需要传参已有的社团id，如果是新建社团不用传社团id）")
    private Integer clubId;
}
