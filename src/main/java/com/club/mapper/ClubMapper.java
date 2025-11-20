package com.club.mapper;

import com.club.entity.Club;
import com.club.entity.request.ClubQueryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClubMapper {
    List<Club> getClubList(ClubQueryDto queryDto);
}