package com.djy.sql.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "sys_role")
public class SysRole implements Serializable {
    private static final long serialVersionUID = 1681124961050257829L;

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private String label;

    private String remarks;

    private Date creatTime;

    private Boolean status;

    @Transient
    List<SysMenu> list;
}
