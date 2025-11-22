package com.club.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "社团查询参数")
public class ClubQueryDto extends PageDto {

    @Schema(description = "状态：1.待平台管理员确认（待审批），2.启动（激活/正常），3.停用（冻结），4.拒绝")
    private String status;

    @Schema(description = "分类ID")
    private Integer categoryId;

    @Schema(description = "负责人邮箱（模糊搜索）")
    private String founderEmail;
}