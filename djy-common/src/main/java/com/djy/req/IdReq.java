package com.djy.req;

import com.djy.common.V;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class IdReq implements Serializable {
    private static final long serialVersionUID = 8356028647594384656L;


    private Long id;
}
