package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("price_quote")
public class PriceQuote {
    @TableId(type = IdType.AUTO)
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