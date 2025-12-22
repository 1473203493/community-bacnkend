package com.club.controller.manager;

import com.club.entity.Notification;
import com.club.entity.message.dto.MessageManagerDto;
import com.club.entity.message.dto.MessageManagerExamineDto;
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
 * @date 2025/12/21 16:55
 */
@RestController("managerMessageController")
@RequestMapping("/manager/message")
@Tag(name = "社团管理员消息管理接口")
@Slf4j
public class MessageController {

    @Autowired
    private NotificationWebSocketHandler notificationWebSocketHandler;

    @Autowired
    private MessageService messageService;

    @Autowired
    private NotificationService notificationService;

    /**
     * 申请创建活动或创建社团
     */
    @PostMapping("/applyCreate/ClubOrActivity")
    public Result<String> applyCreateActivityOrClub(@RequestBody MessageManagerDto messageManagerDto) {

        // 判断接收角色是管理员
        // 如果传回来的role是平台管理员，则操作为社团管理员申请创建社团或创建活动
        if(messageManagerDto.getRole().equals("3")){
            //发送websocket通知
            messageService.sendNotificationForCreate(messageManagerDto);

            //插入数据库的消息表一条记录
            Notification notification = new Notification();
            //接收人id
            notification.setUserId(messageManagerDto.getUserOrAdminId());
            //接收人的角色
            notification.setRole(messageManagerDto.getRole());
            notification.setTitle(messageManagerDto.getTitle());
            notification.setContent(messageManagerDto.getContent());
            notification.setRead(false);
            notification.setCreatedAt(LocalDateTime.now());
            notificationService.save(notification);

        }
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    /**
     * 审批普通学生入社或参加活动
     */
    @PostMapping("/approve/ClubOrActivity")
    public Result<String> approveClubOrActivity(@RequestBody MessageManagerExamineDto messageManagerExamineDto) {

        // 判断接收角色是普通学生
        if(messageManagerExamineDto.getRole().equals("1")){
            //发送websocket通知
            messageService.sendNotificationForExamine(messageManagerExamineDto);
        }

        //插入数据库的消息表一条记录
        Notification notification = new Notification();
        //接收人id
        notification.setUserId(messageManagerExamineDto.getUserOrAdminId());
        //接收人的角色
        notification.setRole(messageManagerExamineDto.getRole());
        notification.setTitle(messageManagerExamineDto.getTitle());
        notification.setContent(messageManagerExamineDto.getContent());
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notificationService.save(notification);

        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
}
