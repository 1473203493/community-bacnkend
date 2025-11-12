package com.club.service;

import com.club.entity.OperationLog;

public interface OperationLogService {
    /**
     * 保存操作日志
     * @param operationLog 操作日志实体
     */
    void saveOperationLog(OperationLog operationLog);
}