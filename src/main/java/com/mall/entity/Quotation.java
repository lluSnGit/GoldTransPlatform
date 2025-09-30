package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("quotation")
@Schema(description = "报价")
public class Quotation {
    
    @TableId(type = IdType.INPUT)
    @Schema(description = "报价ID")
    private String id;
    
    @Schema(description = "订单ID")
    private String orderId;
    
    @Schema(description = "运费")
    private BigDecimal shippingFee;
    
    @Schema(description = "保费")
    private BigDecimal insuranceFee;
    
    @Schema(description = "服务费")
    private BigDecimal serviceFee;
    
    @Schema(description = "其他项目")
    private String otherItems;
    
    @Schema(description = "代金券金额")
    private BigDecimal voucherAmount;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 