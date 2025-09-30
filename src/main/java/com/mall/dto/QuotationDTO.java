package com.mall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "报价DTO")
public class QuotationDTO {
    @Schema(description = "报价ID")
    private String id;

    @Schema(description = "订单ID")
    private String orderId;

    @Schema(description = "运费")
    private BigDecimal shippingFee;

    @Schema(description = "保险费")
    private BigDecimal insuranceFee;

    @Schema(description = "服务费")
    private BigDecimal serviceFee;

    @Schema(description = "其他项目")
    private String otherItems;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "代金券金额")
    private BigDecimal voucherAmount;

    @NotEmpty(message = "报价项不能为空")
    @Schema(description = "报价项列表")
    private List<QuotationItemDTO> items;
} 