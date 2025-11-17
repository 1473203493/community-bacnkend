package com.club.service.impl;

import com.club.entity.Club;
import com.club.entity.request.ClubQueryDto;
import com.club.entity.vo.ClubCreateRequestVO;
import com.club.entity.vo.Result;

import com.club.mapper.ClubMapper;
import com.club.service.ClubService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ClubServiceImpl implements ClubService {

    @Autowired
    private ClubMapper clubMapper;

    @Override
    public Result<Void> createClub(ClubCreateRequestVO request) {
        try {
            // 检查社团名称是否重复
            int count = clubMapper.countByName(request.getName());
            if (count > 0) {
                return Result.build(null, 400, "社团名称已存在");
            }

            // VO → Entity
            Club club = new Club();
            club.setName(request.getName());
            club.setCategoryId(request.getCategoryId());
            club.setDescription(request.getDescription());
            club.setCharter(request.getCharter());
            club.setFounderId(request.getFounderId());
            club.setStatus("1"); // 待平台管理员确认
            club.setCreatedAt(LocalDateTime.now());

            // 插入数据库
            int rows = clubMapper.insertClub(club);
            if (rows > 0) {
                return Result.build(null, 200, "社团创建成功，等待管理员审核");
            } else {
                return Result.build(null, 500, "社团创建失败");
            }

        } catch (Exception e) {
            log.error("新增社团异常", e);
            return Result.build(null, 500, "新增社团失败：" + e.getMessage());
        }

    }

    @Override
    public PageInfo<Club> getClubList(ClubQueryDto queryDto) {
        // 只有当分页参数都不为空时才开启分页
        if (queryDto.getPageNum() != null && queryDto.getPageSize() != null) {
            PageHelper.startPage(queryDto.getPageNum(), queryDto.getPageSize());
        }
        // 执行查询
        List<Club> clubList = clubMapper.getClubList(queryDto);
        // 封装分页结果
        return new PageInfo<>(clubList);
    }

    @Override
    public Result<List<Club>> listMyClubs(Integer userId) {
        List<Club> clubs = clubMapper.selectByUserId(userId);
        return Result.build(clubs, 200, "查询成功");
    }

}
