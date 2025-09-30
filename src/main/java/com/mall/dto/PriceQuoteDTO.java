package com.mall.dto;

import lombok.Data;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class PriceQuoteDTO {
    @NotNull(message = "报价金额不能为空")
    @DecimalMin(value = "0.01", message = "报价金额必须大于0")
    private BigDecimal quotePrice;
    
    private String remark;
} 