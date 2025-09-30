package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Order {
    @TableId(type = IdType.AUTO)
    @Schema(description = "订单ID")
    private Long id;
    
    @Schema(description = "订单编号")
    private String orderCode;
    
    @Schema(description = "用户ID")
    private String userId;
    
    @Schema(description = "商品编号")
    private String productCode;
    
    @Schema(description = "订单状态(1:待审核,2:已审核,3:待取件,4:已收货,5:待检测,6:已检测,7:已定价,8:已完成)")
    private Integer status;
    
    @Schema(description = "交货方式(1:到店,2:邮寄)")
    private Integer deliveryType;
    
    @Schema(description = "预估价格")
    private BigDecimal estimatedPrice;
    
    @Schema(description = "最终价格")
    private BigDecimal finalPrice;
    
    @Schema(description = "平台手续费")
    private BigDecimal platformFee;
    
    @Schema(description = "用户返点")
    private BigDecimal userRebate;
    
    @Schema(description = "备注")
    private String remark;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 