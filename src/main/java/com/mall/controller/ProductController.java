package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.Product;
import com.mall.service.ProductService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import com.mall.dto.ProductDTO;
import org.springframework.validation.annotation.Validated;
import com.mall.vo.ProductDetailVO;
import com.mall.service.ProductCategoryService;
import com.mall.dto.ProductCategoryDTO;
import org.springframework.beans.BeanUtils;

@RestController
@RequestMapping("/api/products")
@Tag(name = "商品管理", description = "商品相关接口")
@Slf4j
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductCategoryService productCategoryService;
    
    @PostMapping
    @Operation(summary = "创建商品")
    public Result<Boolean> createProduct(@ModelAttribute @Validated ProductDTO productDto) {
        boolean success = productService.createProduct(productDto);
        if (success) {
            return Result.success(true);
        } else {
            return Result.error("创建商品失败");
        }
    }
    
    @GetMapping("/{productCode}")
    @Operation(summary = "获取商品信息")
    public Result<ProductDetailVO> getProduct(@Parameter(description = "商品编号") @PathVariable String productCode) {
        Product product = productService.getByProductCode(productCode);
        if (product == null) {
            return Result.error("商品不存在");
        }
        
        ProductDetailVO productDetailVO = new ProductDetailVO();
        
        // 复制商品信息到 VO
        ProductDTO productDto = new ProductDTO();
        BeanUtils.copyProperties(product, productDto);
        productDetailVO.setProduct(productDto);
        
        // 获取并设置商品类别信息
        if (product.getCategoryId() != null) {
            ProductCategoryDTO categoryDto = productCategoryService.getCategoryById(product.getCategoryId());
            productDetailVO.setCategory(categoryDto);
        } else {
            productDetailVO.setCategory(null); // 或者一个表示无类别的信息
        }
        
        return Result.success(productDetailVO);
    }
    
    @GetMapping("/list")
    @Operation(summary = "分页查询商品列表")
    public Result<IPage<Product>> listProducts(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Product> page = new Page<>(pageNum, pageSize);
        return Result.success(productService.listProducts(page));
    }
    
    @PutMapping("/{productCode}")
    @Operation(summary = "更新商品信息")
    public Result<Boolean> updateProduct(@Parameter(description = "商品编号") @PathVariable String productCode, @ModelAttribute @Validated ProductDTO productDto) {
        boolean success = productService.updateProduct(productCode, productDto);
        if (success) {
            return Result.success(true);
        } else {
            return Result.error("更新商品失败，可能商品ID不存在");
        }
    }
    
    @PutMapping("/{productCode}/status")
    @Operation(summary = "更新商品状态")
    public Result<Boolean> updateProductStatus(
            @Parameter(description = "商品编号") @PathVariable String productCode,
            @Parameter(description = "商品状态(1:待检测,2:已检测,3:已定价)") @RequestParam Integer status) {
        return Result.success(productService.updateProductStatus(productCode, status));
    }
    
    @PutMapping("/{productCode}/price")
    @Operation(summary = "设置商品价格")
    public Result<Boolean> setProductPrice(
            @Parameter(description = "商品编号") @PathVariable String productCode,
            @Parameter(description = "预估价格") @RequestParam(required = false) BigDecimal estimatedPrice,
            @Parameter(description = "最终价格") @RequestParam(required = false) BigDecimal finalPrice) {
        return Result.success(productService.setProductPrice(productCode, estimatedPrice, finalPrice));
    }
    
    @PutMapping("/{productCode}/inspection")
    @Operation(summary = "更新检测结果")
    public Result<Boolean> updateInspectionResult(
            @Parameter(description = "商品编号") @PathVariable String productCode,
            @Parameter(description = "检测结果") @RequestParam String result) {
        return Result.success(productService.updateInspectionResult(productCode, result));
    }
    
    @DeleteMapping("/{productCode}")
    @Operation(summary = "删除商品")
    public Result<Boolean> deleteProduct(@Parameter(description = "商品编号") @PathVariable String productCode) {
        boolean success = productService.deleteProduct(productCode);
        if (success) {
            return Result.success(true);
        } else {
            return Result.error("删除商品失败，可能商品不存在");
        }
    }
    
    @PostMapping("/batch/delete")
    @Operation(summary = "批量删除商品")
    public Result<Boolean> batchDeleteProducts(@Parameter(description = "商品编号列表") @RequestParam List<String> productCodes) {
        return Result.success(productService.batchDeleteProducts(productCodes));
    }
    
    @PostMapping("/batch/status")
    @Operation(summary = "批量更新商品状态")
    public Result<Boolean> batchUpdateStatus(
            @Parameter(description = "商品编号列表") @RequestParam List<String> productCodes,
            @Parameter(description = "商品状态") @RequestParam Integer status) {
        return Result.success(productService.batchUpdateStatus(productCodes, status));
    }
    
    @Hidden
    @GetMapping("/internal")
    public Result<?> internalApi() {
        return Result.success("内部接口");
    }
} 