package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("account_transaction")
@Schema(description = "账户交易记录实体")
public class AccountTransaction {
    @TableId(type = IdType.AUTO)
    @Schema(description = "自增ID")
    private Long id;
    
    @Schema(description = "交易ID")
    private String transactionId;
    
    @Schema(description = "账户ID")
    private String accountId;
    
    @Schema(description = "用户ID")
    private String userId;
    
    @Schema(description = "交易类型(1:充值,2:提现,3:返点增加,4:返点减少)")
    private Integer type;
    
    @Schema(description = "交易金额")
    private BigDecimal amount;
    
    @Schema(description = "交易后余额")
    private BigDecimal balance;
    
    @Schema(description = "交易后返点")
    private BigDecimal totalRebate;
    
    @Schema(description = "交易状态(0:失败,1:成功)")
    private Integer status;
    
    @Schema(description = "备注")
    private String remark;
    
    @Schema(description = "转账截图URL")
    private String transferImageUrl;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 