package com.mall.service;

import com.mall.dto.GoldRecycleOrderDTO;
import com.mall.entity.GoldRecycleOrder;
import com.mall.vo.GoldRecycleOrderVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.math.BigDecimal;

public interface GoldRecycleOrderService {
    /**
     * 创建回收订单
     */
    GoldRecycleOrderVO createOrder(GoldRecycleOrderDTO orderDTO, GoldRecycleOrder order);
    
    /**
     * 获取订单详情
     */
    GoldRecycleOrderVO getOrderDetail(String orderId);
    
    /**
     * 获取订单实体
     */
    GoldRecycleOrder getOrderEntity(String orderId);
    
    /**
     * 分页查询所有订单
     */
    IPage<GoldRecycleOrderVO> listAllOrders(Page<GoldRecycleOrder> page, BigDecimal estimatedPrice);
    
    /**
     * 更新订单状态
     */
    boolean updateOrderStatus(String orderId, Integer status);
    
    /**
     * 设置预估价格
     */
    boolean setEstimatedPrice(String orderId, BigDecimal estimatedPrice);
    
    /**
     * 设置最终价格
     */
    boolean setFinalPrice(String orderId, BigDecimal finalPrice);
    
    /**
     * 更新检测结果
     */
    boolean updateInspectionResult(String orderId, String result);
    
    /**
     * 取消订单
     */
    boolean cancelOrder(String orderId);
    
    /**
     * 完成订单
     */
    boolean completeOrder(String orderId);
    
    /**
     * 按状态查询订单
     */
    IPage<GoldRecycleOrderVO> listOrdersByStatus(Integer status, Page<GoldRecycleOrder> page, BigDecimal estimatedPrice);
    
    /**
     * 根据账户查询订单列表
     */
    IPage<GoldRecycleOrderVO> listOrdersByAccount(String account, Page<GoldRecycleOrder> page, BigDecimal estimatedPrice);
    
    /**
     * 更新订单快递信息
     * @param orderId 订单ID
     * @param expressCompany 快递公司
     * @param trackingNumber 快递单号
     * @return 是否更新成功
     */
    boolean updateExpressInfo(String orderId, String expressCompany, String trackingNumber);
} 