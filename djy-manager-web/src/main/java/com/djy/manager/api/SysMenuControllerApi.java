package com.djy.manager.api;

import com.djy.res.Result;
import io.swagger.annotations.Api;

@Api(value = "菜单接口",description = "包含菜单增删改查、权限树")
public interface SysMenuControllerApi {

    public Result getMenuTree();
}
