package com.club.service;

import com.club.entity.Notification;
import com.club.websocket.NotificationWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private NotificationWebSocketHandler webSocketHandler;
    
    /**
     * 创建并发送系统通知
     * @param userId 接收用户ID
     * @param adminId 管理员ID（可选）
     * @param title 通知标题
     * @param content 通知内容
     */
    public void sendSystemNotification(Integer userId, Integer adminId, String title, String content) {
        // 保存通知到数据库
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setAdminId(adminId);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        
        // 保存到数据库
        notificationService.save(notification);
        
        // 通过WebSocket发送实时通知
        webSocketHandler.sendMessageToUser(userId, notification);
    }
    
    /**
     * 向多个用户发送系统通知
     * @param userIds 接收用户ID列表
     * @param adminId 管理员ID（可选）
     * @param title 通知标题
     * @param content 通知内容
     */
    public void sendSystemNotifications(List<Integer> userIds, Integer adminId, String title, String content) {
        // 保存通知到数据库
        for (Integer userId : userIds) {
            Notification notification = new Notification();
            notification.setUserId(userId);
            notification.setAdminId(adminId);
            notification.setTitle(title);
            notification.setContent(content);
            notification.setRead(false);
            notification.setCreatedAt(LocalDateTime.now());
            
            // 保存到数据库
            notificationService.save(notification);
            
            // 通过WebSocket发送实时通知
            webSocketHandler.sendMessageToUser(userId, notification);
        }
    }
    

}