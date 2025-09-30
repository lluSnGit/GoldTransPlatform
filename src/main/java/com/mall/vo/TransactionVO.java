package com.mall.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionVO {
    private Long id;
    private String transactionCode;
    private String userId;
    private String accountId;
    private Long orderId;
    private Integer type;
    private BigDecimal amount;
    private BigDecimal balance;
    private Integer status;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 