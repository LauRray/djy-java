package com.djy.sql.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "sys_user")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 3283938459943769335L;
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    private Integer status;



    private Date creatTime;

    private Date lastLogin;

    private String email;

    private String tel;

    private String realName;

    private String img;

    @Transient
    @JsonIgnore
    private String authorization;

    /**
     * 角色id
     */
    @Transient
    private List<Long> list;

    @Transient
    private List<SysUserRole> sysUserRoles;
}
