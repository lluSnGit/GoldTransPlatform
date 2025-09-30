package com.mall.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PlatformFeeVO {
    private Integer level;
    private String levelName;
    private BigDecimal feeRate;
    private String account;
    private Integer category;

    public Integer getCategory() {
        return category;
    }
    public void setCategory(Integer category) {
        this.category = category;
    }
} 