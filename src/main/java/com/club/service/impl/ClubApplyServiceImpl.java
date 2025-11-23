package com.club.service.impl;

import com.club.entity.vo.Result;
import com.club.entity.vo.ResultCodeEnum;
import com.club.mapper.ClubApplyMapper;
import com.club.service.ClubApplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 入社申请服务实现类
 * @author zyh
 * @date 2025/11/11
 */
@Service
public class ClubApplyServiceImpl implements ClubApplyService {

    private static final Logger logger = LoggerFactory.getLogger(ClubApplyServiceImpl.class);

    @Autowired
    private ClubApplyMapper clubApplyMapper;

    @Override
    public Result<?> getMyClubApplies(Long userId) {
        try {
            logger.info("查询用户[{}]的入社申请列表", userId);
            // 查询用户的入社申请列表
            List<Map<String, Object>> applies = clubApplyMapper.selectByUserId(userId);
            logger.info("查询用户[{}]的入社申请列表成功，共{}条记录", userId, applies.size());
            return Result.build(applies, ResultCodeEnum.SUCCESS.getCode(), "查询成功");
        } catch (Exception e) {
            logger.error("查询用户[{}]的入社申请列表失败", userId, e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "查询失败");
        }
    }

    @Override
    public Result<?> cancelClubApply(Long applyId, Long userId) {
        try {
            logger.info("用户[{}]撤销入社申请[{}]", userId, applyId);

            // 验证申请是否存在且属于当前用户
            Map<String, Object> apply = clubApplyMapper.selectByIdAndUserId(applyId, userId);
            if (apply == null) {
                logger.warn("入社申请[{}]不存在或不属于用户[{}]", applyId, userId);
                return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "入社申请不存在或不属于当前用户");
            }

            // 检查申请状态，只有待审核的申请才能撤销
            String status = apply.get("status") != null ? apply.get("status").toString() : "";
            if (!"pending".equals(status)) {
                logger.warn("入社申请[{}]状态为[{}]，无法撤销", applyId, status);
                return Result.build(null, ResultCodeEnum.PARAM_ERROR.getCode(), "只有待审核的申请才能撤销");
            }

            // 更新申请状态为已撤销
            Map<String, Object> updateMap = new HashMap<>();
            updateMap.put("id", applyId);
            updateMap.put("status", "cancelled");
            int result = clubApplyMapper.updateStatus(updateMap);

            if (result > 0) {
                logger.info("用户[{}]撤销入社申请[{}]成功", userId, applyId);
                return Result.build(null, ResultCodeEnum.SUCCESS.getCode(), "撤销成功");
            } else {
                logger.warn("用户[{}]撤销入社申请[{}]失败", userId, applyId);
                return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "撤销失败");
            }
        } catch (Exception e) {
            logger.error("用户[{}]撤销入社申请[{}]失败", userId, applyId, e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "撤销失败");
        }
    }
}