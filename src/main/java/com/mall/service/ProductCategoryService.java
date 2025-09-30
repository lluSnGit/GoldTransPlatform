package com.mall.service;

import com.mall.dto.ProductCategoryDTO;
import com.mall.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryService {

    /**
     * 获取所有商品类别
     */
    List<ProductCategoryDTO> getAllCategories();

    /**
     * 根据ID获取商品类别详情
     */
    ProductCategoryDTO getCategoryById(Long id);

    /**
     * 创建新的商品类别
     */
    boolean createCategory(ProductCategoryDTO dto);

    /**
     * 更新商品类别
     */
    boolean updateCategory(ProductCategoryDTO dto);

    /**
     * 删除商品类别
     */
    boolean deleteCategory(Long id);
} 