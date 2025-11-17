package com.club.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "图片验证码返回结果实体类")
public class ValidateCodeVo {

    @Schema(description = "验证码的key")
    private String codeKey ;        // 验证码的key

    @Schema(description = "图片验证码对应的字符串数据")
    private String codeValue ;      // 图片验证码对应的字符串数据

}
