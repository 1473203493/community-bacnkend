package com.club.controller.admin;


import com.club.aspect.LogOperation;
import com.club.entity.Club;
import com.club.entity.User;
import com.club.entity.request.AdminLoginDto;
import com.club.entity.request.ClubApprovalDto;
import com.club.entity.request.ClubQueryDto;
import com.club.entity.request.UserQueryDto;
import com.club.entity.vo.AdminLoginVo;
import com.club.entity.vo.Result;
import com.club.entity.vo.ResultCodeEnum;
import com.club.entity.vo.ValidateCodeVo;
import com.club.service.AdminService;
import com.club.service.ClubService;
import com.club.service.UserService;
import com.club.service.ValidateCodeService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.java.Log;
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
    private UserService userService;

    @Autowired
    private ValidateCodeService validateCodeService;

    @Autowired
    private ClubService clubService;

    @LogOperation("管理员登录")
    @Operation(summary = "管理员登录接口")
    @PostMapping("/login")
    public Result<AdminLoginVo> login(@RequestBody AdminLoginDto adminLoginDto) {
        log.info("管理员登录接口") ;
        AdminLoginVo adminLoginVo = adminService.login(adminLoginDto);
        return Result.build(adminLoginVo, ResultCodeEnum.SUCCESS);
    }


    @LogOperation("生成验证码")
    @Operation(summary =  "生成验证码接口")
    @GetMapping(value = "/generateValidateCode")
    public Result<ValidateCodeVo> generateValidateCode() {
        log.info("生成验证码接口") ;
        ValidateCodeVo validateCodeVo = validateCodeService.generateValidateCode();
        return Result.build(validateCodeVo , ResultCodeEnum.SUCCESS) ;
    }

    @LogOperation("查询用户列表")
    @Operation(summary = "分页查询用户信息")
    @GetMapping("/getUsersList")
    public Result<PageInfo<User>> getUserList(
        @RequestParam(required = false) String studentNo,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String role,
        @RequestParam(required = false) String status,
        @RequestParam(defaultValue = "1") Integer pageNum,
        @RequestParam(defaultValue = "10") Integer pageSize) {

    UserQueryDto userQueryDto = new UserQueryDto();
    userQueryDto.setStudentNo(studentNo);
    userQueryDto.setName(name);
    userQueryDto.setRole(role);
    userQueryDto.setStatus(status);
    userQueryDto.setPageNum(pageNum);
    userQueryDto.setPageSize(pageSize);

    PageInfo<User> pageInfo = userService.getUserList(userQueryDto);
    return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
}

    @LogOperation("更新用户状态")
    @Operation(summary = "启用/禁用用户账号")
    @PutMapping("/updateUserStatus")
    public Result<String> updateUserStatus(
            @RequestParam Integer userId,
            @RequestParam String status) {

        userService.updateUserStatus(userId, status);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @LogOperation("查看社团列表")
    @Operation(summary = "获取社团列表", description = "支持筛选条件：状态（待审批/正常/冻结/拒绝）、分类、负责人邮箱（模糊搜索）")
    @GetMapping("/club/list")
    public Result<PageInfo<Club>> getClubList(ClubQueryDto queryDto) {
        // 调用服务层分页查询
        PageInfo<Club> pageInfo = clubService.getClubList(queryDto);
        log.info("管理员查询社团列表成功，筛选条件：{}，当前页码：{}，每页条数：{}",
                queryDto, queryDto.getPageNum(), queryDto.getPageSize());
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    @LogOperation("查看社团审批详情")
    @Operation(summary = "获取社团详情（审批用）", description = "返回社团章程、负责人信息、分类等完整数据")
    @GetMapping("/club/{clubId}")
    public Result<Club> getClubDetail(@PathVariable Integer clubId) {
        Club club = clubService.getClubDetail(clubId);
        return Result.build(club, ResultCodeEnum.SUCCESS);
    }

    @LogOperation("审批社团")
    @Operation(summary = "社团审批操作", description = "status=2(同意)，status=4(拒绝)；拒绝时必须传rejectReason")
    @PostMapping("/club/approve")
    public Result<String> approveClub(@RequestBody ClubApprovalDto approvalDto) {
        clubService.approveClub(approvalDto);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

}