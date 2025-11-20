package com.club.controller.admin;

import com.club.entity.OperationLog;
import com.club.entity.admin.vo.OperationLogVO;
import com.club.entity.request.PageDto;
import com.club.entity.vo.Result;
import com.club.entity.vo.ResultCodeEnum;
import com.club.service.OperationLogService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zyh
 * @date 2025/11/19 10:01
 */
@RestController
@RequestMapping("/admin/operation")
@Tag(name = "平台管理员操作日志接口")
@Slf4j
public class OperationController {

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 分页查询操作日志
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询操作日志")
    public Result<PageInfo<OperationLogVO>> listOperationLogs(@RequestBody PageDto pageDto) {

        PageInfo<OperationLogVO> operationLogs = operationLogService.listOperationLogs(pageDto.getPageNum(),pageDto.getPageSize());

        return Result.build(operationLogs, ResultCodeEnum.SUCCESS);
    }
}
