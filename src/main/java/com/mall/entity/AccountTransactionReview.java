package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("account_transaction_review")
@Schema(description = "账户交易审核记录实体")
public class AccountTransactionReview {
    @TableId(type = IdType.AUTO)
    @Schema(description = "自增ID")
    private Long id;
    
    @Schema(description = "交易ID")
    private String transactionId;
    
    @Schema(description = "账户ID")
    private String accountId;
    
    @Schema(description = "用户ID")
    private String userId;
    
    @Schema(description = "交易类型(1:充值,2:提现)")
    private Integer transactionType;
    
    @Schema(description = "交易金额")
    private BigDecimal amount;
    
    @Schema(description = "支付类型(针对提现：1:支付宝,2:微信,3:银行卡)")
    private Integer paymentType;
    
    @Schema(description = "转账截图URL(针对充值)")
    private String transferImageUrl;
    
    @Schema(description = "审核状态(0:待审核,1:已通过,2:已拒绝)")
    private Integer reviewStatus;
    
    @Schema(description = "用户备注")
    private String remark;
    
    @Schema(description = "审核人ID")
    private String reviewerId;
    
    @Schema(description = "审核时间")
    private LocalDateTime reviewTime;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "伪删除标志：0-未删除，1-已删除")
    private Integer isDeleted;

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
} 