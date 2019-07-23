package com.djy.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.djy.manager.api.SysRoleControllerApi;
import com.djy.manager.reqVo.sysRole.SysRolePageVo;
import com.djy.manager.reqVo.sysRole.SysRoleSaveVo;
import com.djy.manager.service.SysRoleInterface;
import com.djy.req.IdListReq;
import com.djy.res.PageResult;
import com.djy.res.Result;
import com.djy.res.ValidateException;
import com.djy.sql.pojo.SysRole;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 用户角色
 * @author win 10
 */
@RestController
@RequestMapping(value = "${ctx}/role")
public class SysRoleController implements SysRoleControllerApi {

    @Reference
    SysRoleInterface sysRoleInterface;

    /**
     * 分页查询
     * @param sysRolePage
     * @param result
     * @return
     */
    @PostMapping(value = "/page")
    @Override
    public PageResult<SysRole> rolePage(@RequestBody @Valid SysRolePageVo sysRolePage, BindingResult result) {
        if (result.hasErrors()){
            throw new ValidateException(result.getFieldErrors());
        }
        SysRole sysRole=new SysRole();
        BeanUtils.copyProperties(sysRolePage,sysRole);
        return sysRoleInterface.pageList(sysRolePage.getPageNum(),sysRolePage.getPageSize(),sysRole);
    }

    @Override
    @PostMapping(value = "/delete")
    public Result delete(@RequestBody @Valid IdListReq idListReq, BindingResult result) {
        if (result.hasErrors()){
            throw  new ValidateException(result.getFieldErrors());
        }
        return sysRoleInterface.delete(idListReq);
    }

    /**
     * 新增角色
     * @param sysRoleSaveVo
     * @param result
     * @return
     */
    @PostMapping(value = "/save")
    @Override
    public Result sava(@RequestBody @Valid SysRoleSaveVo sysRoleSaveVo, BindingResult result) {
        if (result.hasErrors()){
            throw  new ValidateException(result.getFieldErrors());
        }
        SysRole sysRole=new SysRole();
        BeanUtils.copyProperties(sysRoleSaveVo,sysRole);
        return sysRoleInterface.save(sysRole);
    }


}
