package com.club.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Schema(description = "新增社团请求参数")
public class ClubCreateRequestVO {

    @NotBlank(message = "社团名称不能为空")
    @Schema(description = "社团名称", required = true)
    private String name;

    @NotNull(message = "社团类别不能为空")
    @Schema(description = "社团类别ID", required = true)
    private Integer categoryId;

    @NotBlank(message = "社团简介不能为空")
    @Schema(description = "社团简介", required = true)
    private String description;

    @NotBlank(message = "社团章程不能为空")
    @Schema(description = "社团章程内容", required = true)
    private String charter;

    @NotNull(message = "创建人ID不能为空")
    @Schema(description = "创建人用户ID", required = true)
    private Integer founderId;
}
