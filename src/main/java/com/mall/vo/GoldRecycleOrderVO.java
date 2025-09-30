package com.mall.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Data
@Schema(description = "黄金回收订单VO")
public class GoldRecycleOrderVO {
    @Schema(description = "订单ID")
    private Long id;
    
    @Schema(description = "订单编号")
    private String orderId;
    
    @Schema(description = "账号")
    private String account;
    
    @Schema(description = "黄金类型")
    private String goldType;
    
    @Schema(description = "预估重量(克)")
    private BigDecimal estimatedWeight;
    
    @Schema(description = "成色")
    private String purity;
    
    @Schema(description = "品相")
    private String goldCondition;
    
    @Schema(description = "描述")
    private String description;
    
    @Schema(description = "收货人姓名")
    private String receiverName;
    
    @Schema(description = "收货人电话")
    private String receiverPhone;
    
    @Schema(description = "收货地址ID")
    private String addressId;
    
    @Schema(description = "图片URL")
    private String imageBase64;
    
    @Schema(description = "图片URL列表")
    private List<String> imageList;
    
    @Schema(description = "订单状态")
    private Integer status;
    
    @Schema(description = "预估价格")
    private BigDecimal estimatedPrice;
    
    @Schema(description = "最终价格")
    private BigDecimal finalPrice;
    
    @Schema(description = "检测结果")
    private String inspectionResult;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
    
    @Schema(description = "地址信息")
    private String addressInfo;
} 