package com.club.service;

import com.club.entity.Club;
import com.club.entity.request.ClubQueryDto;
import com.github.pagehelper.PageInfo;

public interface ClubService {
    PageInfo<Club> getClubList(ClubQueryDto queryDto);
}