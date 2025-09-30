package com.mall.dto;

import lombok.Data;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMin;

@Data
@Schema(description = "报价项信息")
public class QuotationItemDTO {
    
    @Schema(description = "报价项ID", type = "string")
    private String id;
    
    @NotBlank(message = "黄金类型不能为空")
    @Schema(description = "黄金类型", type = "string", example = "au999")
    private String goldType;
    
    @Schema(description = "图片链接", type = "string")
    private String imageUrl;
    
    @NotNull(message = "克重不能为空")
    @DecimalMin(value = "0", message = "克重必须大于等于0")
    @Schema(description = "克重", type = "number", format = "double")
    private BigDecimal weight;
    
    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0", message = "价格必须大于等于0")
    @Schema(description = "价格", type = "number", format = "double")
    private BigDecimal price;
    
    @NotNull(message = "含金量不能为空")
    @DecimalMin(value = "0", message = "含金量必须大于等于0")
    @Schema(description = "含金量", type = "number", format = "double")
    private BigDecimal goldContent;
    
    @NotNull(message = "熔后重不能为空")
    @DecimalMin(value = "0", message = "熔后重必须大于等于0")
    @Schema(description = "熔后重", type = "number", format = "double")
    private BigDecimal meltedWeight;
    
    @NotNull(message = "纯金重不能为空")
    @DecimalMin(value = "0", message = "纯金重必须大于等于0")
    @Schema(description = "纯金重", type = "number", format = "double")
    private BigDecimal pureGoldWeight;
} 