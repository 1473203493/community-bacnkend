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
@Schema(description = "社团信息表")
public class Club {

    //社团状态status 1.待平台管理员确认，2.启动（激活）3.停用 4.拒绝（被平台管理员拒绝）
    public static final String STATUS_WAITING = "1";
    public static final String STATUS_ACTIVE = "2";
    public static final String STATUS_FROZEN = "3";
    public static final String STATUS_REJECTED = "4";

    @Schema(description = "社团ID")
    private Integer clubId;

    @Schema(description = "社团名称")
    private String name;

    @Schema(description = "所属类别")
    private Integer categoryId;

    @Schema(description = "社团简介")
    private String description;

    @Schema(description = "章程内容")
    private String charter;

    @Schema(description = "创建人")
    private Integer founderId;

    @Schema(description = "社团状态：1.待平台管理员确认,2.启动(激活),3.停用(冻结),4.拒绝")
    private String status;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    //2025.11.79 -添加 -wsx
    // 扩展字段（用于列表展示，数据库无对应字段，通过关联查询赋值）
    @Schema(description = "负责人姓名")
    private String founderName;

    @Schema(description = "负责人邮箱")
    private String founderEmail;

    @Schema(description = "分类名称")
    private String categoryName;
}