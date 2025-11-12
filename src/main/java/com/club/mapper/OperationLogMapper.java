package com.club.mapper;

import com.club.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperationLogMapper {
    /**
     * 插入操作日志
     * @param operationLog 操作日志实体
     * @return 影响行数
     */
    int insert(OperationLog operationLog);
}