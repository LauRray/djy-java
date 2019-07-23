package com.djy.sql.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Data
public class SysMenu implements Serializable {
    private static final long serialVersionUID = 1752571572765784395L;

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private String title;

    private String promis;

    private Long pid;

    private String url;

    private String icon;

    private String vuePath;

    private Integer type;

    @Transient
    List<SysMenu> children;

    @Transient
    private Boolean spread=true;

    @Transient
    private Boolean checked;
}
