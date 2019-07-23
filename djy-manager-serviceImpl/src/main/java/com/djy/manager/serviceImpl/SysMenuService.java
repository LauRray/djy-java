package com.djy.manager.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.djy.manager.service.SysMenuInterface;
import com.djy.res.ResponseEnum;
import com.djy.res.Result;
import com.djy.sql.mapper.SysMenuMapper;
import com.djy.sql.pojo.SysMenu;
import org.springframework.cache.annotation.Cacheable;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysMenuService implements SysMenuInterface {

    @Resource
    SysMenuMapper sysMenuMapper;

    @Override
    @Cacheable(cacheNames = "djy",key = "'sys_menu::tree'")
    public Result getMenuTree() {

        List<SysMenu> menuTreeList = sysMenuMapper.getMenuTree();
        return Result.buildResultOfSuccess(ResponseEnum.SYS_MENU_TREE_SUCCESS,menuTreeList);
    }
}
