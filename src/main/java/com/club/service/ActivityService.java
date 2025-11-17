package com.club.service;

import com.club.entity.Activity;
import com.club.entity.vo.ActivityCreateRequestVO;
import com.club.entity.vo.Result;

import java.util.List;

public interface ActivityService {
    Result<Void> createActivity(ActivityCreateRequestVO request);

    Result<List<Activity>> listActivitiesByClub(Integer clubId);


}