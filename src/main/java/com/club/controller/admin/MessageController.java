package com.club.controller.admin;

import com.club.entity.Notification;
import com.club.entity.message.dto.MessageAdminExamineDto;
import com.club.entity.vo.Result;
import com.club.entity.vo.ResultCodeEnum;
import com.club.service.MessageService;
import com.club.service.NotificationService;
import com.club.websocket.NotificationWebSocketHandler;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author zyh
 * @date 2025/12/21 16:54
 */
@RestController("adminMessageController")
@RequestMapping("/admin/message")
@Tag(name = "平台管理员消息管理接口")
@Slf4j
public class MessageController {

    @Autowired
    private NotificationWebSocketHandler notificationWebSocketHandler;

    @Autowired
    private MessageService messageService;

    @Autowired
    private NotificationService notificationService;

    /**
     * 审批创建活动或创建社团
     */
    @PostMapping("/approve/ClubOrActivity")
    public Result<String> approveClubOrActivity(@RequestBody MessageAdminExamineDto messageAdminExamineDto) {

        // 判断接收的角色是社团管理员
        if(messageAdminExamineDto.getRole().equals("2")){
            //发送websocket通知
            messageService.sendNotificationForExamineV2(messageAdminExamineDto);
        }

        //插入数据库的消息表一条记录
        Notification notification = new Notification();
        //接收人id
        notification.setUserId(messageAdminExamineDto.getUserOrAdminId());
        //接收人的角色
        notification.setRole(messageAdminExamineDto.getRole());
        notification.setTitle(messageAdminExamineDto.getTitle());
        notification.setContent(messageAdminExamineDto.getContent());
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notificationService.save(notification);

        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

}

