package com.club.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zyh
 * @date 2025/12/15 22:41
 * 用户保存信息传参
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSaveDto {
    @Schema(description = "微信用户唯一code标识，每个微信用户都是唯一的")
    private String code;

    @Schema(description = "学号或管理员编号")
    private String studentNo;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "邮箱")
    private String email;
}
