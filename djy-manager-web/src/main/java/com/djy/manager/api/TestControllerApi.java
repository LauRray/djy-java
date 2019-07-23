package com.djy.manager.api;

import com.djy.sql.pojo.Test;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@Api(value = "测试类",description = "测试用来的")
public interface TestControllerApi {

    @ApiOperation("测试的了")
    public List<Test> test();
}
