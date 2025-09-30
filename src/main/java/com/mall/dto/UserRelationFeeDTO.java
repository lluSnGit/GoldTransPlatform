package com.mall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "用户关系费率信息")
public class UserRelationFeeDTO {
    @Schema(description = "上级用户ID")
    private String parentId;
    
    @Schema(description = "上级用户等级")
    private Integer parentLevel;
    
    @Schema(description = "上级费率")
    private BigDecimal parentFeeRate;
    
    @Schema(description = "上上级用户ID")
    private String grandParentId;
    
    @Schema(description = "上上级用户等级")
    private Integer grandParentLevel;
    
    @Schema(description = "上上级费率")
    private BigDecimal grandParentFeeRate;
}