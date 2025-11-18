package com.club.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zyh
 * @date 2025/11/17 9:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "分页参数")
public class PageDto {

    @Schema(description = "当前页")
    private Integer pageNum;

    @Schema(description = "每页数量")
    private Integer pageSize;
}
