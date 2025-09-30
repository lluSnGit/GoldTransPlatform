package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.entity.Product;
import com.mall.dto.ProductDTO;
import com.mall.mapper.ProductMapper;
import com.mall.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    
    @Override
    @Transactional
    public boolean createProduct(ProductDTO productDto) {
        Product product = new Product();
        BeanUtils.copyProperties(productDto, product);
        if (!StringUtils.hasText(product.getProductCode())) {
            return false;
        }
        if (product.getProductType() != null) {
            if (product.getProductType() == 1) {
                if (product.getEstimatedPrice() == null || product.getFinalPrice() == null) {
                    // 您可能需要更具体的校验或默认值设置
                }
                product.setCraftingFee(BigDecimal.ZERO);
            } else if (product.getProductType() == 2) {
                if (product.getCraftingFee() == null) {
                    // 您可能需要更具体的校验或默认值设置
                }
                product.setEstimatedPrice(BigDecimal.ZERO);
                product.setFinalPrice(BigDecimal.ZERO);
            }
        }
        product.setCreateTime(java.time.LocalDateTime.now());
        product.setUpdateTime(java.time.LocalDateTime.now());
        return save(product);
    }
    
    @Override
    @Transactional
    public boolean updateProduct(String productCode, ProductDTO productDto) {
        Product product = getByProductCode(productCode);
        if (product == null) {
            return false;
        }
        BeanUtils.copyProperties(productDto, product, "productCode", "createTime");
        
        if (productDto.getProductType() != null) {
            if (productDto.getProductType() == 1) {
                if (productDto.getEstimatedPrice() != null) product.setEstimatedPrice(productDto.getEstimatedPrice());
                if (productDto.getFinalPrice() != null) product.setFinalPrice(productDto.getFinalPrice());
                product.setCraftingFee(BigDecimal.ZERO);
            } else if (productDto.getProductType() == 2) {
                if (productDto.getCraftingFee() != null) product.setCraftingFee(productDto.getCraftingFee());
                product.setEstimatedPrice(BigDecimal.ZERO);
                product.setFinalPrice(BigDecimal.ZERO);
            }
        } else {
            if (product.getProductType() == 1) {
                if (productDto.getEstimatedPrice() != null) product.setEstimatedPrice(productDto.getEstimatedPrice());
                if (productDto.getFinalPrice() != null) product.setFinalPrice(productDto.getFinalPrice());
            } else if (product.getProductType() == 2) {
                if (productDto.getCraftingFee() != null) product.setCraftingFee(productDto.getCraftingFee());
            }
        }
        product.setUpdateTime(java.time.LocalDateTime.now());
        return updateById(product);
    }
    
    @Override
    public Product getByProductCode(String productCode) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getProductCode, productCode);
        return getOne(wrapper);
    }
    
    @Override
    public IPage<Product> listProducts(Page<Product> page) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Product::getCreateTime);
        return page(page, wrapper);
    }
    
    @Override
    @Transactional
    public boolean updateProductStatus(String productCode, Integer status) {
        Product product = getByProductCode(productCode);
        if (product != null) {
            product.setStatus(status);
            return updateById(product);
        }
        return false;
    }
    
    @Override
    @Transactional
    public boolean setProductPrice(String productCode, BigDecimal estimatedPrice, BigDecimal finalPrice) {
        Product product = getByProductCode(productCode);
        if (product != null) {
            if (estimatedPrice != null) {
                product.setEstimatedPrice(estimatedPrice);
            }
            if (finalPrice != null) {
                product.setFinalPrice(finalPrice);
            }
            return updateById(product);
        }
        return false;
    }
    
    @Override
    @Transactional
    public boolean updateInspectionResult(String productCode, String result) {
        Product product = getByProductCode(productCode);
        if (product != null) {
            product.setInspectionResult(result);
            return updateById(product);
        }
        return false;
    }
    
    @Override
    @Transactional
    public boolean deleteProduct(String productCode) {
        return removeById(productCode);
    }
    
    @Override
    @Transactional
    public boolean batchDeleteProducts(List<String> productCodes) {
        return removeByIds(productCodes);
    }
    
    @Override
    @Transactional
    public boolean batchUpdateStatus(List<String> productCodes, Integer status) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Product::getProductCode, productCodes);
        Product product = new Product();
        product.setStatus(status);
        return update(product, wrapper);
    }
} 