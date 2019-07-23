package com.djy.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.djy.manager.api.SysMenuControllerApi;
import com.djy.manager.service.SysMenuInterface;
import com.djy.res.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "${ctx}/sysMenu")
public class SysMenuController implements SysMenuControllerApi {

    @Reference
    SysMenuInterface sysMenuInterface;

    @PostMapping(value = "/getMenuTree")
    @Override
    public Result getMenuTree() {
        return sysMenuInterface.getMenuTree();
    }
}
