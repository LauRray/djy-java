package com.djy.sql.pojo;

import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "sys_user_role")
public class SysUserRole implements Serializable {

    private static final long serialVersionUID = -4032190108833789089L;
    private Long userId;

    private Long roleId;
}
