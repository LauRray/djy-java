package com.djy.manager.api;

import com.djy.manager.reqVo.sysLog.SysLogPageVo;
import com.djy.res.PageResult;
import com.djy.sql.pojo.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;

@Api(value = "日志管理接口")
public interface SysLogControllerApi {

    @ApiOperation("登陆日志分页查询")
    PageResult<SysLog> pageList(SysLogPageVo sysLogPageVo, BindingResult result);

    @ApiModelProperty("操作日志分页查询")
    PageResult<SysLog> operatePageList(SysLogPageVo sysLogPageVo, BindingResult result);
}
