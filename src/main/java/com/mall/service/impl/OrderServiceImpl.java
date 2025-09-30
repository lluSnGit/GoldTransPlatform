package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.entity.Order;
import com.mall.entity.PlatformFee;
import com.mall.entity.UserLevelRebate;
import com.mall.mapper.OrderMapper;
import com.mall.service.OrderService;
import com.mall.service.PlatformFeeService;
import com.mall.service.UserLevelRebateService;
import com.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private UserService userService;
    
    @Autowired
    private PlatformFeeService platformFeeService;
    
    @Autowired
    private UserLevelRebateService userLevelRebateService;

    @Override
    @Transactional
    public boolean createOrder(Order order) {
        return save(order);
    }

    @Override
    @Transactional
    public Order auditOrder(Long id, boolean approved) {
        Order order = getById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        if (approved) {
            order.setStatus(2); // 已审核
        } else {
            order.setStatus(0); // 审核拒绝
        }
        
        updateById(order);
        return order;
    }

    @Override
    @Transactional
    public Order confirmPickup(Long id) {
        Order order = getById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        order.setStatus(3); // 待取件
        updateById(order);
        return order;
    }

    @Override
    @Transactional
    public Order confirmReceive(Long id) {
        Order order = getById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        order.setStatus(4); // 已收货
        updateById(order);
        return order;
    }

    @Override
    @Transactional
    public Order inspectProduct(Long id, String inspectionResult) {
        Order order = getById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        order.setStatus(6); // 已检测
        updateById(order);
        return order;
    }

    @Override
    @Transactional
    public Order setPrice(Long id, Double finalPrice) {
        Order order = getById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 设置最终价格
        order.setFinalPrice(BigDecimal.valueOf(finalPrice));
        
        // 计算平台手续费
        // 获取用户等级
        Integer userLevel = userService.getUserLevelByUserId(order.getUserId());
        if (userLevel != null) {
            PlatformFee platformFee = platformFeeService.getByUserLevel(userLevel);
            if (platformFee != null) {
                BigDecimal fee = calculatePlatformFee(finalPrice, platformFee);
                order.setPlatformFee(fee);
            }
        }
        
        // 计算用户返点
        UserLevelRebate rebate = userLevelRebateService.getByUserLevel(order.getUserId());
        if (rebate != null) {
            BigDecimal rebateAmount = calculateUserRebate(finalPrice, rebate);
            order.setUserRebate(rebateAmount);
        }
        
        order.setStatus(7); // 已定价
        updateById(order);
        return order;
    }

    @Override
    @Transactional
    public Order completeOrder(Long id) {
        Order order = getById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        order.setStatus(8); // 已完成
        updateById(order);
        return order;
    }

    @Override
    public IPage<Order> listOrders(Page<Order> page, String userId, String productCode, Integer status, BigDecimal minAmount, BigDecimal maxAmount) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        if (userId != null && !userId.isEmpty()) {
            wrapper.eq(Order::getUserId, userId);
        }
        if (StringUtils.hasText(productCode)) {
            wrapper.eq(Order::getProductCode, productCode);
        }
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        if (minAmount != null) {
            wrapper.ge(Order::getEstimatedPrice, minAmount);
        }
        if (maxAmount != null) {
            wrapper.le(Order::getEstimatedPrice, maxAmount);
        }
        wrapper.orderByDesc(Order::getCreateTime);
        return page(page, wrapper);
    }

    /**
     * 计算平台手续费
     */
    private BigDecimal calculatePlatformFee(Double amount, PlatformFee platformFee) {
        BigDecimal price = BigDecimal.valueOf(amount);
        // 直接使用手续费率计算
        return price.multiply(platformFee.getFeeRate())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    /**
     * 计算用户返点
     */
    private BigDecimal calculateUserRebate(Double amount, UserLevelRebate rebate) {
        return BigDecimal.valueOf(amount)
                .multiply(rebate.getRebateRate())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean setFinalPrice(Long orderId, BigDecimal finalPrice) {
        Order order = getById(orderId);
        if (order != null) {
            order.setFinalPrice(finalPrice);
            return updateById(order);
        }
        return false;
    }

    @Override
    public BigDecimal calculateUserRebate(Long orderId) {
        Order order = getById(orderId);
        if (order == null) {
            return BigDecimal.ZERO;
        }
        UserLevelRebate rebate = userLevelRebateService.getByUserLevel(order.getUserId());
        return rebate != null ? rebate.getRebateRate() : BigDecimal.ZERO;
    }

    @Override
    @Transactional
    public boolean updateOrderStatus(String orderCode, Integer status) {
        Order order = getByOrderCode(orderCode);
        if (order != null) {
            order.setStatus(status);
            return updateById(order);
        }
        return false;
    }

    @Override
    public Order getByOrderCode(String orderCode) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderCode, orderCode);
        return getOne(wrapper);
    }

    @Override
    @Transactional
    public boolean batchDeleteOrders(List<String> orderCodes) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Order::getOrderCode, orderCodes);
        return remove(wrapper);
    }

    @Override
    @Transactional
    public boolean batchUpdateStatus(List<String> orderCodes, Integer status) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Order::getOrderCode, orderCodes);
        Order order = new Order();
        order.setStatus(status);
        return update(order, wrapper);
    }

    @Override
    public List<Order> getUserOrders(String userId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId);
        wrapper.orderByDesc(Order::getCreateTime);
        return list(wrapper);
    }

    @Override
    public List<Order> getProductOrders(String productCode) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getProductCode, productCode);
        wrapper.orderByDesc(Order::getCreateTime);
        return list(wrapper);
    }
} 