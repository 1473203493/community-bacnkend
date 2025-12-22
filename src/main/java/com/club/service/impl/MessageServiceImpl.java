package com.club.service.impl;

import com.club.entity.*;
import com.club.entity.message.dto.MessageAdminExamineDto;
import com.club.entity.message.dto.MessageManagerDto;
import com.club.entity.message.dto.MessageManagerExamineDto;
import com.club.entity.message.dto.MessageUserDto;
import com.club.entity.message.replyvo.MessageAdminReplyVo;
import com.club.entity.message.replyvo.MessageManagerReplyVo;
import com.club.entity.message.vo.MessageManagerVO;
import com.club.entity.message.vo.MessageUserVo;
import com.club.entity.vo.Result;
import com.club.entity.vo.ResultCodeEnum;
import com.club.mapper.*;
import com.club.service.MessageService;
import com.club.service.NotificationService;
import com.club.util.AuthContextUtil;
import com.club.websocket.NotificationWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationWebSocketHandler webSocketHandler;

    @Autowired
    private ClubMemberMapper clubMemberMapper;

    @Autowired
    private ClubMapper clubMapper;

    @Autowired
    private ClubCategoryMapper clubCategoryMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private ActivitySignupMapper activitySignupMapper;

//    /**
//     * 创建并发送系统通知
//     *
//     * @param userId  接收用户ID
//     * @param adminId 管理员ID（可选）
//     * @param title   通知标题
//     * @param content 通知内容
//     */
//    public void sendSystemNotification(Integer userId, Integer adminId, String title, String content) {
//        try {
//            // 保存通知到数据库
//            // TODO: 实现通知保存逻辑
//            log.info("发送系统通知：userId={}, title={}", userId, title);
//        } catch (Exception e) {
//            log.error("发送系统通知失败", e);
//        }
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
//        try {
//            // TODO: 实现批量发送通知逻辑
//            log.info("批量发送系统通知：userIds={}, title={}", userIds, title);
//        } catch (Exception e) {
//            log.error("批量发送系统通知失败", e);
//        }
//    }

    @Override
    public Result<?> getMessageList(Long userId, Integer pageNum, Integer pageSize, String type) {
        try {
            // TODO: 实现消息列表查询逻辑
            log.info("获取消息列表：userId={}, pageNum={}, pageSize={}, type={}", userId, pageNum, pageSize, type);

            // 构建模拟数据
            Map<String, Object> result = new HashMap<>();
            List<Map<String, Object>> messages = new ArrayList<>();

            Map<String, Object> message1 = new HashMap<>();
            message1.put("id", 1L);
            message1.put("title", "社团活动通知");
            message1.put("content", "欢迎参加我们的社团活动，请准时到达！");
            message1.put("type", "activity");
            message1.put("isRead", false);
            message1.put("createdAt", LocalDateTime.now().minusDays(1));
            messages.add(message1);

            Map<String, Object> message2 = new HashMap<>();
            message2.put("id", 2L);
            message2.put("title", "入社申请通过");
            message2.put("content", "恭喜您，您的社团申请已通过审核！");
            message2.put("type", "application");
            message2.put("isRead", true);
            message2.put("createdAt", LocalDateTime.now().minusDays(2));
            messages.add(message2);

            result.put("list", messages);
            result.put("total", 2);
            result.put("pageNum", pageNum);
            result.put("pageSize", pageSize);

            return Result.build(result, ResultCodeEnum.SUCCESS.getCode(), "获取成功");
        } catch (Exception e) {
            log.error("获取消息列表失败", e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "获取消息列表失败");
        }
    }

    @Override
    public Result<?> getMessageDetail(Long messageId, Long userId) {
        try {
            // TODO: 实现消息详情查询逻辑
            log.info("获取消息详情：messageId={}, userId={}", messageId, userId);

            // 构建模拟数据
            Map<String, Object> message = new HashMap<>();
            message.put("id", messageId);
            message.put("title", "社团活动通知");
            message.put("content", "欢迎参加我们的社团活动，请准时到达！\n时间：2025-11-20 14:00\n地点：学生活动中心301室");
            message.put("type", "activity");
            message.put("isRead", false);
            message.put("createdAt", LocalDateTime.now().minusDays(1));

            return Result.build(message, ResultCodeEnum.SUCCESS.getCode(), "获取成功");
        } catch (Exception e) {
            log.error("获取消息详情失败", e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "获取消息详情失败");
        }
    }

    @Override
    public Result<?> markAsRead(Long messageId, Long userId) {
        try {
            // TODO: 实现标记已读逻辑
            log.info("标记消息已读：messageId={}, userId={}", messageId, userId);

            return Result.build(null, ResultCodeEnum.SUCCESS.getCode(), "标记成功");
        } catch (Exception e) {
            log.error("标记消息已读失败", e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "标记失败");
        }
    }

    @Override
    public Result<?> batchMarkAsRead(Long userId, Long[] messageIds) {
        try {
            // TODO: 实现批量标记已读逻辑
            log.info("批量标记消息已读：userId={}, messageIds={}", userId, Arrays.toString(messageIds));

            return Result.build(null, ResultCodeEnum.SUCCESS.getCode(), "批量标记成功");
        } catch (Exception e) {
            log.error("批量标记消息已读失败", e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "批量标记失败");
        }
    }

    @Override
    public Result<?> deleteMessage(Long messageId, Long userId) {
        try {
            // TODO: 实现删除消息逻辑
            log.info("删除消息：messageId={}, userId={}", messageId, userId);

            return Result.build(null, ResultCodeEnum.SUCCESS.getCode(), "删除成功");
        } catch (Exception e) {
            log.error("删除消息失败", e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "删除失败");
        }
    }

    @Override
    public Result<?> batchDeleteMessage(Long userId, Long[] messageIds) {
        try {
            // TODO: 实现批量删除消息逻辑
            log.info("批量删除消息：userId={}, messageIds={}", userId, Arrays.toString(messageIds));

            return Result.build(null, ResultCodeEnum.SUCCESS.getCode(), "批量删除成功");
        } catch (Exception e) {
            log.error("批量删除消息失败", e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "批量删除失败");
        }
    }

    @Override
    public Result<?> getUnreadCount(Long userId) {
        try {
            // TODO: 实现未读消息数量查询逻辑
            log.info("获取未读消息数量：userId={}", userId);

            // 模拟数据：返回3条未读消息
            Map<String, Object> result = new HashMap<>();
            result.put("count", 3);

            return Result.build(result, ResultCodeEnum.SUCCESS.getCode(), "获取成功");
        } catch (Exception e) {
            log.error("获取未读消息数量失败", e);
            return Result.build(null, ResultCodeEnum.SYSTEM_ERROR.getCode(), "获取未读消息数量失败");
        }
    }



    /**
     * 申请入社或申请参加活动
     * 创建并发送通知
     * 普通学生
     */
    @Override
    public void sendNotificationForAdd(MessageUserDto messageUserDto) {

        MessageUserVo messageUserVo = new MessageUserVo();

        //消息要传给社团管理员
        if(messageUserDto.getRole().equals("2")){

            // 获取当前用户
            User user = AuthContextUtil.getUser();

            messageUserVo.setTitle(messageUserDto.getTitle());
            messageUserVo.setContent(messageUserDto.getContent());
            if(messageUserDto.getClubId() != null && messageUserDto.getActivityId() == null){
                //申请加入社团（默认状态为待确定）
                ClubMember clubMember = new ClubMember();
                clubMember.setClubId(messageUserDto.getClubId());
                clubMember.setUserId(user.getUserId());
                clubMember.setRole("1");
                clubMember.setJoinStatus("1");
                clubMemberMapper.insert(clubMember);
                messageUserVo.setClubId(messageUserDto.getClubId());
            }else if(messageUserDto.getClubId() != null && messageUserDto.getActivityId() != null){
                //申请加入活动（默认状态为待确定）
                ActivitySignup activitySignup = new ActivitySignup();
                activitySignup.setActivityId(messageUserDto.getActivityId());
                activitySignup.setUserId(user.getUserId());
                activitySignup.setStatus("1");
                messageUserVo.setClubId(messageUserDto.getClubId());
                messageUserVo.setActivityId(messageUserDto.getActivityId());
            }

            // 复制user到messageUserVo
            messageUserVo.setUserId(user.getUserId());
            messageUserVo.setOpenid(user.getOpenid());
            messageUserVo.setStudentNo(user.getStudentNo());
            messageUserVo.setName(user.getName());
            messageUserVo.setEmail(user.getEmail());
            messageUserVo.setRole(user.getRole());
            messageUserVo.setStatus(user.getStatus());
            // 通过WebSocket发送实时通知
            webSocketHandler.sendMessageToUser(messageUserDto.getUserOrAdminId(), messageUserDto.getRole(), messageUserVo);
        }

    }

    /**
     * 申请创建社团或创建活动
     * 创建并发送通知
     * 社团管理员
     */
    @Override
    public void sendNotificationForCreate(MessageManagerDto messageManagerDto) {

        MessageManagerVO messageManagerVO = new MessageManagerVO();

        // 获取当前用户
        User user = AuthContextUtil.getUser();
        messageManagerVO.setUserId(user.getUserId());
        messageManagerVO.setOpenid(user.getOpenid());
        messageManagerVO.setStudentNo(user.getStudentNo());
        messageManagerVO.setName(user.getName());
        messageManagerVO.setEmail(user.getEmail());
        messageManagerVO.setRole(user.getRole());
        messageManagerVO.setStatus(user.getStatus());

        //消息要传给平台管理员
        if(messageManagerDto.getRole().equals("3")){

            messageManagerVO.setTitle(messageManagerDto.getTitle());
            messageManagerVO.setContent(messageManagerDto.getContent());
            //创建社团
            if(messageManagerDto.getClubName() != null && messageManagerDto.getClubCategory() != null
                    && messageManagerDto.getDescription() != null && messageManagerDto.getClubId() == null){

                //插入新社团的分类
                ClubCategory clubCategory = new ClubCategory();
                clubCategory.setName(messageManagerDto.getClubCategory());
                clubCategoryMapper.insertByAdmin(clubCategory);

                //插入新社团
                Club club = new Club();
                club.setName(messageManagerDto.getClubName());
                club.setCategoryId(clubCategory.getCategoryId());
                club.setDescription(messageManagerDto.getDescription());
                club.setStatus("1");  //默认为待确认状态
                clubMapper.insertClub(club);
                messageManagerVO.setClubId(club.getClubId());


            }else if(messageManagerDto.getActivityName() != null && messageManagerDto.getActivityLocation() != null
                    && messageManagerDto.getStartTime() != null && messageManagerDto.getEndTime() != null && messageManagerDto.getActivityDescription() != null
                    && messageManagerDto.getActivityNeedAudit() != null && messageManagerDto.getClubId() != null){
                //如果是新建活动
                messageManagerVO.setClubId(messageManagerDto.getClubId());

                //插入新活动
                Activity activity = new Activity();
                activity.setTitle(messageManagerDto.getActivityName());
                activity.setDescription(messageManagerDto.getActivityDescription());
                activity.setLocation(messageManagerDto.getActivityLocation());
                activity.setTime(messageManagerDto.getStartTime());
                activity.setEndTime(messageManagerDto.getEndTime());
                activity.setNeedAudit(messageManagerDto.getActivityNeedAudit());
                activity.setQuota(messageManagerDto.getActivityQuota());
                activity.setClubId(messageManagerDto.getClubId());
                activity.setStatus("1");  //默认为待确认状态
                activityMapper.insertActivity( activity);
                messageManagerVO.setActivityId(activity.getActivityId());


            }
            // 通过WebSocket发送实时通知
            webSocketHandler.sendMessageToUser(messageManagerDto.getUserOrAdminId(), messageManagerDto.getRole(), messageManagerVO);

        }

    }

    /**
     * 社团管理员审批普通学生申请加入社团或加入活动
     * 创建并发送通知
     * 社团管理员 -> 普通学生
     */
    @Override
    public void sendNotificationForExamine(MessageManagerExamineDto messageManagerExamineDto) {

        MessageManagerReplyVo messageManagerReplyVo = new MessageManagerReplyVo();

        // 获取当前用户
        User user = AuthContextUtil.getUser();
        messageManagerReplyVo.setUserId(user.getUserId());
        messageManagerReplyVo.setOpenid(user.getOpenid());
        messageManagerReplyVo.setStudentNo(user.getStudentNo());
        messageManagerReplyVo.setName(user.getName());
        messageManagerReplyVo.setEmail(user.getEmail());
        messageManagerReplyVo.setRole(user.getRole());
        messageManagerReplyVo.setStatus(user.getStatus());
        messageManagerReplyVo.setResult(messageManagerExamineDto.getResult());

        //消息要传给普通学生
        if(messageManagerExamineDto.getRole().equals("1")){
            messageManagerReplyVo.setTitle(messageManagerExamineDto.getTitle());
            messageManagerReplyVo.setContent(messageManagerExamineDto.getContent());
            //同意加入社团
            if(messageManagerExamineDto.getResult() && messageManagerExamineDto.getClubId() != null && messageManagerExamineDto.getActivityId() ==  null){
                messageManagerReplyVo.setClubId(messageManagerExamineDto.getClubId());
                //变更社团-成员表状态
                ClubMember clubMember = clubMemberMapper.selectMemberByUserIdAndClubId(messageManagerExamineDto.getClubId(), messageManagerExamineDto.getUserOrAdminId());
                clubMember.setJoinStatus("2");
                clubMemberMapper.updateMemberJoinStatus(clubMember);

                messageManagerReplyVo.setClubId(messageManagerExamineDto.getClubId());
            } else if(messageManagerExamineDto.getResult().equals( false) && messageManagerExamineDto.getClubId() != null && messageManagerExamineDto.getActivityId() ==  null){
                //拒绝加入社团
                ClubMember clubMember = clubMemberMapper.selectMemberByUserIdAndClubId(messageManagerExamineDto.getClubId(), messageManagerExamineDto.getUserOrAdminId());
                clubMember.setJoinStatus("3");
                clubMemberMapper.updateMemberJoinStatus(clubMember);

                messageManagerReplyVo.setClubId(messageManagerExamineDto.getClubId());
            }else if(messageManagerExamineDto.getResult() && messageManagerExamineDto.getClubId() != null && messageManagerExamineDto.getActivityId() !=  null){
                //同意加入活动
                ActivitySignup activitySignup = activitySignupMapper.selectSignupByUserIdAndActivityId(messageManagerExamineDto.getActivityId(), messageManagerExamineDto.getUserOrAdminId());
                activitySignup.setStatus("2");
                activitySignupMapper.updateSignupStatus(activitySignup);

                messageManagerReplyVo.setClubId(messageManagerExamineDto.getClubId());
                messageManagerReplyVo.setActivityId(messageManagerExamineDto.getActivityId());
            }else if(messageManagerExamineDto.getResult().equals( false) && messageManagerExamineDto.getClubId() != null && messageManagerExamineDto.getActivityId() !=  null){
                //拒绝加入活动
                ActivitySignup activitySignup = activitySignupMapper.selectSignupByUserIdAndActivityId(messageManagerExamineDto.getActivityId(), messageManagerExamineDto.getUserOrAdminId());
                activitySignup.setStatus("3");
                activitySignupMapper.updateSignupStatus(activitySignup);

                messageManagerReplyVo.setClubId(messageManagerExamineDto.getClubId());
                messageManagerReplyVo.setActivityId(messageManagerExamineDto.getActivityId());
            }
        }

        // 通过WebSocket发送实时通知
        webSocketHandler.sendMessageToUser(messageManagerExamineDto.getUserOrAdminId(), messageManagerReplyVo.getRole(), messageManagerReplyVo);



    }

    /**
     * 平台管理员审批社团管理员申请创建社团或创建活动
     * 创建并发送通知
     * 平台管理员 -> 社团管理员
     */
    @Override
    public void sendNotificationForExamineV2(MessageAdminExamineDto messageAdminExamineDto) {

        MessageAdminReplyVo messageAdminReplyVo = new MessageAdminReplyVo();

        // 获取当前用户（平台管理员）
        Admin admin = AuthContextUtil.get();
        messageAdminReplyVo.setUserId(admin.getUserId());
        messageAdminReplyVo.setAdminNo(admin.getAdminNo());
        messageAdminReplyVo.setName(admin.getName());
        messageAdminReplyVo.setEmail(admin.getEmail());
        messageAdminReplyVo.setRole(admin.getRole());
        messageAdminReplyVo.setStatus(admin.getStatus());
        messageAdminReplyVo.setResult(messageAdminExamineDto.getResult());

        //消息要传给社团管理员
        if(messageAdminExamineDto.getRole().equals("2")){
            messageAdminReplyVo.setTitle(messageAdminExamineDto.getTitle());
            messageAdminReplyVo.setContent(messageAdminExamineDto.getContent());

            //同意创建社团
            if(messageAdminExamineDto.getResult() && messageAdminExamineDto.getClubId() != null && messageAdminExamineDto.getActivityId() ==  null){
                messageAdminReplyVo.setClubId(messageAdminExamineDto.getClubId());
                //变更社团状态
                Club club = clubMapper.selectById(messageAdminExamineDto.getClubId());
                club.setStatus("2");
                clubMapper.updateStatus(club);
            }else if(messageAdminExamineDto.getResult().equals( false) && messageAdminExamineDto.getClubId() != null && messageAdminExamineDto.getActivityId() ==  null){
                //拒绝创建社团
                messageAdminReplyVo.setClubId(messageAdminExamineDto.getClubId());

                Club club = clubMapper.selectById(messageAdminExamineDto.getClubId());
                club.setStatus("4");
                clubMapper.updateStatus(club);
            }else if(messageAdminExamineDto.getResult() && messageAdminExamineDto.getClubId() != null && messageAdminExamineDto.getActivityId() !=  null){
                //同意创建活动
                messageAdminReplyVo.setClubId(messageAdminExamineDto.getClubId());
                messageAdminReplyVo.setActivityId(messageAdminExamineDto.getActivityId());
                //变更活动状态
                Activity activity = activityMapper.selectById(messageAdminExamineDto.getActivityId());
                activity.setStatus("2");
                activityMapper.updateStatus(activity);
            }else if(messageAdminExamineDto.getResult().equals( false) && messageAdminExamineDto.getClubId() != null && messageAdminExamineDto.getActivityId() !=  null){
                //拒绝创建活动
                messageAdminReplyVo.setClubId(messageAdminExamineDto.getClubId());
                messageAdminReplyVo.setActivityId(messageAdminExamineDto.getActivityId());

                Activity activity = activityMapper.selectById(messageAdminExamineDto.getActivityId());
                activity.setStatus("5");
                activityMapper.updateStatus(activity);
            }

            // 通过WebSocket发送实时通知
            webSocketHandler.sendMessageToUser(messageAdminExamineDto.getUserOrAdminId(), messageAdminExamineDto.getRole(), messageAdminReplyVo);

        }

    }

}