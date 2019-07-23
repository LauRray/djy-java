package com.djy.manager.reqVo.sysUser;

import com.djy.manager.reqVo.PageVo;
import lombok.Data;

import java.io.Serializable;

@Data
public class SysUserPageVo extends PageVo implements Serializable {
    private static final long serialVersionUID = -2600601098449738936L;

    private String username;
}
