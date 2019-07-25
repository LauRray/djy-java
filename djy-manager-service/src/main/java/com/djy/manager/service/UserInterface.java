package com.djy.manager.service;

import com.djy.req.IdListReq;
import com.djy.res.PageResult;
import com.djy.res.Result;
import com.djy.sql.pojo.SysUser;

import java.util.List;

public interface UserInterface {

    public Result userLogin(SysUser sysUser);

    public Result findById(Long userId);

    public Result save(SysUser sysUser);

    public PageResult<SysUser> pageList(int pageNum,int pageSize,String username);

    public Result delete(IdListReq idListReq);



    public List<SysUser> findSysUser(SysUser sysUser);



}
