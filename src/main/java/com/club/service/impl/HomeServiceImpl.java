package com.club.service.impl;

import com.club.entity.vo.ClubSimpleVO;
import com.club.entity.vo.Result;
import com.club.mapper.HomeMapper;
import com.club.service.HomeService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private HomeMapper homeMapper;

    @Override
    public Result<List<ClubSimpleVO>> getHotClubs() {
        try {
            log.info("开始获取热门社团数据");
            List<ClubSimpleVO> hotClubs = homeMapper.selectHotClubs(5);
            log.info("成功获取{}个热门社团", hotClubs.size());

            // 使用正确的静态工厂方法
            return Result.build(hotClubs, 200, "success");

        } catch (Exception e) {
            log.error("获取热门社团异常", e);
            return Result.build(null, 500, "获取热门社团失败");
        }
    }

    @Override
    public Result<List<ClubSimpleVO>> getPopularClubs() {
        try {
            log.info("开始获取人气社团数据");
            List<ClubSimpleVO> popularClubs = homeMapper.selectPopularClubs(3);
            log.info("成功获取{}个人气社团", popularClubs.size());

            // 使用正确的静态工厂方法
            return Result.build(popularClubs, 200, "success");

        } catch (Exception e) {
            log.error("获取人气社团异常", e);
            return Result.build(null, 500, "获取人气社团失败");
        }
    }
}