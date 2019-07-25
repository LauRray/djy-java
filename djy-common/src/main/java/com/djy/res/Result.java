package com.djy.res;

import javafx.beans.binding.ObjectExpression;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class Result implements Serializable{

    private static final long serialVersionUID = -6239782806843374849L;
    private int code;

    private String msg;

    private Object data;

    private boolean show;

    public Result() {
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

    public boolean isShow() {
        return show;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public Result(int code, String msg, Object data, boolean flg) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.show = flg;
    }

    public static Result buildResultOfSuccess(ResponseEnum responseEnum,Object data){
        return new Result(responseEnum.getCode(),responseEnum.getMsg(),data,responseEnum.isShow());
    }

    public static Result buildResultOfError(ResponseEnum responseEnum,String data){
        return new Result(responseEnum.getCode(),data,responseEnum.getMsg(),responseEnum.isShow());
    }

    public static Result buildResultOfErrorNotData(ResponseEnum responseEnum){
        return new Result(responseEnum.getCode(),responseEnum.getMsg(),"",responseEnum.isShow());
    }

    public static Result buildResultOfErrorData(ResponseEnum responseEnum,Object data){
        return new Result(responseEnum.getCode(),responseEnum.getMsg(),data,responseEnum.isShow());
    }
}
