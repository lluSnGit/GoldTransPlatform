package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.dto.ProductCategoryDTO;
import com.mall.entity.ProductCategory;
import com.mall.mapper.ProductCategoryMapper;
import com.mall.service.ProductCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {

    @Override
    public List<ProductCategoryDTO> getAllCategories() {
        List<ProductCategory> categories = list();
        return categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductCategoryDTO getCategoryById(Long id) {
        ProductCategory category = getById(id);
        return convertToDTO(category);
    }

    @Override
    public boolean createCategory(ProductCategoryDTO dto) {
        ProductCategory category = new ProductCategory();
        BeanUtils.copyProperties(dto, category);
        // ID 由DTO提供，直接使用
        return save(category);
    }

    @Override
    public boolean updateCategory(ProductCategoryDTO dto) {
        ProductCategory category = getById(dto.getId());
        if (category == null) {
            return false;
        }
        BeanUtils.copyProperties(dto, category);
        // ID 从DTO中获取，不需要再次设置
        return updateById(category);
    }

    @Override
    public boolean deleteCategory(Long id) {
        return removeById(id);
    }

    private ProductCategoryDTO convertToDTO(ProductCategory category) {
        if (category == null) {
            return null;
        }
        ProductCategoryDTO dto = new ProductCategoryDTO();
        BeanUtils.copyProperties(category, dto);
        return dto;
    }
} 