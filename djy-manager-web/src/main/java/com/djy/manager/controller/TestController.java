package com.djy.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.djy.manager.api.TestControllerApi;
import com.djy.manager.service.TestInterface;
import com.djy.sql.pojo.Test;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javafx.scene.chart.ValueAxis;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "${ctx}/test")

public class TestController implements TestControllerApi {

    @Reference(check = false)
    TestInterface testInterface;


    @GetMapping(value = "/test")
    @Override
    public List<Test> test(){
        return testInterface.findAll();
    }
}
