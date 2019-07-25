package com.djy.sql.pojo;

import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author win 10
 */
@Table(name = "sys_role_menu")
@Data
public class SysRoleMenu implements Serializable {

    private static final long serialVersionUID = 7707486730372079037L;
    private Long roleId;

    private Long menuId;

    private Integer type;
}
