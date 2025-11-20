// com/club/entity/request/UserQueryDto.java
package com.club.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户信息查询参数")
public class UserQueryDto extends PageDto {

    @Schema(description = "学号")
    private String studentNo;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "角色类型：1.学生，2.社团管理员")
    private String role;

    @Schema(description = "账号状态 1.启用，2.停用")
    private String status;
}
