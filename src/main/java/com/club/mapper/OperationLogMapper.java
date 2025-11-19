package com.club.mapper;

import com.club.entity.OperationLog;
import com.club.entity.admin.vo.OperationLogVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OperationLogMapper {
    /**
     * 插入操作日志
     * @param operationLog 操作日志实体
     * @return 影响行数
     */
    int insert(OperationLog operationLog);

    /**
     * 分页查询操作日志
     * @return
     */
    List<OperationLogVO> list();
}