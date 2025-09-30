package com.mall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
@Schema(description = "登录结果")
public class LoginResultDTO {
    
    @Schema(description = "用户ID")
    private String userId;
    
    @Schema(description = "账号")
    private String account;
    
    @Schema(description = "用户名")
    private String username;
    
    @Schema(description = "用户等级")
    private Integer userLevel;
    
    @Schema(description = "角色等级")
    private Integer roleLevel;
    
    @Schema(description = "访问令牌")
    private String token;
} 