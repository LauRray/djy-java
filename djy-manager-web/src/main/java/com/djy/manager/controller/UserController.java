package com.djy.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.djy.common.CommonUtil;
import com.djy.common.IdWorker;
import com.djy.manager.api.UserControllerApi;
import com.djy.manager.reqVo.UserLoginVo;
import com.djy.manager.reqVo.sysUser.SysUserPageVo;
import com.djy.manager.reqVo.sysUser.SysUserSaveVo;
import com.djy.manager.reqVo.sysUser.SysUserUpdateVo;
import com.djy.manager.service.SysMenuInterface;
import com.djy.manager.service.UserInterface;
import com.djy.req.IdListReq;
import com.djy.req.IdReq;
import com.djy.res.PageResult;
import com.djy.res.ResponseEnum;
import com.djy.res.Result;
import com.djy.res.ValidateException;
import com.djy.sql.pojo.SysLog;
import com.djy.sql.pojo.SysMenu;
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
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "${ctx}/user")
@Slf4j
public class UserController implements UserControllerApi {


    @Reference(check = false)
    UserInterface userInterface;

    @Resource
    IdWorker idWorker;

    @Reference
    SysMenuInterface sysMenuInterface;

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
            Map<String,Object> map=new Hashtable<>();
            /**
             * 获取用户权限
             */
            List<SysMenu> listMenu = sysMenuInterface.findMenuByUserId(sysUserData.getId());
            map.put("Authorization",sysUserData.getAuthorization());
            map.put("djy_user_menu",listMenu);
            return Result.buildResultOfSuccess(ResponseEnum.USER_LOGIN_SUCCESS,map);
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


        return userInterface.delete(idListReq);
    }

    /**
     * 新增用户
     */
    @PostMapping(value = "/sava")
    @Override
    public Result sava(@RequestBody @Valid SysUserSaveVo sysUserSaveVo, BindingResult result) {
        if (result.hasErrors()){
            throw new ValidateException(result.getFieldErrors());
        }
        SysUser sysUser=new SysUser();
        BeanUtils.copyProperties(sysUserSaveVo,sysUser);
        return userInterface.save(sysUser);
    }


    /**
     * 根据id获取用户
     */
    @GetMapping(value = "/findById/{id}")
    public Result findById(@PathVariable(value = "id",required = true) Long id){
        return userInterface.findById(id);
    }

    /**
     * 修改用户信息
     */
    @PostMapping(value = "/update")
    public Result update(@RequestBody SysUserUpdateVo sysUserUpdateVo,BindingResult result){
        if (result.hasErrors()){
            throw new ValidateException(result.getFieldErrors());
        }
        SysUser sysUser=new SysUser();
        BeanUtils.copyProperties(sysUserUpdateVo,sysUser);
        return userInterface.save(sysUser);
    }

}
