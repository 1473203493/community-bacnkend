package com.club.aspect;

import com.club.entity.OperationLog;
import com.club.service.OperationLogService;
import com.club.util.AuthContextUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Aspect
@Component
public class OperationLogAspect {
    
    @Autowired
    private OperationLogService operationLogService;
    
    /**
     * 环绕通知处理日志记录
     * @param joinPoint 连接点
     * @param logOperation 日志操作注解
     * @return 方法执行结果
     * @throws Throwable 异常
     */
    @Around("@annotation(logOperation)")
    public Object logOperation(ProceedingJoinPoint joinPoint, LogOperation logOperation) throws Throwable {
        // 获取当前时间
        LocalDateTime startTime = LocalDateTime.now();
        
        // 执行目标方法
        Object result = joinPoint.proceed();
        
        // 记录操作日志
        try {
            saveOperationLog(joinPoint, logOperation, startTime);
        } catch (Exception e) {
            // 日志记录失败不应该影响主业务流程
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * 保存操作日志
     * @param joinPoint 连接点
     * @param logOperation 日志操作注解
     * @param startTime 开始时间
     */
    private void saveOperationLog(ProceedingJoinPoint joinPoint, LogOperation logOperation, LocalDateTime startTime) {
        OperationLog operationLog = new OperationLog();
        
        // 设置操作时间
        operationLog.setCreatedAt(startTime);
        
        // 获取当前用户信息
        Long userId = AuthContextUtil.getCurrentUserId();
        String role = AuthContextUtil.getCurrentUserRole();
        
        if (userId != null) {
            if ("admin".equals(role)) {
                operationLog.setAdminId(userId.intValue());
            } else {
                operationLog.setUserId(userId.intValue());
            }
        }
        
        // 设置操作描述
        String action = logOperation.value();
        if (action == null || action.isEmpty()) {
            action = joinPoint.getSignature().getName();
        }
        operationLog.setAction(action);
        
        // 获取请求IP地址
        String ipAddress = getClientIpAddress();
        operationLog.setIpAddress(ipAddress);
        
        // 保存日志
        operationLogService.saveOperationLog(operationLog);
    }
    
    /**
     * 获取客户端IP地址
     * @return IP地址
     */
    private String getClientIpAddress() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String ip = request.getHeader("X-Forwarded-For");
                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("X-Real-IP");
                }
                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }
                return ip;
            }
        } catch (Exception e) {
            // 忽略异常
        }
        return "unknown";
    }
}