package com.club.controller.admin;


import com.club.aspect.LogOperation;
import com.club.entity.request.AdminLoginDto;
import com.club.entity.vo.AdminLoginVo;
import com.club.entity.vo.Result;
import com.club.entity.vo.ResultCodeEnum;
import com.club.entity.vo.ValidateCodeVo;
import com.club.service.AdminService;
import com.club.service.ValidateCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 平台管理员相关接口
 */
@RestController
@RequestMapping("/admin")
@Tag(name = "管理员相关接口")
@Slf4j
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ValidateCodeService validateCodeService;

    @LogOperation("管理员登录")
    @Operation(summary = "管理员登录接口")
    @PostMapping("/login")
    public Result<AdminLoginVo> login(@RequestBody AdminLoginDto adminLoginDto) {
        log.info("管理员登录接口") ;
        AdminLoginVo adminLoginVo = adminService.login(adminLoginDto) ;
        return Result.build(adminLoginVo, ResultCodeEnum.SUCCESS) ;
    }


    @LogOperation("生成验证码")
    @Operation(summary =  "生成验证码接口")
    @GetMapping(value = "/generateValidateCode")
    public Result<ValidateCodeVo> generateValidateCode() {
        log.info("生成验证码接口") ;
        ValidateCodeVo validateCodeVo = validateCodeService.generateValidateCode();
        return Result.build(validateCodeVo , ResultCodeEnum.SUCCESS) ;
    }

}