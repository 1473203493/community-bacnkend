package com.club.mapper;

import com.club.entity.Notification;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotificationMapper {
    /**
     * 插入通知记录
     * @param notification 通知实体
     */
    void insert(Notification notification);
}