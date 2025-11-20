package com.club.service.impl;

import com.club.entity.OperationLog;
import com.club.entity.admin.vo.OperationLogVO;
import com.club.mapper.OperationLogMapper;
import com.club.service.OperationLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationLogServiceImpl implements OperationLogService {
    
    @Autowired
    private OperationLogMapper operationLogMapper;
    
    @Override
    public void saveOperationLog(OperationLog operationLog) {
        // 保存操作日志
        operationLogMapper.insert(operationLog);
    }

    /**
     * 分页获取操作日志列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<OperationLogVO> listOperationLogs(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum , pageSize);
        List<OperationLogVO> operationLogVOList = operationLogMapper.list();
        PageInfo<OperationLogVO> pageInfo = new PageInfo(operationLogVOList);
        return pageInfo;
    }
}