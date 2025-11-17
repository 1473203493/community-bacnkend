package com.club.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "简化版社团信息")
public class ClubSimpleVO {

    @Schema(description = "社团ID")
    private Integer clubId;

    @Schema(description = "社团名称")
    private String name;

    @Schema(description = "社团简介")
    private String description;

    @Schema(description = "成员数量")
    private Integer memberCount;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "所属分类")
    private String categoryName;
}