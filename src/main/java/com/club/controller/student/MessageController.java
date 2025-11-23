package com.club.controller.student;

import com.club.entity.vo.Result;
import com.club.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 学生端消息中心控制器
 * @author zyh
 * @date 2025/11/11
 */
@Slf4j
@Tag(name = "消息中心接口")
@RestController
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 获取消息列表
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param type 消息类型（可选）
     * @return 消息列表
     */
    @Operation(summary = "获取消息列表")
    @GetMapping("/list")
    public Result<?> getMessageList(@RequestParam Long userId,
                                    @RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(required = false) String type) {
        log.info("获取消息列表请求：userId={}, pageNum={}, pageSize={}, type={}", userId, pageNum, pageSize, type);
        return messageService.getMessageList(userId, pageNum, pageSize, type);
    }

    /**
     * 获取消息详情
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 消息详情
     */
    @Operation(summary = "获取消息详情")
    @GetMapping("/detail/{messageId}")
    public Result<?> getMessageDetail(@PathVariable Long messageId, @RequestParam Long userId) {
        log.info("获取消息详情请求：messageId={}, userId={}", messageId, userId);
        return messageService.getMessageDetail(messageId, userId);
    }

    /**
     * 标记消息为已读
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 操作结果
     */
    @Operation(summary = "标记消息为已读")
    @PutMapping("/read/{messageId}")
    public Result<?> markAsRead(@PathVariable Long messageId, @RequestParam Long userId) {
        log.info("标记消息已读请求：messageId={}, userId={}", messageId, userId);
        return messageService.markAsRead(messageId, userId);
    }

    /**
     * 批量标记消息为已读
     * @param request 批量标记请求
     * @return 操作结果
     */
    @Operation(summary = "批量标记消息为已读")
    @PutMapping("/read/batch")
    public Result<?> batchMarkAsRead(@RequestBody BatchMarkRequest request) {
        log.info("批量标记消息已读请求：userId={}, messageIds={}", request.getUserId(), request.getMessageIds());
        return messageService.batchMarkAsRead(request.getUserId(), request.getMessageIds());
    }

    /**
     * 删除消息
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 操作结果
     */
    @Operation(summary = "删除消息")
    @DeleteMapping("/{messageId}")
    public Result<?> deleteMessage(@PathVariable Long messageId, @RequestParam Long userId) {
        log.info("删除消息请求：messageId={}, userId={}", messageId, userId);
        return messageService.deleteMessage(messageId, userId);
    }

    /**
     * 批量删除消息
     * @param request 批量删除请求
     * @return 操作结果
     */
    @Operation(summary = "批量删除消息")
    @DeleteMapping("/batch")
    public Result<?> batchDeleteMessage(@RequestBody BatchDeleteRequest request) {
        log.info("批量删除消息请求：userId={}, messageIds={}", request.getUserId(), request.getMessageIds());
        return messageService.batchDeleteMessage(request.getUserId(), request.getMessageIds());
    }

    /**
     * 获取未读消息数量
     * @param userId 用户ID
     * @return 未读消息数量
     */
    @Operation(summary = "获取未读消息数量")
    @GetMapping("/unread/count")
    public Result<?> getUnreadCount(@RequestParam Long userId) {
        log.info("获取未读消息数量请求：userId={}", userId);
        return messageService.getUnreadCount(userId);
    }

    // 请求参数类
    public static class BatchMarkRequest {
        private Long userId;
        private Long[] messageIds;

        // getter and setter
        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long[] getMessageIds() {
            return messageIds;
        }

        public void setMessageIds(Long[] messageIds) {
            this.messageIds = messageIds;
        }
    }

    public static class BatchDeleteRequest {
        private Long userId;
        private Long[] messageIds;

        // getter and setter
        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long[] getMessageIds() {
            return messageIds;
        }

        public void setMessageIds(Long[] messageIds) {
            this.messageIds = messageIds;
        }
    }
}