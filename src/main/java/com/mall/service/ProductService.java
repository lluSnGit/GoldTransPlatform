package com.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.entity.Product;
import com.mall.dto.ProductDTO;
import java.math.BigDecimal;
import java.util.List;

public interface ProductService extends IService<Product> {
    // 创建商品
    boolean createProduct(ProductDTO productDto);
    
    // 更新商品
    boolean updateProduct(String productCode, ProductDTO productDto);
    
    // 根据商品编号获取商品
    Product getByProductCode(String productCode);
    
    // 分页查询商品列表
    IPage<Product> listProducts(Page<Product> page);
    
    // 更新商品状态
    boolean updateProductStatus(String productCode, Integer status);
    
    // 设置商品价格
    boolean setProductPrice(String productCode, BigDecimal estimatedPrice, BigDecimal finalPrice);
    
    // 更新检测结果
    boolean updateInspectionResult(String productCode, String result);
    
    // 批量删除商品
    boolean batchDeleteProducts(List<String> productCodes);
    
    // 批量更新商品状态
    boolean batchUpdateStatus(List<String> productCodes, Integer status);

    // 删除商品
    boolean deleteProduct(String productCode);
} 