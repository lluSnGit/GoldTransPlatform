package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("products")
@Schema(description = "商品实体")
public class Product {
    @TableId(type = IdType.INPUT)
    @Schema(description = "商品编号")
    private String productCode;
    
    @Schema(description = "商品名称")
    private String name;
    
    @Schema(description = "商品描述")
    private String description;
    
    @Schema(description = "商品类型 (1:直接定价, 2:金基商品)")
    private Integer productType;
    
    @Schema(description = "商品类别ID")
    private Long categoryId;
    
    @Schema(description = "手工费")
    private BigDecimal craftingFee;
    
    @Schema(description = "预估价格")
    private BigDecimal estimatedPrice;
    
    @Schema(description = "最终价格")
    private BigDecimal finalPrice;
    
    @Schema(description = "检测结果")
    private String inspectionResult;
    
    @Schema(description = "商品状态(1:待检测,2:已检测,3:已定价)")
    private Integer status;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 