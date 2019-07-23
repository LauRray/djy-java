package com.djy.manager.service;

import com.djy.req.IdListReq;
import com.djy.res.PageResult;
import com.djy.res.Result;
import com.djy.sql.pojo.SysRole;

public interface SysRoleInterface {

    public PageResult<SysRole> pageList(Integer pageNum,Integer pageSize,SysRole sysRole);

    public Result delete(IdListReq idListReq);

    public Result save(SysRole sysRole);
}
