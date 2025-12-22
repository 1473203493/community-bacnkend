package com.club.controller.student;

import com.club.entity.Notification;
import com.club.entity.message.dto.MessageUserDto;
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
 * @date 2025/12/21 16:56
 */
@RestController("studentWebSocketMessageController")
@RequestMapping("/student/message")
@Tag(name = "普通学生消息管理接口")
@Slf4j
public class MessageWebSocketController {

    @Autowired
    private NotificationWebSocketHandler notificationWebSocketHandler;

    @Autowired
    private MessageService messageService;

    @Autowired
    private NotificationService notificationService;


    /**
     * 申请加入社团或加入活动
     */
    @PostMapping("/apply/Join")
    public Result<String> applyJoinClub(@RequestBody MessageUserDto messageUserDto) {

        // 判断接收角色是社团管理员
        // 如果传回来的role是社团管理员，则操作为普通学生申请加入社团或加入活动
        if(messageUserDto.getRole().equals("2")){
            //发送websocket通知
            messageService.sendNotificationForAdd(messageUserDto);

            //插入数据库的消息表一条记录
            Notification notification = new Notification();
            //接收人id
            notification.setUserId(messageUserDto.getUserOrAdminId());
            //接收人的角色
            notification.setRole(messageUserDto.getRole());
            notification.setTitle(messageUserDto.getTitle());
            notification.setContent(messageUserDto.getContent());
            notification.setRead(false);
            notification.setCreatedAt(LocalDateTime.now());
            notificationService.save(notification);

        }
        return Result.build(null, ResultCodeEnum.SUCCESS);

    }

}
