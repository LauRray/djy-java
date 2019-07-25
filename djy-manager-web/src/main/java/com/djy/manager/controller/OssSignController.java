package com.djy.manager.controller;

import com.djy.core.OssSign;
import com.djy.manager.api.OssSignControllerApi;
import com.djy.res.ResponseEnum;
import com.djy.res.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "${ctx}/oss")
public class OssSignController implements OssSignControllerApi {

    @Resource
    OssSign ossSign;

    @PostMapping(value = "/getSign")
    @Override
    public Result getOssSign(HttpServletRequest request, HttpServletResponse response) {
        return Result.buildResultOfSuccess(ResponseEnum.SUCCESS,ossSign.ossSign(request,response));
    }
}
