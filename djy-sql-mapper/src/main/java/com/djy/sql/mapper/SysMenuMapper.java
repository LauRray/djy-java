package com.djy.sql.mapper;

import com.djy.sql.pojo.SysMenu;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SysMenuMapper extends Mapper<SysMenu> {

    public List<SysMenu> getMenuTree();
}
