package com.mall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "返点计算请求参数")
public class RebateCalculationDTO {
    
    @Schema(description = "用户ID")
    private String userId;
    
    @Schema(description = "贵金属类型 0=黄金 1=铂金 2=钯金")
    private Integer category;
    
    @Schema(description = "克重")
    private BigDecimal weight;
} 