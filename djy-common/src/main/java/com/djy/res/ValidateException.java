package com.djy.res;

import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.List;

/**
 * 参数验证异常处理
 */
public class ValidateException extends RuntimeException{

    private int code = CodeEnum.ERROR.getCode();//参数错误返回的信息
    private String[] messageArray;
    private Object data;
    public ValidateException(String message) {
        super(message);
    }

    public ValidateException(String message, int code) {
        super(message);
        this.code = code;
    }

    public ValidateException(String message, int code, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    public ValidateException(List<FieldError> fieldErrors) {
        messageArray = new String[fieldErrors.size()];
        int i = 0;
        for (FieldError fieldError : fieldErrors) {
            messageArray[i++] = fieldError.getDefaultMessage();
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String[] getMessageArray() {
        return messageArray;
    }

    public void setMessageArray(String[] messageArray) {
        this.messageArray = messageArray;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        if (messageArray != null) {
            return "ValidateException [code=" + code + ", messageArray=" + Arrays.toString(messageArray) + ", data="
                    + data + "]";
        } else {
            return "ValidateException [code=" + code + ", message=" + getMessage() + ", data=" + data + "]";
        }
    }
}
