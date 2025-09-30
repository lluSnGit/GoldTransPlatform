package com.mall.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderVO {
    private Long id;
    private String orderCode;
    private Integer orderType;
    private Integer status;
    private BigDecimal estimatedPrice;
    private BigDecimal finalPrice;
    private BigDecimal platformFee;
    private BigDecimal userRebate;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 