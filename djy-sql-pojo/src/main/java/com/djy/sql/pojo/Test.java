package com.djy.sql.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author 吴永杰
 */
@Data
@Table(name = "test")
public class Test implements Serializable {

    private static final long serialVersionUID = -8012130700501349985L;
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private String name;
}
