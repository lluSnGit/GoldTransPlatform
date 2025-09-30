package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@TableName("admin")
public class Admin {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String adminId;
    
    private String username;
    
    private String password;
    
    private String realName;
    
    private String phone;
    
    @Schema(description = "邮箱")
    private String email;
    
    @Schema(description = "管理员类型：0-普通管理员，1-超级管理员")
    private Integer adminType;
    
    @Schema(description = "状态：0-禁用，1-启用")
    private Integer status; // 0: 禁用, 1: 启用
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private String lastLoginIp;
    
    private LocalDateTime lastLoginTime;
} 