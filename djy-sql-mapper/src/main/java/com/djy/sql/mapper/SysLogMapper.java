package com.djy.sql.mapper;

import com.djy.sql.pojo.SysLog;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SysLogMapper extends Mapper<SysLog> {

    public List<SysLog> pageList(@Param("userId") Long userId,@Param("status") Boolean status, @Param("type")Integer type);
}
