package com.club.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description =  "管理员登录传递参数")
public class AdminLoginDto {

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "提交验证码")
    private String captcha ;

    @Schema(description = "验证码key")
    private String codeKey ;
}
