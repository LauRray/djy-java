package com.djy.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.djy.manager.api.SysLogControllerApi;
import com.djy.manager.reqVo.sysLog.SysLogPageVo;
import com.djy.manager.service.LogInterface;
import com.djy.res.PageResult;
import com.djy.res.ValidateException;
import com.djy.sql.pojo.SysLog;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "${ctx}/log")
public class SysLogController implements SysLogControllerApi {

    @Reference
    LogInterface logInterface;

    @PostMapping(value = "/pageList")
    @Override
    public PageResult<SysLog> pageList(@RequestBody @Valid SysLogPageVo sysLogPageVo, BindingResult result) {
        if (result.hasErrors()){
            throw new ValidateException(result.getFieldErrors());
        }
        SysLog sysLog=new SysLog();
        BeanUtils.copyProperties(sysLogPageVo,sysLog);
        return logInterface.pageList(sysLogPageVo.getPageNum(),sysLogPageVo.getPageSize(),sysLog);
    }

    @RequestMapping(value = "/operate")
    @Override
    public PageResult<SysLog> operatePageList(@RequestBody @Valid SysLogPageVo sysLogPageVo, BindingResult result) {
        if (result.hasErrors()){
            throw new ValidateException(result.getFieldErrors());
        }
        SysLog sysLog=new SysLog();
        BeanUtils.copyProperties(sysLogPageVo,sysLog);
        return logInterface.pageList(sysLogPageVo.getPageNum(),sysLogPageVo.getPageSize(),sysLog);
    }


}
