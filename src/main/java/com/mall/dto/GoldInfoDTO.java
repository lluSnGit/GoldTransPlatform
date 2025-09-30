package com.mall.dto;

import lombok.Data;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class GoldInfoDTO {
    @NotBlank(message = "黄金类型不能为空")
    private String goldType;
    
    @NotNull(message = "重量不能为空")
    @DecimalMin(value = "0.001", message = "重量必须大于0")
    private BigDecimal weight;
    
    @NotNull(message = "成色不能为空")
    @DecimalMin(value = "0", message = "成色必须大于等于0")
    @DecimalMax(value = "100", message = "成色必须小于等于100")
    private BigDecimal purity;
    
    private String condition;
} 