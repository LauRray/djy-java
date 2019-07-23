package com.djy.manager.reqVo;

import com.djy.common.V;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class UserLoginVo implements Serializable {

    private static final long serialVersionUID = 4508611368451327104L;

    @ApiModelProperty(value = "用户名",required = true)
    @NotBlank(message = "username"+ V.empty)
    private String username;

    @ApiModelProperty(value = "密码",required = true)
    @NotBlank(message = "password"+V.empty)
    private String password;
}
