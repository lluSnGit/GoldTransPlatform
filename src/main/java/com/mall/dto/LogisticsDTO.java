package com.mall.dto;

import lombok.Data;
import javax.validation.constraints.*;

@Data
public class LogisticsDTO {
    @NotBlank(message = "物流单号不能为空")
    private String logisticsCode;
    
    @NotBlank(message = "物流公司不能为空")
    private String logisticsCompany;
    
    @NotBlank(message = "发件人姓名不能为空")
    private String senderName;
    
    @NotBlank(message = "发件人电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String senderPhone;
    
    @NotBlank(message = "发件人地址不能为空")
    private String senderAddress;
    
    @NotBlank(message = "收件人姓名不能为空")
    private String receiverName;
    
    @NotBlank(message = "收件人电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String receiverPhone;
    
    @NotBlank(message = "收件人地址不能为空")
    private String receiverAddress;
} 