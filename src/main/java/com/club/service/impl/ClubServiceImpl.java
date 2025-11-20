package com.club.service.impl;

import com.club.entity.Club;
import com.club.entity.request.ClubQueryDto;
import com.club.mapper.ClubMapper;
import com.club.service.ClubService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubServiceImpl implements ClubService {

    @Autowired
    private ClubMapper clubMapper;

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
}