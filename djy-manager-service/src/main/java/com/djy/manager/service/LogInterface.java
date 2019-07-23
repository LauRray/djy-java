package com.djy.manager.service;

import com.djy.res.PageResult;
import com.djy.res.Result;
import com.djy.sql.pojo.SysLog;

public interface LogInterface {

    public Result save(SysLog sysLog);

    public PageResult<SysLog> pageList(int pageNum,int pageSize,SysLog sysLog);
}
