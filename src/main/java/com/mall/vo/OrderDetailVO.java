package com.mall.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderDetailVO extends OrderVO {
    private GoldInfoVO goldInfo;
    private LogisticsVO logistics;
    private PriceQuoteVO priceQuote;
} 