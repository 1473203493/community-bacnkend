package com.club.service.impl;

import com.club.entity.vo.Result;
import com.club.entity.vo.ResultCodeEnum;
import com.club.mapper.ClubCategoryMapper;
import com.club.service.ClubCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 社团分类服务实现类
 * @author zyh
 * @date 2025/11/11
 */
@Service
public class ClubCategoryServiceImpl implements ClubCategoryService {

    private static final Logger logger = LoggerFactory.getLogger(ClubCategoryServiceImpl.class);

    @Autowired
    private ClubCategoryMapper clubCategoryMapper;

    @Override
    public Result<?> getClubCategories() {
        try {
            logger.info("开始查询社团分类列表");
            List<?> categories = clubCategoryMapper.selectAll();
            logger.info("查询社团分类列表成功，共{}条记录", categories.size());
            return Result.build(categories, ResultCodeEnum.SUCCESS.getCode(), "查询成功");
        } catch (Exception e) {
            logger.error("查询社团分类列表失败", e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "查询失败");
        }
    }
}