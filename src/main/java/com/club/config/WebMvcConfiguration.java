package com.club.config;



import com.club.interceptor.JwtTokenUserInterceptor;
import com.club.interceptor.LoginAuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private JwtTokenUserInterceptor jwtTokenUserInterceptor ;


    @Autowired
    private LoginAuthInterceptor loginAuthInterceptor ;




    //拦截器注册，正常调试的时候可以注释掉
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 平台管理员端拦截器，只拦截/admin/**路径 ，
//        registry.addInterceptor(loginAuthInterceptor)
//                .excludePathPatterns("/admin/login" ,
//                        "/admin/generateValidateCode") //开放登录接口和验证码接口
//                .addPathPatterns("/admin/**");  // 只拦截/admin开头的请求

        // 注册用户端（学生或社团管理员）拦截器
        registry.addInterceptor(jwtTokenUserInterceptor)
            .addPathPatterns("/user/**")  // 拦截所有/user开头的请求
            .excludePathPatterns("/user/login"); // 开发登录接口
    }
    //允许跨域
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")      // 添加路径规则
                .allowCredentials(true)               // 是否允许在跨域的情况下传递Cookie
                .allowedOriginPatterns("*")           // 允许请求来源的域规则
                .allowedMethods("*")
                .allowedHeaders("*") ;                // 允许所有的请求头
    }
}
