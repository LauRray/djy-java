package com.djy.sql.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "sys_bill")
public class SysBill {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private String billTitle;

    private String bitImg;

    private Boolean hasPublish;

    private Date createDate;

    private Boolean delFlag;

    private String h5Url;
}
