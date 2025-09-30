package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("account")
@Schema(description = "账户实体")
public class Account {
    @TableId(type = IdType.AUTO)
    @Schema(description = "自增ID")
    private Long id;
    
    @Schema(description = "账户ID")
    private String accountId;
    
    @Schema(description = "用户随机ID")
    private String userId;
    
    @Schema(description = "账户余额")
    private BigDecimal balance = BigDecimal.ZERO;
    
    @Schema(description = "累计返点")
    private BigDecimal totalRebate = BigDecimal.ZERO;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 