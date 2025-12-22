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

    // 定义角色类型常量
    public static final String ROLE_USER = "1";
    public static final String ROLE_MANAGER = "2";
    public static final String ROLE_ADMIN = "3";

    // 用户标识类，用于区分用户和管理员
    public static class UserIdentifier {
        private Integer id;
        private String role;

        public UserIdentifier(Integer id, String role) {
            this.id = id;
            this.role = role;
        }

        public Integer getId() {
            return id;
        }

        public String getRole() {
            return role;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserIdentifier that = (UserIdentifier) o;
            return id.equals(that.id) && role.equals(that.role);
        }

        @Override
        public int hashCode() {
            return id.hashCode() * 31 + role.hashCode();
        }
    }

    // 存储用户标识与WebSocket会话的映射关系
    private static final ConcurrentHashMap<UserIdentifier, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 连接建立后触发
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 获取用户标识（包括ID和角色）
        UserIdentifier userIdentifier = getUserIdentifierFromSession(session);
        if (userIdentifier != null) {
            userSessions.put(userIdentifier, session);
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
     * @param role 用户角色 (user 或 admin)
     * @param notification 消息对象
     */
    public void sendMessageToUser(Integer userId, String role, Object notification) {
        UserIdentifier identifier = new UserIdentifier(userId, role);
        WebSocketSession session = userSessions.get(identifier);
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
     * @param role 用户角色 (user 或 admin)
     * @param notification 消息对象
     */
    public void sendMessageToUsers(Iterable<Integer> userIds, String role, Object notification) {
        for (Integer userId : userIds) {
            sendMessageToUser(userId, role, notification);
        }
    }

    /**
     * 从会话中获取用户标识
     * @param session WebSocket会话
     * @return 用户标识
     */
    private UserIdentifier getUserIdentifierFromSession(WebSocketSession session) {
        // 示例：从查询参数中获取用户ID和角色
        // ws://localhost:8080/ws/notifications?userOrAdminID=123&role="1"
        String queryParams = session.getUri().getQuery();
        if (queryParams != null) {
            String[] params = queryParams.split("&");
            Integer userOrAdminID = null;
            String role = null;

            for (String param : params) {
                if (param.startsWith("userOrAdminID=")) {
                    try {
                        userOrAdminID = Integer.valueOf(param.substring(14)); // "userOrAdminID=".length() = 14
                    } catch (NumberFormatException e) {
                        // 解析失败，继续处理其他参数
                    }
                } else if (param.startsWith("role=")) {
                    role = param.substring(5); // "role=".length() = 5
                }
            }

            if (userOrAdminID != null && role != null) {
                return new UserIdentifier(userOrAdminID, role);
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
