package com.djy.res;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ResponseEnum {

    SUCCESS(CodeEnum.SUCCESS.getCode(),false,"成功"),
    EXCEPTION(CodeEnum.EXCEPTION.getCode(),true,"系统异常"),
    ERROR(CodeEnum.ERROR.getCode(),true,"业务失败"),
    LOGIN_OUT_TIME(CodeEnum.LOGIN_OUT_TIME.getCode(),true,"登陆超时！"),
    FEI_FA_LOGIN(CodeEnum.ERROR.getCode(),true,"非法登陆！"),
    USER_NOT_EXIT(CodeEnum.ERROR.getCode(),true,"登陆账户不存在！"),
    USER_PASSWORD_ERROR(CodeEnum.ERROR.getCode(),true,"用户密码错误！"),
    USER_LOGIN_SUCCESS(CodeEnum.SUCCESS.getCode(),true,"登陆成功！"),
    USER_STOP_USEING(CodeEnum.USER_SUO.getCode(),true,"该账号被停用！"),
    USER_SAVE_SUCCESS(CodeEnum.SUCCESS.getCode(),true,"用户新增成功"),
    USER_UPDATE_SUCCESS(CodeEnum.SUCCESS.getCode(),true,"用户修改成功"),
    USER_PAGELIST_SUCCESS(CodeEnum.SUCCESS.getCode(),false,"用户分页成功"),
    USER_DELETE_SUCCESS(CodeEnum.SUCCESS.getCode(),true,"用户删除成功"),
    SYS_ROLE_PAGELIST_SUCCESS(CodeEnum.SUCCESS.getCode(),false,"角色分页查看成功"),
    SYS_ROLE_DELETE_SUCCESS(CodeEnum.SUCCESS.getCode(),true,"删除成功"),
    SYS_LOG_PAGELIST_SUCCESS(CodeEnum.SUCCESS.getCode(),false,"获取日志成功"),
    SYS_ROLE_SAVE_SUCCESS(CodeEnum.SUCCESS.getCode(),true,"角色新增成功"),
    SYS_MENU_TREE_SUCCESS(CodeEnum.SUCCESS.getCode(),false,"成功获取权限树")
    ;
    private int code;
    private boolean show;
    private String msg;
}
