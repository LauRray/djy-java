package com.djy.manager.reqVo.sysRole;

import com.djy.manager.reqVo.PageVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import java.io.Serializable;

@Data
public class SysRolePageVo extends PageVo implements Serializable {
    private static final long serialVersionUID = -1225672851552603606L;

    @ApiModelProperty("角色名称")
    private String label;
}
