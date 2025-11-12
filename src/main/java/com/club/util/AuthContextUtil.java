package com.club.util;


import com.club.entity.Admin;
import com.club.entity.User;

/**
 * ThreadLocal工具类，用于存放用户信息，
 * @author ljl
 * @create 2023-10-25-12:03
 */
public class AuthContextUtil {

    // 创建一个ThreadLocal对象
    private static final ThreadLocal<Admin> threadLocal = new ThreadLocal<>() ;

    // 定义存储数据的静态方法
    public static void set(Admin admin) {
        threadLocal.set(admin);
    }

    // 定义获取数据的方法
    public static Admin get() {
        return threadLocal.get() ;
    }

    // 删除数据的方法
    public static void remove() {
        threadLocal.remove();
    }

    private static final ThreadLocal<User> userInfoThreadLocal = new ThreadLocal<>() ;


    // 定义存储数据的静态方法
    public static void setUser(User user) {
        userInfoThreadLocal.set(user);
    }

    // 定义获取数据的方法
    public static User getUser() {
        return userInfoThreadLocal.get() ;
    }

    // 删除数据的方法
    public static void removeUser() {
        userInfoThreadLocal.remove();
    }
    
    /**
     * 获取当前用户ID
     * @return 用户ID
     */
    public static Long getCurrentUserId() {
        Admin admin = get();
        if (admin != null) {
            return Long.valueOf(admin.getUserId());
        }
        
        User user = getUser();
        if (user != null) {
            return Long.valueOf(user.getUserId());
        }
        
        return null;
    }
    
    /**
     * 获取当前用户角色
     * @return 用户角色
     */
    public static String getCurrentUserRole() {
        Admin admin = get();
        if (admin != null) {
            return admin.getRole();
        }
        
        User user = getUser();
        if (user != null) {
            return user.getRole();
        }
        
        return null;
    }
}