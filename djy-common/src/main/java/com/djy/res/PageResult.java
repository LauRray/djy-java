package com.djy.res;

import java.io.Serializable;
import java.util.List;

public class PageResult<T> extends Result implements Serializable {

    private static final long serialVersionUID = 4490335690250129439L;
    private Long total;
    private List<T> list;


    public PageResult() {
    }


   public  PageResult build(ResponseEnum responseEnum,List<T> list,Long total){
        return new PageResult(responseEnum.getCode(),responseEnum.getMsg(),list,responseEnum.isShow(),total);
   }


    public PageResult(int code, String msg, Object data, boolean flg, Long total) {
        super(code, msg, data, flg);
        this.total = total;

    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Long getTotal() {
        return total;
    }


}
