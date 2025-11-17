package com.club.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "社团分类表")
public class ClubCategory {

    @Schema(description = "分类ID")
    private Integer categoryId;

    @Schema(description = "分类名称")
    private String name;

    @Schema(description = "排序序号")
    private Integer orderNo;
}