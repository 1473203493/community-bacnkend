package com.club.service;

import com.club.entity.Notification;

public interface NotificationService {
    /**
     * 保存通知
     * @param notification 通知实体
     */
    void save(Notification notification);
}