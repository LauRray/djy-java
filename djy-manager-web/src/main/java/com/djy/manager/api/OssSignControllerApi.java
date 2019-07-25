package com.djy.manager.api;

import com.djy.res.Result;
import io.swagger.annotations.Api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "OSS管理")
public interface OssSignControllerApi {

    public Result getOssSign(HttpServletRequest request, HttpServletResponse response);
}
