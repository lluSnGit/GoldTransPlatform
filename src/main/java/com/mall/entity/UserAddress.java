package com.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_address")
@Schema(description = "用户地址实体")
public class UserAddress {
    @TableId(type = IdType.AUTO)
    @Schema(description = "自增ID")
    private Long id;
    
    @Schema(description = "地址随机ID")
    private String addressId;
    
    @Schema(description = "账号")
    private String account;
    
    @Schema(description = "收货人姓名")
    private String receiverName;
    
    @Schema(description = "收货人电话")
    private String receiverPhone;
    
    @Schema(description = "省份")
    private String province;
    
    @Schema(description = "城市")
    private String city;
    
    @Schema(description = "区县")
    private String district;
    
    @Schema(description = "详细地址")
    private String detailAddress;
    
    @Schema(description = "邮政编码")
    private String postCode;
    
    @Schema(description = "是否默认地址(0:否,1:是)")
    private Integer isDefault;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 