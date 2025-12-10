package com.club.service.impl;

import com.club.entity.vo.Result;
import com.club.entity.vo.ResultCodeEnum;
import com.club.mapper.ClubCreateMapper;
import com.club.service.ClubCreateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 社团创建服务实现类
 * @author zyh
 * @date 2025/11/11
 */
@Service
public class ClubCreateServiceImpl implements ClubCreateService {

    private static final Logger logger = LoggerFactory.getLogger(ClubCreateServiceImpl.class);

    @Autowired
    private ClubCreateMapper clubCreateMapper;

    @Override
    public Result<?> submitClubCreateRequest(Map<String, Object> params, MultipartFile attachment) {
        try {
            logger.info("开始提交社团创建申请，参数：{}", params);

            // 检查参数
            String name = (String) params.get("name");
            String category = (String) params.get("category");
            String purpose = (String) params.get("purpose");
            String charter = (String) params.get("charter");
            Long userId = (Long) params.get("userId");

            if (name == null || name.trim().isEmpty() ||
                    category == null || category.trim().isEmpty() ||
                    purpose == null || purpose.trim().isEmpty() ||
                    charter == null || charter.trim().isEmpty() ||
                    userId == null) {
                return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "参数不完整");
            }

            // 检查用户是否已经有未审核的社团创建申请
            int count = clubCreateMapper.countPendingRequestsByUserId(userId);
            if (count > 0) {
                return Result.build(null, ResultCodeEnum.BUSINESS_ERROR.getCode(), "您已有未审核的社团创建申请，请等待审核结果");
            }

            // 检查社团名称是否已存在
            count = clubCreateMapper.countByName(name);
            if (count > 0) {
                return Result.build(null, ResultCodeEnum.BUSINESS_ERROR.getCode(), "社团名称已存在");
            }

            // 处理附件上传
            String attachmentPath = null;
            if (attachment != null && !attachment.isEmpty()) {
                // 生成唯一文件名
                String originalFilename = attachment.getOriginalFilename();
                String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".pdf";
                String filename = UUID.randomUUID().toString() + extension;

                // 保存文件（实际应用中应配置文件保存路径）
                String savePath = "C:/upload/club/" + filename;
                File saveFile = new File(savePath);
                saveFile.getParentFile().mkdirs();
                attachment.transferTo(saveFile);

                attachmentPath = savePath;
            }

            // 构建插入参数
            Map<String, Object> insertMap = new HashMap<>();
            insertMap.put("name", name);
            insertMap.put("category", category);
            insertMap.put("purpose", purpose);
            insertMap.put("charter", charter);
            insertMap.put("userId", userId);
            insertMap.put("status", "pending");
            insertMap.put("createTime", new java.util.Date());
            insertMap.put("updateTime", new java.util.Date());
            if (attachmentPath != null) {
                insertMap.put("attachment", attachmentPath);
            }

            // 插入申请记录
            int result = clubCreateMapper.insert(insertMap);

            if (result > 0) {
                logger.info("用户[{}]提交社团创建申请成功，社团名称：{}", userId, name);
                return Result.build(null, ResultCodeEnum.SUCCESS.getCode(), "提交成功，请等待审核");
            } else {
                logger.warn("用户[{}]提交社团创建申请失败，社团名称：{}", userId, name);
                return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "提交失败，请稍后重试");
            }
        } catch (IOException e) {
            logger.error("处理附件上传失败", e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "附件上传失败");
        } catch (Exception e) {
            logger.error("提交社团创建申请失败", e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "提交失败，请稍后重试");
        }
    }

    @Override
    public Result<?> getMyClubCreateRequests(Long userId) {
        try {
            logger.info("查询用户[{}]发起的社团创建申请", userId);

            // 查询用户的社团创建申请列表
            List<Map<String, Object>> requests = clubCreateMapper.selectByUserId(userId);
            logger.info("查询用户[{}]发起的社团创建申请成功，共{}条记录", userId, requests.size());

            return Result.build(requests, ResultCodeEnum.SUCCESS.getCode(), "查询成功");
        } catch (Exception e) {
            logger.error("查询用户[{}]发起的社团创建申请失败", userId, e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "查询失败，请稍后重试");
        }
    }
}