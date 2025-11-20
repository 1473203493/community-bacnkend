package com.club.service.impl;

import com.club.entity.Notification;
import com.club.mapper.NotificationMapper;
import com.club.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    
    @Autowired
    private NotificationMapper notificationMapper;
    
    @Override
    public void save(Notification notification) {
        notificationMapper.insert(notification);
    }
}