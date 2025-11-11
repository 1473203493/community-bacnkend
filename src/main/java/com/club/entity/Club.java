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

    @Schema(description = "社团状态")
    private String status;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}