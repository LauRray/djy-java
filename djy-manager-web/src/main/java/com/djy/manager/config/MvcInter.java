package com.djy.manager.config;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.djy.common.CommonUtil;
import com.djy.common.IdWorker;
import com.djy.manager.service.LogInterface;
import com.djy.manager.service.UserInterface;
import com.djy.res.ResponseEnum;
import com.djy.res.Result;
import com.djy.sql.pojo.SysLog;
import com.djy.sql.pojo.SysUser;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.lang3.StringUtils;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;

public class MvcInter implements HandlerInterceptor {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Reference(check = false)
    UserInterface userInterface;

    @Reference(check = false)
    LogInterface logInterface;

    @Resource
    RabbitTemplate rabbitTemplate;

    @Resource
    IdWorker idWorker;

    @Value("${rabbit.send.log.exchange}")
    private String logExc;

    @Value("${rabbit.send.log.routerkey}")
    private String router;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        String token = request.getHeader("Authorization");
        boolean flg=false;
        Result result=null;
        //请求不携带token说明非法登录
        if (token==null){
            result=Result.buildResultOfErrorNotData(ResponseEnum.FEI_FA_LOGIN);
            flg=true;
        }
        String key="djy::authorization::" + token;
        //token超时，默认30分钟
        String value=stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isBlank(value)&&!flg){
            result=Result.buildResultOfErrorNotData(ResponseEnum.LOGIN_OUT_TIME);
            flg=true;
        }
        //登录失败！
        if (flg){
            String str = JSONObject.toJSONString(result);
            this.write(str,response);
            return false;
        }

        //从数据库获取用户信息，实时请求获取用户的状态
        SysUser sysUser = JSONObject.parseObject(value, SysUser.class);

        Result reultUser = userInterface.findById(sysUser.getId());
        sysUser  = (SysUser)reultUser.getData();
        if (sysUser==null){
            result=Result.buildResultOfErrorNotData(ResponseEnum.USER_NOT_EXIT);
            String str = JSONObject.toJSONString(result);
            this.write(str,response);
            return false;
        }
        //账号被停用
        if (sysUser.getStatus()==2){
            result=Result.buildResultOfErrorNotData(ResponseEnum.USER_STOP_USEING);
            String str = JSONObject.toJSONString(result);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(str.getBytes("UTF-8"));
            outputStream.close();
            return false;
        }
        //request.setAttribute("sysUserAttr",sysUser);
        boolean logFlg = Boolean.valueOf(request.getHeader("logFlg"));
        if (logFlg){
            /**
             * 构建日志信息
             */
            try{
                SysLog sysLog=new SysLog(
                        idWorker.nextId(),sysUser.getId(),2,true, CommonUtil.getIpAddress(request),new Date(),
                        request.getMethod(),request.getRequestURI(),"","");
                rabbitTemplate.convertAndSend(logExc,router,sysLog);
            }catch (Exception e){
                e.printStackTrace();
            }

        }


        return true;


    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    public void write(String str,HttpServletResponse response) throws IOException {
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(str.getBytes("UTF-8"));
        outputStream.close();
    }
}


