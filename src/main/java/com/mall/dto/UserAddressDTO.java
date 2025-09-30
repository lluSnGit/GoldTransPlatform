package com.mall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.*;

@Data
@Schema(description = "用户地址DTO")
public class UserAddressDTO {
    @Schema(description = "地址ID")
    private String addressId;

    @Schema(description = "账号")
    @NotBlank(message = "账号不能为空")
    private String account;

    @Schema(description = "收货人姓名")
    @NotBlank(message = "收货人姓名不能为空")
    private String receiverName;
    
    @Schema(description = "收货人电话")
    @NotBlank(message = "收货人电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String receiverPhone;
    
    @Schema(description = "省份")
    @NotBlank(message = "省份不能为空")
    private String province;
    
    @Schema(description = "城市")
    @NotBlank(message = "城市不能为空")
    private String city;
    
    @Schema(description = "区县")
    @NotBlank(message = "区县不能为空")
    private String district;
    
    @Schema(description = "详细地址")
    @NotBlank(message = "详细地址不能为空")
    private String detailAddress;
    
    @Schema(description = "邮政编码")
    @Pattern(regexp = "^[1-9]\\d{5}$", message = "邮政编码格式不正确")
    private String postCode;
    
    @Schema(description = "是否默认地址(0:否,1:是)")
    @Min(value = 0, message = "默认地址值不正确")
    @Max(value = 1, message = "默认地址值不正确")
    private Integer isDefault;
} 