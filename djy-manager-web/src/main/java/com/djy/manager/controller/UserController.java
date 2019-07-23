package com.djy.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.djy.common.CommonUtil;
import com.djy.common.IdWorker;
import com.djy.manager.api.UserControllerApi;
import com.djy.manager.reqVo.UserLoginVo;
import com.djy.manager.reqVo.sysUser.SysUserPageVo;
import com.djy.manager.service.UserInterface;
import com.djy.req.IdListReq;
import com.djy.res.PageResult;
import com.djy.res.ResponseEnum;
import com.djy.res.Result;
import com.djy.res.ValidateException;
import com.djy.sql.pojo.SysLog;
import com.djy.sql.pojo.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping(value = "${ctx}/user")
@Slf4j
public class UserController implements UserControllerApi {


    @Reference(check = false)
    UserInterface userInterface;

    @Resource
    IdWorker idWorker;

    @Resource
    RabbitTemplate rabbitTemplate;

    @Value("${rabbit.send.log.exchange}")
    private String logExc;

    @Value("${rabbit.send.log.routerkey}")
    private String router;

    @Override
    @PostMapping(value = "/login")
    public Result userLogin(@RequestBody @Valid UserLoginVo userLoginVo, BindingResult result, HttpServletRequest request) {

        if (result.hasErrors()){
            throw new ValidateException(result.getFieldErrors());
        }
        SysUser sysUser=new SysUser();
        BeanUtils.copyProperties(userLoginVo,sysUser);
        Result resultData = userInterface.userLogin(sysUser);
        SysUser sysUserData=(SysUser)resultData.getData();
        try{
            /**
             * 构建日志信息
             */
            if (sysUserData.getId()!=null){
                SysLog sysLog=new SysLog(
                        idWorker.nextId(),sysUserData.getId(),1,true, CommonUtil.getIpAddress(request),new Date(),
                        request.getMethod(),request.getRequestURI(),"用户登录","");
                sysLog.setStatus(resultData.getCode()==200?true:false);
                sysLog.setReason(resultData.getMsg());
                rabbitTemplate.convertAndSend(logExc,router,sysLog);
            }

        }catch (Exception e){
            e.printStackTrace();
            log.error("rabbit保存系统日志失败........................");
        }
        if (resultData.getCode()==200){
            return Result.buildResultOfSuccess(ResponseEnum.USER_LOGIN_SUCCESS,sysUserData.getAuthorization());
        }else {
            return resultData;
        }
    }

    /**
     * 用户分页查询
     */
    @PostMapping("/pageList")
    @Override
    public PageResult<SysUser> pageList(@RequestBody SysUserPageVo sysUserPageVo, BindingResult result) {
        if (result.hasErrors()){
            throw new ValidateException(result.getFieldErrors());
        }
        return userInterface.pageList(sysUserPageVo.getPageNum(),sysUserPageVo.getPageSize(),sysUserPageVo.getUsername());
    }

    @Override
    @PostMapping(value = "/delete")
    public Result delete(@RequestBody @Valid IdListReq idListReq, BindingResult result) {
        if (result.hasErrors()){
            throw new ValidateException(result.getFieldErrors());
        }
        try{
            for (Long id:idListReq.getList()){

                userInterface.deleteRedis(id);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //清楚缓存中的信息
        return userInterface.delete(idListReq);
    }


}
