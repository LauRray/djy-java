package com.djy.sql.mapper;

import com.djy.sql.pojo.SysRole;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface SysRoleMapper extends Mapper<SysRole> , IdListMapper<SysRole,Long> {
}
