package com.mall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Schema(description = "商品DTO")
public class ProductDTO {

    @Schema(description = "商品编号")
    private String productCode;

    @Schema(description = "商品名称")
    @NotBlank(message = "商品名称不能为空")
    private String name;

    @Schema(description = "商品描述")
    private String description;

    @Schema(description = "商品类型 (1:直接定价, 2:金基商品)")
    @NotNull(message = "商品类型不能为空")
    private Integer productType;

    @Schema(description = "商品类别ID")
    @NotNull(message = "商品类别ID不能为空")
    private Long categoryId;

    @Schema(description = "手工费")
    private BigDecimal craftingFee;

    @Schema(description = "预估价格")
    private BigDecimal estimatedPrice;

    @Schema(description = "最终价格")
    private BigDecimal finalPrice;

    @Schema(description = "检测结果")
    private String inspectionResult;

    @Schema(description = "商品状态(1:待检测,2:已检测,3:已定价)")
    private Integer status;
} 