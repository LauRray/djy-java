package com.djy.sql.mapper;

import com.djy.sql.pojo.SysMenu;
import com.djy.sql.pojo.SysRoleMenu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SysRoleMenuMapper extends Mapper<SysRoleMenu>{

    public void save(@Param("list") List<SysMenu> list, @Param("roleId")Long roleId);

    @Delete("delete from sys_role_menu where role_id=#{roleId}")
    public void deleteByRoleId(@Param("roleId") Long roleId);
}
