package com.club.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NotificationWebSocketHandler extends TextWebSocketHandler {
    
    // 存储用户ID与WebSocket会话的映射关系
    private static final ConcurrentHashMap<Integer, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 连接建立后触发
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 获取用户ID（可以通过查询参数或其他方式）
        Integer userId = getUserIdFromSession(session);
        if (userId != null) {
            userSessions.put(userId, session);
        }
    }
    
    /**
     * 处理收到的消息
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 可以处理客户端发来的消息，此处暂不处理
    }
    
    /**
     * 连接关闭后触发
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 移除已关闭的连接
        userSessions.values().remove(session);
    }
    
    /**
     * 发送消息给指定用户
     * @param userId 用户ID
     * @param notification 消息对象
     */
    public void sendMessageToUser(Integer userId, Object notification) {
        WebSocketSession session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                String jsonMessage = objectMapper.writeValueAsString(notification);
                session.sendMessage(new TextMessage(jsonMessage));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 发送消息给多个用户
     * @param userIds 用户ID列表
     * @param notification 消息对象
     */
    public void sendMessageToUsers(Iterable<Integer> userIds, Object notification) {
        for (Integer userId : userIds) {
            sendMessageToUser(userId, notification);
        }
    }
    
    /**
     * 从会话中获取用户ID
     * 此处可以根据实际需求修改获取用户ID的方式
     * @param session WebSocket会话
     * @return 用户ID
     */
    private Integer getUserIdFromSession(WebSocketSession session) {
        // 示例：从查询参数中获取用户ID
        // ws://localhost:8080/ws/notifications?userId=123
        String userIdParam = session.getUri().getQuery();
        if (userIdParam != null && userIdParam.startsWith("userId=")) {
            try {
                return Integer.valueOf(userIdParam.substring(7)); // "userId=".length() = 7
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    
    /**
     * 获取当前在线用户数
     * @return 在线用户数
     */
    public int getOnlineUserCount() {
        return userSessions.size();
    }
}