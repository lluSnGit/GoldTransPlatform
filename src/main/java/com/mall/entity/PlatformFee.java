package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("platform_fees")
@Schema(description = "平台手续费实体")
public class PlatformFee {
    @TableId(type = IdType.AUTO)
    @Schema(description = "手续费ID")
    private Long id;
    
    @Schema(description = "账号")
    private String account;
    
    @Schema(description = "手续费率")
    private BigDecimal feeRate;
    
    @Schema(description = "用户等级")
    private Integer userLevel;
    
    @Schema(description = "用户等级名称")
    private String levelName;
    
    /**
     * 类别 0=黄金 1=铂金 2=钯金
     */
    private Integer category;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 