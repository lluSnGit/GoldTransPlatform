package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user")
@Schema(description = "用户实体")
public class User {
    @TableId(type = IdType.AUTO)
    @Schema(description = "自增ID")
    private Long id;
    
    @Schema(description = "用户随机ID")
    private String userId;
    
    @Schema(description = "账号")
    private String account;
    
    @Schema(description = "密码")
    private String password;
    
    @Schema(description = "用户名")
    private String username;
    
    @Schema(description = "用户等级")
    private Integer userLevel;
    
    @Schema(description = "角色等级")
    private Integer roleLevel;
    
    @Schema(description = "父级ID")
    private Long parentId;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
} 