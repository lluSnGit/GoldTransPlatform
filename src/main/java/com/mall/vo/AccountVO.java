package com.mall.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AccountVO {
    private Long id;
    private String accountId;
    private String userId;
    private BigDecimal balance;
    private BigDecimal totalRebate;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 