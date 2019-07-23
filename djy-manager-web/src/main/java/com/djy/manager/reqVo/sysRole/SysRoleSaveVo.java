package com.djy.manager.reqVo.sysRole;

import com.djy.common.V;
import com.djy.req.IdReq;
import com.djy.sql.pojo.SysMenu;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class SysRoleSaveVo extends IdReq implements Serializable {
    private static final long serialVersionUID = -7136434480170583548L;

    @NotBlank(message = "名称"+ V.empty)
    private String label;


    private String remarks;

    private Date creatTime;

    @NotNull(message = "状态"+V.empty)
    private Boolean status;


    List<SysMenu> list;
}
