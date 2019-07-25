package com.djy.sql.mapper;

import com.djy.sql.pojo.SysRole;
import com.djy.sql.pojo.SysUserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SysUserRoleMapper extends Mapper<SysUserRole> {

    @Delete("delete from sys_user_role where user_id=#{userId}")
    public void deleteByUserId(@Param("userId")Long userId);

    public void save(@Param("list") List<Long> list,@Param("userId")Long userId);

    public void deleteByUserIdList(@Param("list") List<Long> list);
}
