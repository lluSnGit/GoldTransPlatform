package com.mall.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "返点计算结果")
public class RebateCalculationVO {
    
    @Schema(description = "用户ID")
    private String userId;
    
    @Schema(description = "用户等级")
    private Integer userLevel;
    
    @Schema(description = "用户费率")
    private BigDecimal userFeeRate;
    
    @Schema(description = "上级用户ID")
    private String parentUserId;
    
    @Schema(description = "上级用户等级")
    private Integer parentUserLevel;
    
    @Schema(description = "上级用户费率")
    private BigDecimal parentFeeRate;
    
    @Schema(description = "上级返点金额")
    private BigDecimal parentRebateAmount;
    
    @Schema(description = "上上级用户ID")
    private String grandParentUserId;
    
    @Schema(description = "上上级用户等级")
    private Integer grandParentUserLevel;
    
    @Schema(description = "上上级用户费率")
    private BigDecimal grandParentFeeRate;
    
    @Schema(description = "上上级返点金额")
    private BigDecimal grandParentRebateAmount;
    
    @Schema(description = "贵金属类型")
    private Integer category;
    
    @Schema(description = "克重")
    private BigDecimal weight;
} 