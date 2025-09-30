package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("gold_info")
public class GoldInfo {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;
    private String goldType;
    private BigDecimal weight;
    private BigDecimal purity;
    private String condition;
    private String images;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 