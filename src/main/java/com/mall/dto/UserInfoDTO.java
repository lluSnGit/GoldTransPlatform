package com.mall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Schema(description = "用户信息DTO")
public class UserInfoDTO {
    @Schema(description = "真实姓名")
    private String realName;
    
    @Schema(description = "性别(0:未知,1:男,2:女)")
    @Min(value = 0, message = "性别值不正确")
    @Max(value = 2, message = "性别值不正确")
    private Integer gender;
    
    @Schema(description = "手机号")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    @Schema(description = "邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @Schema(description = "身份证号")
    @Pattern(regexp = "^[1-9]\\d{5}(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]$", message = "身份证号格式不正确")
    private String idCard;
    
    @Schema(description = "生日")
    private LocalDateTime birthday;
    
    @Schema(description = "头像")
    private String avatar;
} 