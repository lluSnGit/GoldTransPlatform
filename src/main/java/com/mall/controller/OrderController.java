package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.Order;
import com.mall.service.OrderService;
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
import com.mall.dto.GoldRecycleOrderDTO;
import com.mall.vo.OrderDetailVO;
import com.mall.vo.OrderVO;
import com.mall.common.PageResult;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "订单管理", description = "订单相关接口")
@Slf4j
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @PostMapping
    @Operation(summary = "创建订单")
    public Result<Boolean> createOrder(
            @Parameter(description = "订单编号") @RequestParam String orderCode,
            @Parameter(description = "用户ID") @RequestParam String userId,
            @Parameter(description = "商品编号") @RequestParam String productCode,
            @Parameter(description = "交货方式(1:到店,2:邮寄)") @RequestParam Integer deliveryType,
            @Parameter(description = "预估价格") @RequestParam BigDecimal estimatedPrice,
            @Parameter(description = "最终价格") @RequestParam(required = false) BigDecimal finalPrice,
            @Parameter(description = "订单状态") @RequestParam(defaultValue = "1") Integer status) {
        Order order = new Order();
        order.setOrderCode(orderCode);
        order.setUserId(userId);
        order.setProductCode(productCode);
        order.setDeliveryType(deliveryType);
        order.setEstimatedPrice(estimatedPrice);
        order.setFinalPrice(finalPrice);
        order.setStatus(status);
        return Result.success(orderService.createOrder(order));
    }
    
    @GetMapping("/{orderCode}")
    @Operation(summary = "获取订单信息")
    public Result<Order> getOrder(@Parameter(description = "订单编号") @PathVariable String orderCode) {
        return Result.success(orderService.getByOrderCode(orderCode));
    }
    
    @GetMapping("/list")
    @Operation(summary = "分页查询订单列表")
    public Result<IPage<Order>> listOrders(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "用户ID") @RequestParam(required = false) String userId,
            @Parameter(description = "商品编号") @RequestParam(required = false) String productCode,
            @Parameter(description = "订单状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "最低金额") @RequestParam(required = false) BigDecimal minAmount,
            @Parameter(description = "最高金额") @RequestParam(required = false) BigDecimal maxAmount) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        return Result.success(orderService.listOrders(page, userId, productCode, status, minAmount, maxAmount));
    }
    
    @PutMapping("/{orderCode}")
    @Operation(summary = "更新订单信息")
    public Result<Boolean> updateOrder(
            @Parameter(description = "订单编号") @PathVariable String orderCode,
            @Parameter(description = "预估价格") @RequestParam(required = false) BigDecimal estimatedPrice,
            @Parameter(description = "订单状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "备注") @RequestParam(required = false) String remark) {
        Order order = orderService.getByOrderCode(orderCode);
        if (order == null) {
            return Result.error("订单不存在");
        }
        if (estimatedPrice != null) order.setEstimatedPrice(estimatedPrice);
        if (status != null) order.setStatus(status);
        if (remark != null) order.setRemark(remark);
        return Result.success(orderService.updateById(order));
    }
    
    @PutMapping("/{orderCode}/status")
    @Operation(summary = "更新订单状态")
    public Result<Boolean> updateOrderStatus(
            @Parameter(description = "订单编号") @PathVariable String orderCode,
            @Parameter(description = "订单状态") @RequestParam Integer status) {
        return Result.success(orderService.updateOrderStatus(orderCode, status));
    }
    
    @DeleteMapping("/{orderCode}")
    @Operation(summary = "删除订单")
    public Result<Boolean> deleteOrder(@Parameter(description = "订单编号") @PathVariable String orderCode) {
        Order order = orderService.getByOrderCode(orderCode);
        if (order == null) {
            return Result.error("订单不存在");
        }
        return Result.success(orderService.removeById(order.getId()));
    }
    
    @PostMapping("/batch/delete")
    @Operation(summary = "批量删除订单")
    public Result<Boolean> batchDeleteOrders(@Parameter(description = "订单编号列表") @RequestParam List<String> orderCodes) {
        return Result.success(orderService.batchDeleteOrders(orderCodes));
    }
    
    @PostMapping("/batch/status")
    @Operation(summary = "批量更新订单状态")
    public Result<Boolean> batchUpdateStatus(
            @Parameter(description = "订单编号列表") @RequestParam List<String> orderCodes,
            @Parameter(description = "订单状态") @RequestParam Integer status) {
        return Result.success(orderService.batchUpdateStatus(orderCodes, status));
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户订单列表")
    public Result<List<Order>> getUserOrders(@Parameter(description = "用户ID") @PathVariable String userId) {
        return Result.success(orderService.getUserOrders(userId));
    }
    
    @GetMapping("/product/{productCode}")
    @Operation(summary = "获取商品订单列表")
    public Result<List<Order>> getProductOrders(@Parameter(description = "商品编号") @PathVariable String productCode) {
        return Result.success(orderService.getProductOrders(productCode));
    }
    
    @Hidden
    @GetMapping("/internal")
    public Result<?> internalApi() {
        return Result.success("内部接口");
    }
    
    /**
     * 创建黄金回收订单
     */
    @PostMapping("/gold-recycle")
    public Result<OrderVO> createGoldRecycleOrder(@RequestBody @Valid GoldRecycleOrderDTO dto) {
        // TODO: 实现创建订单逻辑
        return Result.success(null);
    }
    
    /**
     * 获取订单详情
     */
    @GetMapping("/{orderId}")
    public Result<OrderDetailVO> getOrderDetail(@PathVariable Long orderId) {
        // TODO: 实现获取订单详情逻辑
        return Result.success(null);
    }
    
    /**
     * 获取用户订单列表
     */
    @GetMapping("/user/list")
    public Result<PageResult<OrderVO>> getUserOrders(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer orderType,
            @RequestParam(required = false) Integer status) {
        // TODO: 实现获取用户订单列表逻辑
        return Result.success(null);
    }
    
    /**
     * 获取商家订单列表
     */
    @GetMapping("/merchant/list")
    public Result<PageResult<OrderVO>> getMerchantOrders(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer orderType,
            @RequestParam(required = false) Integer status) {
        // TODO: 实现获取商家订单列表逻辑
        return Result.success(null);
    }
} 