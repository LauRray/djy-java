package com.djy.sql.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor

@Data
@Table(name = "sys_log")
public class SysLog implements Serializable {

    private static final long serialVersionUID = -2969569143001444629L;
    @Id
    private Long id;

    private Long userId;

    private Integer type;

    private Boolean status;

    private String ip;

    private Date creatTime;

    private String method;

    private String url;

    private String urlDesc;

    private String reason;

    public SysLog(Long id, Long userId, Integer type, Boolean status, String ip, Date creatTime, String method, String url, String urlDesc, String reason) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.status = status;
        this.ip = ip;
        this.creatTime = creatTime;
        this.method = method;
        this.url = url;
        this.urlDesc = urlDesc;
        this.reason = reason;
    }

    @Transient
    private String username;
}
