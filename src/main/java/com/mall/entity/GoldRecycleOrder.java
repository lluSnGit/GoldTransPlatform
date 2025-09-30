package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@TableName("gold_recycle_order")
public class GoldRecycleOrder {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String orderId;
    
    private String account;
    
    private String goldType;
    
    private BigDecimal estimatedWeight;
    
    private String purity;
    
    private String goldCondition;
    
    private String description;
    
    private String receiverName;
    
    private String receiverPhone;
    
    private String addressId;
    
    @Schema(description = "图片URL，多个URL用逗号分隔")
    private String images;
    
    private Integer status;
    
    private BigDecimal estimatedPrice;
    
    private BigDecimal finalPrice;
    
    private String inspectionResult;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;

    /**
     * 快递公司
     */
    private String expressCompany;

    /**
     * 快递单号
     */
    private String trackingNumber;
} 