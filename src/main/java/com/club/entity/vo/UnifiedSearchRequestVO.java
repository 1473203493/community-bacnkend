package com.club.entity.vo;

import lombok.Data;

@Data
public class UnifiedSearchRequestVO {

    /** 搜索类型：1=社团，2=活动 */
    private Integer searchType;

    /** 搜索关键词 */
    private String keyword;

    /** 分类 ID，可选 */
    private Integer categoryId;

    /** 页码 */
    private Integer pageNum = 1;

    /** 每页条数 */
    private Integer pageSize = 10;
}
