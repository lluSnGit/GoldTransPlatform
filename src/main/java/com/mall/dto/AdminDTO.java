package com.mall.dto;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class AdminDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    private String password;
    
    private String realName;
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    @Schema(description = "邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(description = "管理员类型：0-普通管理员，1-超级管理员")
    @Min(value = 0, message = "管理员类型不正确")
    @Max(value = 1, message = "管理员类型不正确")
    private Integer adminType;
} 