package com.mall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
@Schema(description = "账户交易审核请求DTO")
public class AccountTransactionReviewDTO {

    @Schema(description = "账户ID", required = true)
    private String accountId;

    @Schema(description = "用户ID", required = true)
    private String userId;

    @Schema(description = "交易类型(1:充值,2:提现)", required = true)
    private Integer transactionType;

    @Schema(description = "交易金额", required = true)
    private BigDecimal amount;

    @Schema(description = "支付类型(针对提现：1:支付宝,2:微信,3:银行卡)")
    private Integer paymentType;

    @Schema(description = "转账截图URL(针对充值)")
    private String transferImage;

    @Schema(description = "用户备注")
    private String remark;
} 