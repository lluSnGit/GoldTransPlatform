package com.mall.vo;

import com.mall.dto.ProductDTO;
import com.mall.dto.ProductCategoryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "商品详情VO")
public class ProductDetailVO {

    @Schema(description = "商品信息")
    private ProductDTO product;

    @Schema(description = "商品类别信息")
    private ProductCategoryDTO category;

    // 您也可以在这里直接包含 ProductDTO 和 ProductCategoryDTO 的字段，而不是嵌套对象
    // 例如: private Long id; private String productCode; private String name; ...
    // private Long categoryId; private String categoryName; ...
} 