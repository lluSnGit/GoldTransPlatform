package com.mall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@Data
@Schema(description = "黄金回收订单DTO")
public class GoldRecycleOrderDTO {
    @NotBlank(message = "账号不能为空")
    @Schema(description = "账号")
    private String account;

    @NotBlank(message = "黄金类型不能为空")
    @Schema(description = "黄金类型")
    private String goldType;
    
    @NotNull(message = "预估重量不能为空")
    @Schema(description = "预估重量(克)")
    private BigDecimal estimatedWeight;
    
    @Schema(description = "预估价格")
    private BigDecimal estimatedPrice;
    
    @NotBlank(message = "纯度不能为空")
    @Schema(description = "成色")
    private String purity;
    
    @NotBlank(message = "品相不能为空")
    @Schema(description = "品相")
    private String goldCondition;
    
    @Schema(description = "描述")
    private String description;
    
    @NotBlank(message = "收货人姓名不能为空")
    @Schema(description = "收货人姓名")
    private String receiverName;
    
    @NotBlank(message = "收货人电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "收货人电话")
    private String receiverPhone;
    
    @NotNull(message = "收货地址ID不能为空")
    @Schema(description = "收货地址ID")
    private String addressId;
    
    @Schema(description = "图片URL列表")
    private List<String> images;
} 