package com.club.interceptor;


import com.club.entity.User;
import com.club.mapper.UserMapper;
import com.club.util.AuthContextUtil;
import com.club.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;



@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            // 当前拦截到的不是动态方法，直接放行
            return true;
        }

        // 从请求头中获取令牌
        String token = request.getHeader("token");

        // 判断令牌是否存在，不存在直接返回错误
        if (token == null || token.isEmpty()) {
            response.setStatus(401);
            return false;
        }

        // 校验令牌，解析失败直接返回错误
        try {
            // 使用与UserServiceImpl中相同的密钥
            String secret = "club-key"; // 实际应从配置文件或环境变量中获取
            // 修正参数顺序：先密钥后token
            Claims claims = JwtUtil.parseJWT(secret, token);
            // 打印token和解析结果，帮助调试
            log.info("收到token: {}", token);
            log.info("解析结果: {}", claims);
            
            Long userId = Long.valueOf(claims.get("userId").toString());

            // 根据用户ID查询用户信息
            User user = userMapper.getUserById(userId);

            // 将用户信息存储到ThreadLocal中
            AuthContextUtil.setUser(user);

            // 放行
            return true;
        } catch (Exception ex) {
            log.error("令牌解析失败: {}", ex.getMessage());
            response.setStatus(401);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清理ThreadLocal中的用户信息，防止内存泄漏
        AuthContextUtil.removeUser();
    }
}
