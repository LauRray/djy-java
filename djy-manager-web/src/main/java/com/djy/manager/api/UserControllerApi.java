package com.djy.manager.api;

import com.djy.manager.reqVo.UserLoginVo;
import com.djy.manager.reqVo.sysUser.SysUserPageVo;
import com.djy.manager.reqVo.sysUser.SysUserSaveVo;
import com.djy.req.IdListReq;
import com.djy.res.PageResult;
import com.djy.res.Result;
import com.djy.sql.pojo.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

@Api(value = "用户管理",description = "包含用户登录")
public interface UserControllerApi {

    @ApiOperation(value = "用户登录")
    public Result userLogin(UserLoginVo userLoginVo , BindingResult result, HttpServletRequest request);

    @ApiOperation(value = "用户分页查询")
    public PageResult<SysUser> pageList(SysUserPageVo sysUserPageVo,BindingResult result);

    @ApiOperation(value = "删除用户")
    public Result delete(IdListReq idListReq,BindingResult result);

    @ApiOperation(value = "新增用户")
    public Result sava(SysUserSaveVo sysUserSaveVo,BindingResult result);
}
