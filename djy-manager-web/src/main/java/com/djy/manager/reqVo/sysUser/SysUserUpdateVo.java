package com.djy.manager.reqVo.sysUser;

import com.djy.common.V;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

@Data
public class SysUserUpdateVo implements Serializable {

    private static final long serialVersionUID = -4670255332157399781L;
    @ApiModelProperty(value = "用户ID",required = true)
    @NotNull(message = "id"+ V.empty)
    private Long id;

    @ApiModelProperty(value = "用户名",required = true)
    @Length(min = 1,max = 20,message = "用户名"+V.lengthScope+"1-20")
    private String username;

    /**
     * 1正常 2停用
     */
    @Min(value = 1,message = "状态"+V.minNum+1)
    @Max(value = 2,message = "状态"+V.maxNum+2)
    @NotNull(message = "状态"+V.empty)
    @ApiModelProperty(value = "状态")
    private Integer status;

    @NotBlank(message ="邮箱"+ V.empty)
    @Email(message = V.email)
    @ApiModelProperty(value = "邮箱",required = true)
    private String email;

    @ApiModelProperty(value = "手机号",required = true)
    @Length(min = 11,max = 11,message = "手机号格式错误")
    private String tel;

    @ApiModelProperty(value = "真实姓名",required = true)
    @Length(min = 1,max = 10,message = "真实姓名"+V.lengthScope+"1-10")
    private String realName;

    @ApiModelProperty(value = "用户头像", required= true)
    @NotBlank(message = "请上传头像")
    private String img;

    @ApiModelProperty(value = "角色",required = false)
    private List<Long> list;
}
