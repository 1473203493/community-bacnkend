package com.club.entity.admin.vo;

import com.club.entity.OperationLog;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author zyh
 * @date 2025/11/19 10:16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "操作日志详情VO")
@AllArgsConstructor
@NoArgsConstructor
public class OperationLogVO extends OperationLog {

    @Schema(description = "学生编号(来自user表)")
    private String studentNo;

    @Schema(description = "管理员编号(来自admin表)")
    private String adminNo;

    @Schema(description = "角色")
    private String role;
}
