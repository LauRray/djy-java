package com.djy.manager.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.djy.manager.service.SysRoleMenuInterface;
import com.djy.res.ResponseEnum;
import com.djy.res.Result;
import com.djy.sql.mapper.SysRoleMenuMapper;
import com.djy.sql.pojo.SysRoleMenu;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

@Service
public class SysRoleMenuService implements SysRoleMenuInterface {

    @Resource
    SysRoleMenuMapper sysRoleMenuMapper;
    @Override
    public Result findByRoleId(Long roleId) {
        Example example=new Example(SysRoleMenu.class);
        example.createCriteria().andEqualTo("roleId",roleId);
       // .andNotEqualTo("type",1);
        return Result.buildResultOfSuccess(ResponseEnum.SUCCESS,sysRoleMenuMapper.selectByExample(example));
    }
}
