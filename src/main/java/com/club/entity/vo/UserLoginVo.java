package com.club.entity.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户登录返回结果实体类")
public class UserLoginVo {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "微信openid")
    private String openid;

    @Schema(description = "角色（学生还是社团管理员）")
    private String role;

    @Schema(description = "令牌")
    private String token;

    @Schema(description = "用户姓名")
    private String name;

    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "学号")
    private String studentNo;

}
