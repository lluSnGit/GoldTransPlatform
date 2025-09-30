package com.mall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "商品类别DTO")
public class ProductCategoryDTO {

    @Schema(description = "类别ID")
    private Long id;

    @Schema(description = "类别名称")
    @NotBlank(message = "类别名称不能为空")
    private String name;
} 