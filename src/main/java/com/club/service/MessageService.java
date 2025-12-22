package com.club.service;

import com.club.entity.message.dto.MessageAdminExamineDto;
import com.club.entity.message.dto.MessageManagerDto;
import com.club.entity.message.dto.MessageManagerExamineDto;
import com.club.entity.message.dto.MessageUserDto;
import com.club.entity.vo.Result;

/**
 * 消息服务接口
 * @author zyh
 * @date 2025/11/11
 */
public interface MessageService {

    /**
     * 获取消息列表
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param type 消息类型（可选）
     * @return 消息列表
     */
    Result<?> getMessageList(Long userId, Integer pageNum, Integer pageSize, String type);

    /**
     * 获取消息详情
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 消息详情
     */
    Result<?> getMessageDetail(Long messageId, Long userId);

    /**
     * 标记消息为已读
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 操作结果
     */
    Result<?> markAsRead(Long messageId, Long userId);

    /**
     * 批量标记消息为已读
     * @param userId 用户ID
     * @param messageIds 消息ID数组
     * @return 操作结果
     */
    Result<?> batchMarkAsRead(Long userId, Long[] messageIds);

    /**
     * 删除消息
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 操作结果
     */
    Result<?> deleteMessage(Long messageId, Long userId);

    /**
     * 批量删除消息
     * @param userId 用户ID
     * @param messageIds 消息ID数组
     * @return 操作结果
     */
    Result<?> batchDeleteMessage(Long userId, Long[] messageIds);

    /**
     * 获取未读消息数量
     * @param userId 用户ID
     * @return 未读消息数量
     */
    Result<?> getUnreadCount(Long userId);

    /**
     * 申请入社或申请参加活动
     * 创建并发送通知
     * 普通学生 -> 社团管理员
     */
    void sendNotificationForAdd(MessageUserDto messageUserDto);

    /**
     * 申请创建社团或创建活动
     * 创建并发送通知
     * 社团管理员 -> 平台管理员
     */
    void sendNotificationForCreate(MessageManagerDto messageManagerDto);

    /**
     * 社团管理员审批普通学生申请加入社团或加入活动
     * 创建并发送通知
     * 社团管理员 -> 普通学生
     */
    void sendNotificationForExamine(MessageManagerExamineDto messageManagerExamineDto);

    /**
     * 平台管理员审批社团管理员申请创建社团或创建活动
     * 创建并发送通知
     * 平台管理员 -> 社团管理员
     */
    void sendNotificationForExamineV2(MessageAdminExamineDto messageAdminExamineDto);
}



//@Service
//public class MessageService {
//
//    @Autowired
//    private NotificationService notificationService;
//
//    @Autowired
//    private NotificationWebSocketHandler webSocketHandler;
//
//    /**
//     * 创建并发送系统通知
//     *
//     * @param userId  接收用户ID
//     * @param adminId 管理员ID（可选）
//     * @param title   通知标题
//     * @param content 通知内容
//     */
//    public void sendSystemNotification(Integer userId, Integer adminId, String title, String content) {
//        // 保存通知到数据库
//        Notification notification = new Notification();
//        notification.setUserId(userId);
//        notification.setAdminId(adminId);
//        notification.setTitle(title);
//        notification.setContent(content);
//        notification.setRead(false);
//        notification.setCreatedAt(LocalDateTime.now());
//
//        // 保存到数据库
//        notificationService.save(notification);
//
//        // 通过WebSocket发送实时通知
//        webSocketHandler.sendMessageToUser(userId, notification);
//    }
//
//    /**
//     * 向多个用户发送系统通知
//     *
//     * @param userIds 接收用户ID列表
//     * @param adminId 管理员ID（可选）
//     * @param title   通知标题
//     * @param content 通知内容
//     */
//    public void sendSystemNotifications(List<Integer> userIds, Integer adminId, String title, String content) {
//        // 保存通知到数据库
//        for (Integer userId : userIds) {
//            Notification notification = new Notification();
//            notification.setUserId(userId);
//            notification.setAdminId(adminId);
//            notification.setTitle(title);
//            notification.setContent(content);
//            notification.setRead(false);
//            notification.setCreatedAt(LocalDateTime.now());
//
//            // 保存到数据库
//            notificationService.save(notification);
//
//            // 通过WebSocket发送实时通知
//            webSocketHandler.sendMessageToUser(userId, notification);
//        }
//    }
//
//}