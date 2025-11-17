package com.club.service;

import com.club.entity.Club;
import com.club.entity.vo.ClubCreateRequestVO;
import com.club.entity.vo.Result;

import java.util.List;

public interface ClubService {

    Result<Void> createClub(ClubCreateRequestVO request);
    Result<List<Club>> listMyClubs(Integer userId);

}