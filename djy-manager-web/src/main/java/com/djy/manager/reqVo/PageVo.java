package com.djy.manager.reqVo;

import com.djy.common.V;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * @author win 10
 */
@Data
public class PageVo implements Serializable {
    private static final long serialVersionUID = -8771815426654668242L;

    @ApiModelProperty("页数")
    @Min(value = 1,message ="页数"+V.minlength+1)
    private int pageNum;

    @ApiModelProperty("每页长度")
    @Min(value = 1,message =V.minlength+1)
    @Max(value = 50,message =V.maxlength+50)
    private int pageSize;
}
