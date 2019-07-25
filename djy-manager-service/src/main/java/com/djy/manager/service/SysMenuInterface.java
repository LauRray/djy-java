package com.djy.manager.service;

import com.djy.res.Result;
import com.djy.sql.pojo.SysMenu;

import java.util.List;

public interface SysMenuInterface {

    public Result getMenuTree();

    public List<SysMenu> findMenuByUserId(Long userId);


}
