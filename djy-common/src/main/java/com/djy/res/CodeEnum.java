package com.djy.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum  CodeEnum {

    /**
     * 成功
     */
    SUCCESS(200),

    /**
     * 系统异常
     */
    EXCEPTION(500),

    /**
     * 登陆超时
     */
    LOGIN_OUT_TIME(300),

    /**
     * 业务错误
     */
    ERROR(100),

    /**
     * 用户被锁住
     */
    USER_SUO(400)

    ;
    private int code;
}
