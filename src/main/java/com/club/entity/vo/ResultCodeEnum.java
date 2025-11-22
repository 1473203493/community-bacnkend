package com.club.entity.vo;

import lombok.Getter;

@Getter // 提供获取属性值的getter方法
public enum ResultCodeEnum {

    SUCCESS(200 , "操作成功") ,
    LOGIN_ERROR(201 , "用户名或者密码错误"),
    VALIDATECODE_ERROR(202 , "验证码错误") ,
    LOGIN_AUTH(208 , "用户未登录"),
    USER_NAME_IS_EXISTS(209 , "用户名已经存在"),
    SYSTEM_ERROR(9999 , "您的网络有问题请稍后重试"),
    NODE_ERROR( 217, "该节点下有子节点，不可以删除"),
    DATA_ERROR(204, "数据异常"),
    USER_NOT_LOGIN( 210, "用户还未登录"),

    ACCOUNT_STOP( 216, "账号已停用"),

    STOCK_LESS( 219, "库存不足"),
    WX_ERROR( 210, "微信服务异常"),

    USER_NOT_EXIST(211,"用户不存在" ),
    // 新增社团相关异常码
    PARAM_ERROR(212, "参数错误"),
    REJECT_REASON_REQUIRED(213,"驳回需要填写理由"),
    CLUB_NOT_EXIST(214, "社团不存在"),
    CLUB_STATUS_NOT_PENDING(215, "仅待审批状态的社团可操作"),
    OPERATION_FAIL(216, "操作失败"),
    CLUB_STATUS_ERROR(217,"社团状态异常");

    private Integer code ;      // 业务状态码
    private String message ;    // 响应消息

    private ResultCodeEnum(Integer code , String message) {
        this.code = code ;
        this.message = message ;
    }

}
