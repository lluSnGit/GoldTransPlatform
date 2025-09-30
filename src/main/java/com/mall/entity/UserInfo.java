package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_info")
@Schema(description = "用户信息实体")
public class UserInfo {
    @TableId(type = IdType.AUTO)
    @Schema(description = "自增ID")
    private Long id;
    
    @Schema(description = "用户随机ID")
    private String userId;
    
    @Schema(description = "真实姓名")
    private String realName;
    
    @Schema(description = "性别(0:未知,1:男,2:女)")
    private Integer gender;
    
    @Schema(description = "手机号")
    private String phone;
    
    @Schema(description = "邮箱")
    private String email;
    
    @Schema(description = "身份证号")
    private String idCard;
    
    @Schema(description = "生日")
    private LocalDateTime birthday;
    
    @Schema(description = "头像")
    private String avatar;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 