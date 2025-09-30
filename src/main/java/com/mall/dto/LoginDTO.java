package com.mall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "登录参数")
public class LoginDTO {
    
    @Schema(description = "账号", required = true)
    private String account;
    
    @Schema(description = "密码", required = true)
    private String password;
} 