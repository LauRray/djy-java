package com.djy.sql.mapper;

import com.djy.sql.pojo.SysMenu;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SysMenuMapper extends Mapper<SysMenu>, IdListMapper<SysMenu,Long> {

    public List<SysMenu> getMenuTree();

 }
