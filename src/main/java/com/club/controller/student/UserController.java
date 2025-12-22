package com.club.controller.student;

import com.club.entity.User;
import com.club.entity.request.UserSaveDto;
import com.club.entity.vo.Result;
import com.club.entity.vo.ResultCodeEnum;
import com.club.entity.vo.UserLoginVo;
import com.club.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.club.util.AuthContextUtil;

/**
 * 学生相关接口
 * @author zyh
 * @date 2025/11/11 22:08
 */
@Tag(name = "用户接口")
@RestController("studentUserController")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 微信登录
     * @param code 每个微信用户都会有一个微信专属的code，前端需传回来
     * @return
     */
    @Operation(summary = "微信登录")
    @PostMapping("/login")
    public Result<UserLoginVo> login(@RequestParam String code) {
        // 调用服务层处理登录逻辑
        UserLoginVo userLoginVo = userService.wxLogin(code);
        return Result.build(userLoginVo, ResultCodeEnum.SUCCESS);
    }

    /**
     * 新增用户信息
     */
    @Operation(summary = "新增用户信息")
    @PostMapping("/save")
    public Result<String> save(@RequestBody UserSaveDto userSaveDto) {
        userService.save(userSaveDto);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    /**
     * 根据微信的Openid查询用户信息
     * @param openid
     * @return
     */
    @Operation(summary = "根据Openid查询用户信息")
    @GetMapping("/getByOpenid")
    public Result<User> getByOpenid(@RequestParam String openid) {
        User user = userService.getByOpenid(openid);
        return Result.build(user, ResultCodeEnum.SUCCESS);
    }


    @Operation(summary = "更新用户信息")
    @PutMapping("/updateUserInfo")
    public Result<String> updateUserInfo(@RequestBody User user) {
        userService.updateUserInfo(user);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    /**
     * 用户登出
     */
    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    public Result<String> logout() {
        // 获取当前用户
        User user = AuthContextUtil.getUser();
        // 删除当前用户
        userService.removeUserInfo(user.getOpenid());
        // 登出成功
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
}
