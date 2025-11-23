package com.club.service;

import com.club.entity.vo.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 社团创建服务接口
 * @author zyh
 * @date 2025/11/11
 */
public interface ClubCreateService {

    /**
     * 提交社团创建申请
     * @param params 社团信息参数
     * @param attachment 负责人证明附件
     * @return 操作结果
     */
    Result<?> submitClubCreateRequest(Map<String, Object> params, MultipartFile attachment);

    /**
     * 查询用户发起的社团申请
     * @param userId 用户ID
     * @return 社团申请列表
     */
    Result<?> getMyClubCreateRequests(Long userId);
}