package com.djy.manager.reqVo.sysLog;

import com.djy.common.V;
import com.djy.manager.reqVo.PageVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

@Data
public class SysLogPageVo extends PageVo implements Serializable {

    private static final long serialVersionUID = -8117275707981712265L;
    @ApiModelProperty(value = "日志类型，1登录 2操作",required = true)
    @Max(value = 2,message = "type"+V.maxNum+2)
    @Min(value = 1,message = "type"+V.minNum+1)
    private Integer type;

    @ApiModelProperty(value = "状态 0失败 1成功",required = false)
    private Boolean status;

    @ApiModelProperty(value = "用户名",required = false)
    private String username;
}
