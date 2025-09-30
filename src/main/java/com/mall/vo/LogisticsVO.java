package com.mall.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LogisticsVO {
    private Long id;
    private Long orderId;
    private String logisticsCode;
    private String logisticsCompany;
    private String senderName;
    private String senderPhone;
    private String senderAddress;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private Integer status;
    private String trackingInfo;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 