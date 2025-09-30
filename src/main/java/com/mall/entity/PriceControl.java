package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("price_control")
@Schema(description = "价格控制实体")
public class PriceControl {
    @TableId(type = IdType.AUTO)
    @Schema(description = "自增ID")
    private Long id;
    
    @Schema(description = "回收价")
    private BigDecimal recyclePrice;
    
    @Schema(description = "出售价")
    private BigDecimal sellPrice;
    
    @Schema(description = "价格类型(1:金价,2:铂金价)")
    private Integer priceType;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 