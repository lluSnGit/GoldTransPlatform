package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("user_level_rebates")
@Schema(description = "用户等级返点实体")
public class UserLevelRebate {
    @TableId(type = IdType.AUTO)
    @Schema(description = "返点ID")
    private Long id;
    
    @Schema(description = "用户等级")
    private Integer userLevel;
    
    @Schema(description = "用户ID")
    private String userId;
    
    @Schema(description = "返点比例")
    private BigDecimal rebateRate;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 