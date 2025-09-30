package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.entity.Order;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

public interface OrderService extends IService<Order> {
    // 创建订单
    boolean createOrder(Order order);
    
    // 根据订单编号获取订单
    Order getByOrderCode(String orderCode);
    
    // 分页查询订单列表
    IPage<Order> listOrders(Page<Order> page, String userId, String productCode, Integer status, BigDecimal minAmount, BigDecimal maxAmount);
    
    // 更新订单状态
    boolean updateOrderStatus(String orderCode, Integer status);
    
    // 批量删除订单
    boolean batchDeleteOrders(List<String> orderCodes);
    
    // 批量更新订单状态
    boolean batchUpdateStatus(List<String> orderCodes, Integer status);
    
    // 获取用户订单列表
    List<Order> getUserOrders(String userId);
    
    // 获取商品订单列表
    List<Order> getProductOrders(String productCode);
    
    // 设置最终价格
    boolean setFinalPrice(Long orderId, BigDecimal finalPrice);
    
    // 计算用户返点
    BigDecimal calculateUserRebate(Long orderId);
    
    /**
     * 审核订单
     */
    Order auditOrder(Long id, boolean approved);
    
    /**
     * 确认取件
     */
    Order confirmPickup(Long id);
    
    /**
     * 确认收货
     */
    Order confirmReceive(Long id);
    
    /**
     * 检测商品
     */
    Order inspectProduct(Long id, String inspectionResult);
    
    /**
     * 设置最终价格
     */
    Order setPrice(Long id, Double finalPrice);
    
    /**
     * 完成订单
     */
    Order completeOrder(Long id);
} 