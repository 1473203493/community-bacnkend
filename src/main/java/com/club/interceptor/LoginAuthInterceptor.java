package com.club.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;

import com.club.entity.vo.Result;
import com.club.entity.Admin;
import com.club.entity.vo.ResultCodeEnum;
import com.club.util.AuthContextUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * @author ljl
 * @create 2023-10-25-12:10
 */
@Component
public class LoginAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String , String> redisTemplate ;

    //在目标方法之前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // 获取请求方式
        String method = request.getMethod();
        if("OPTIONS".equals(method)) {      // 如果是跨域预检请求，直接放行
            return true ;
        }


        // 获取token (修改为从Authorization头中获取Bearer token)
        String token = null;
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // 去掉 "Bearer " 前缀
        }

        if(StrUtil.isEmpty(token)) {
            responseNoLoginInfo(response) ;
            return false ;
        }


        // 如果token不为空，那么此时验证token的合法性
        String Admin = redisTemplate.opsForValue().get("admin:login:" + token);
        if(StrUtil.isEmpty(Admin)) {
            responseNoLoginInfo(response) ;
            return false ;
        }

        // 将用户数据存储到ThreadLocal中
        Admin admin = JSON.parseObject(Admin, Admin.class);
        AuthContextUtil.set(admin);

        // 重置Redis中的用户数据的有效时间
        redisTemplate.expire("admin:login:" + token , 30 , TimeUnit.MINUTES) ;

        // 放行
        return true ;
    }

    //响应208状态码给前端
    private void responseNoLoginInfo(HttpServletResponse response) {
        Result<Object> result = Result.build(null, ResultCodeEnum.LOGIN_AUTH);
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(JSON.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    //等方法全部完成后执行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AuthContextUtil.remove();  // 移除threadLocal中的数据
    }
}
