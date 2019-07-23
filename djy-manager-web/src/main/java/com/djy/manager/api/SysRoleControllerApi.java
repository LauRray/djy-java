package com.djy.manager.api;

import com.djy.manager.reqVo.sysRole.SysRolePageVo;
import com.djy.manager.reqVo.sysRole.SysRoleSaveVo;
import com.djy.req.IdListReq;
import com.djy.res.PageResult;
import com.djy.res.Result;
import com.djy.sql.pojo.SysRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;

@Api(value = "角色管理",description = "提供角色的增、删、改、查" )
public interface SysRoleControllerApi {

    @ApiOperation("角色分页查询")
    public PageResult<SysRole> rolePage(SysRolePageVo sysRolePage, BindingResult result);

    @ApiOperation("角色删除")
    public Result delete(IdListReq idListReq,BindingResult result);

    @ApiOperation("角色新增")
    public Result sava(SysRoleSaveVo sysRoleSaveVo,BindingResult result);
}
