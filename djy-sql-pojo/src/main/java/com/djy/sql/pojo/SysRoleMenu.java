package com.djy.sql.pojo;

import javax.persistence.Table;

@Table(name = "sys_role_menu")
public class SysRoleMenu {

    private Long roleId;

    private Long menuId;

    private Integer type;
}
