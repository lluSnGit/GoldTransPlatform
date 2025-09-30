package com.mall.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PriceQuoteVO {
    private Long id;
    private Long orderId;
    private BigDecimal quotePrice;
    private LocalDateTime quoteTime;
    private Integer quoteStatus;
    private LocalDateTime confirmTime;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 