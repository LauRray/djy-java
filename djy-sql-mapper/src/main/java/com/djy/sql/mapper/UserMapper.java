package com.djy.sql.mapper;

import com.djy.sql.pojo.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<SysUser> , IdListMapper<SysUser,Long> {

    /**
     * 根据用户名查询
     */

    @Select("select * from sys_user where username=#{username}")
    public SysUser findByUsername(@Param("username") String username);

    /**
     * 查询用户分页
     */
    public List<SysUser> findUserPageList(@Param("username") String username);
}
