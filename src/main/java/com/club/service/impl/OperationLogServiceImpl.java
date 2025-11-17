package com.club.service.impl;

import com.club.entity.OperationLog;
import com.club.mapper.OperationLogMapper;
import com.club.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationLogServiceImpl implements OperationLogService {
    
    @Autowired
    private OperationLogMapper operationLogMapper;
    
    @Override
    public void saveOperationLog(OperationLog operationLog) {
        // 保存操作日志
        operationLogMapper.insert(operationLog);
    }
}