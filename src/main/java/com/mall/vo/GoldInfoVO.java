package com.mall.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GoldInfoVO {
    private Long id;
    private Long orderId;
    private String goldType;
    private BigDecimal weight;
    private BigDecimal purity;
    private String condition;
    private String images;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 