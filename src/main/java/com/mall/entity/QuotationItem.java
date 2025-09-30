package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("quotation_item")
@Schema(description = "报价项")
public class QuotationItem {
    
    @TableId(type = IdType.INPUT)
    @Schema(description = "报价项ID")
    private String id;
    
    @Schema(description = "报价ID")
    private String quotationId;
    
    @Schema(description = "黄金类型")
    private String goldType;
    
    @Schema(description = "图片链接")
    private String imageUrl;
    
    @Schema(description = "克重")
    private BigDecimal weight;
    
    @Schema(description = "价格")
    private BigDecimal price;
    
    @Schema(description = "含金量")
    private BigDecimal goldContent;
    
    @Schema(description = "熔后重")
    private BigDecimal meltedWeight;
    
    @Schema(description = "纯金重")
    private BigDecimal pureGoldWeight;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 