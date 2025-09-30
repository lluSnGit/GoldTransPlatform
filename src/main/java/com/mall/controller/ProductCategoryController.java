package com.mall.controller;

import com.mall.common.api.CommonResult;
import com.mall.dto.ProductCategoryDTO;
import com.mall.service.ProductCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-categories")
@Tag(name = "商品类别管理", description = "商品类别相关接口")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping
    @Operation(summary = "获取所有商品类别")
    public CommonResult<List<ProductCategoryDTO>> getAllCategories() {
        List<ProductCategoryDTO> categories = productCategoryService.getAllCategories();
        return CommonResult.success(categories);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取商品类别详情")
    public CommonResult<ProductCategoryDTO> getCategoryById(@PathVariable Long id) {
        ProductCategoryDTO category = productCategoryService.getCategoryById(id);
        if (category != null) {
            return CommonResult.success(category);
        } else {
            return CommonResult.failed("商品类别不存在");
        }
    }

    @PostMapping
    @Operation(summary = "创建新的商品类别")
    public CommonResult<Boolean> createCategory(@RequestBody @Validated ProductCategoryDTO dto) {
        // 创建时，ID从DTO中获取，由用户手动传入
        boolean success = productCategoryService.createCategory(dto);
        if (success) {
            return CommonResult.success(true);
        } else {
            return CommonResult.failed("创建商品类别失败，可能类别ID已存在");
        }
    }

    @PutMapping
    @Operation(summary = "更新商品类别")
    public CommonResult<Boolean> updateCategory(@RequestBody @Validated ProductCategoryDTO dto) {
        // 更新时，ID从DTO中获取，由用户手动传入
        boolean success = productCategoryService.updateCategory(dto);
        if (success) {
            return CommonResult.success(true);
        } else {
            return CommonResult.failed("更新商品类别失败，可能类别ID不存在");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除商品类别")
    public CommonResult<Boolean> deleteCategory(@PathVariable Long id) {
        boolean success = productCategoryService.deleteCategory(id);
        if (success) {
            return CommonResult.success(true);
        } else {
            return CommonResult.failed("删除商品类别失败，可能类别ID不存在");
        }
    }
} 