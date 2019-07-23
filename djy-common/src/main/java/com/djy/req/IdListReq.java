package com.djy.req;

import com.djy.common.V;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
public class IdListReq implements Serializable {

    private static final long serialVersionUID = -7351285742943818863L;
    @Size(min = 1 , message = V.listlength)
    private List<Long> list;

}
