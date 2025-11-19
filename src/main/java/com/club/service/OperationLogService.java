package com.club.service;

import com.club.entity.OperationLog;
import com.club.entity.admin.vo.OperationLogVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface OperationLogService {
    /**
     * 保存操作日志
     * @param operationLog 操作日志实体
     */
    void saveOperationLog(OperationLog operationLog);

    /**
     * 分页查询操作日志
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<OperationLogVO> listOperationLogs(Integer pageNum, Integer pageSize);
}