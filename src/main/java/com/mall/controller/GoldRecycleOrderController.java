package com.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.common.Result;
import com.mall.dto.GoldRecycleOrderDTO;
import com.mall.entity.GoldRecycleOrder;
import com.mall.service.GoldRecycleOrderService;
import com.mall.vo.GoldRecycleOrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.math.BigDecimal;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.mall.service.UserAddressService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.HashMap;
import com.mall.service.MinioService;

@RestController
@RequestMapping("/api/gold-recycle/orders")
@Tag(name = "黄金回收订单接口")
@Slf4j
public class GoldRecycleOrderController {

    @Autowired
    private GoldRecycleOrderService goldRecycleOrderService;

    @Autowired
    private UserAddressService userAddressService;

    @Autowired
    private MinioService minioService;

    @PostMapping
    @Operation(summary = "创建回收订单")
    public Result<GoldRecycleOrderVO> createOrder(
            @Parameter(description = "账号") @RequestParam(required = false) String account,
            @Parameter(description = "黄金类型") @RequestParam(required = false) String goldType,
            @Parameter(description = "预估重量(克)") @RequestParam(required = false) BigDecimal estimatedWeight,
            @Parameter(description = "预估价格") @RequestParam(required = false) BigDecimal estimatedPrice,
            @Parameter(description = "成色") @RequestParam(required = false) String purity,
            @Parameter(description = "品相") @RequestParam(required = false) String goldCondition,
            @Parameter(description = "描述") @RequestParam(required = false) String description,
            @Parameter(description = "收货人姓名") @RequestParam(required = false) String receiverName,
            @Parameter(description = "收货人电话") @RequestParam(required = false) String receiverPhone,
            @Parameter(description = "收货地址ID") @RequestParam(required = false) String addressId,
            @Parameter(description = "图片URL数组") @RequestParam(required = false) String images,
            @RequestBody(required = false) GoldRecycleOrderDTO orderDTO) {
        
        // 如果使用JSON格式，从orderDTO获取数据
        if (orderDTO != null) {
            account = orderDTO.getAccount();
            goldType = orderDTO.getGoldType();
            estimatedWeight = orderDTO.getEstimatedWeight();
            estimatedPrice = orderDTO.getEstimatedPrice();
            purity = orderDTO.getPurity();
            goldCondition = orderDTO.getGoldCondition();
            description = orderDTO.getDescription();
            receiverName = orderDTO.getReceiverName();
            receiverPhone = orderDTO.getReceiverPhone();
            addressId = orderDTO.getAddressId();
            if (orderDTO.getImages() != null) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    images = objectMapper.writeValueAsString(orderDTO.getImages());
                } catch (JsonProcessingException e) {
                    log.error("处理图片JSON失败", e);
                    return Result.error("图片格式不正确");
                }
            }
        } else if (images != null && !images.isEmpty()) {
            try {
                // URL解码
                String decodedImages = java.net.URLDecoder.decode(images, "UTF-8");
                // 验证JSON格式
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.readValue(decodedImages, List.class);
                images = decodedImages;
            } catch (Exception e) {
                log.error("处理图片JSON失败", e);
                return Result.error("图片格式不正确");
            }
        }
        
        GoldRecycleOrderDTO dto = new GoldRecycleOrderDTO();
        dto.setAccount(account);
        dto.setGoldType(goldType);
        dto.setEstimatedWeight(estimatedWeight);
        dto.setEstimatedPrice(estimatedPrice);
        dto.setPurity(purity);
        dto.setGoldCondition(goldCondition);
        dto.setDescription(description);
        dto.setReceiverName(receiverName);
        dto.setReceiverPhone(receiverPhone);
        dto.setAddressId(addressId);
        
        // 创建订单实体并设置图片URL
        GoldRecycleOrder order = new GoldRecycleOrder();
        if (images != null && !images.isEmpty()) {
            order.setImages(images);
        }
        
        GoldRecycleOrderVO result = goldRecycleOrderService.createOrder(dto, order);
        return Result.success(result);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "获取订单详情")
    public Result<GoldRecycleOrderVO> getOrderDetail(
            @Parameter(description = "订单ID") @PathVariable String orderId) {
        GoldRecycleOrderVO order = goldRecycleOrderService.getOrderDetail(orderId);
        if (order != null && order.getAddressId() != null) {
            // 查询地址信息
            String addressInfo = userAddressService.getAddressInfo(order.getAddressId());
            order.setAddressInfo(addressInfo);
        }
        return Result.success(order);
    }

    @GetMapping("/list")
    @Operation(summary = "分页查询所有订单")
    public Result<IPage<GoldRecycleOrderVO>> listAllOrders(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "预估价格") @RequestParam(required = false) BigDecimal estimatedPrice) {
        
        // 参数验证
        if (pageNum == null || pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10;
        }
        
        // 限制页大小，防止一次查询过多数据
        if (pageSize > 100) {
            pageSize = 100;
        }
        
        log.info("控制器接收到的分页参数 - 页码: {}, 每页数量: {}, 预估价格: {}", pageNum, pageSize, estimatedPrice);
        
        // 创建分页对象
        Page<GoldRecycleOrder> page = new Page<>(pageNum, pageSize);
        
        // 设置分页参数
        page.setOptimizeCountSql(true);
        page.setSearchCount(true);
        
        log.info("创建的Page对象 - current: {}, size: {}", page.getCurrent(), page.getSize());
        
        // 调用服务层
        IPage<GoldRecycleOrderVO> result = goldRecycleOrderService.listAllOrders(page, estimatedPrice);
        
        log.info("服务层返回结果 - 总记录数: {}, 当前页记录数: {}, 总页数: {}", 
                result.getTotal(), result.getRecords().size(), result.getPages());
        
        return Result.success(result);
    }

    @PutMapping("/{orderId}/status")
    @Operation(summary = "更新订单状态")
    public Result<Boolean> updateOrderStatus(
            @Parameter(description = "订单ID") @PathVariable String orderId,
            @Parameter(description = "订单状态") @RequestParam Integer status) {
        // 将整数状态映射为文字描述状态
        String statusDescription;
        switch (status) {
            case 0:
                statusDescription = "已取消";
                break;
            case 1:
                statusDescription = "已下单";
                break;
            case 2:
                statusDescription = "已取件";
                break;
            case 3:
                statusDescription = "待检测";
                break;
            case 4:
                statusDescription = "已检测";
                break;
            case 5:
                statusDescription = "已确认";
                break;
            case 6:
                statusDescription = "订单已完成";
                break;
            default:
                statusDescription = "未知状态";
        }
        boolean success = goldRecycleOrderService.updateOrderStatus(orderId, status);
        return Result.success(success);
    }

    @PutMapping("/{orderId}/estimated-price")
    @Operation(summary = "设置预估价格")
    public Result<Boolean> setEstimatedPrice(
            @Parameter(description = "订单ID") @PathVariable String orderId,
            @Parameter(description = "预估价格") @RequestParam BigDecimal estimatedPrice) {
        boolean success = goldRecycleOrderService.setEstimatedPrice(orderId, estimatedPrice);
        return Result.success(success);
    }

    @PutMapping("/{orderId}/final-price")
    @Operation(summary = "设置最终价格")
    public Result<Boolean> setFinalPrice(
            @Parameter(description = "订单ID") @PathVariable String orderId,
            @Parameter(description = "最终价格") @RequestParam BigDecimal finalPrice) {
        boolean success = goldRecycleOrderService.setFinalPrice(orderId, finalPrice);
        return Result.success(success);
    }

    @PutMapping("/{orderId}/inspection-result")
    @Operation(summary = "更新检测结果")
    public Result<Boolean> updateInspectionResult(
            @Parameter(description = "订单ID") @PathVariable String orderId,
            @Parameter(description = "检测结果") @RequestParam String result) {
        boolean success = goldRecycleOrderService.updateInspectionResult(orderId, result);
        return Result.success(success);
    }

    @PutMapping("/{orderId}/cancel")
    @Operation(summary = "取消订单")
    public Result<GoldRecycleOrderVO> cancelOrder(
            @Parameter(description = "订单ID") @PathVariable String orderId) {
        // 更新订单状态为已取消
        boolean success = goldRecycleOrderService.updateOrderStatus(orderId, 0);
        if (success) {
            // 获取更新后的订单详情
            GoldRecycleOrderVO order = goldRecycleOrderService.getOrderDetail(orderId);
            if (order != null && order.getAddressId() != null) {
                // 查询地址信息
                String addressInfo = userAddressService.getAddressInfo(order.getAddressId());
                order.setAddressInfo(addressInfo);
            }
            return Result.success(order);
        }
        return Result.error("取消订单失败");
    }

    @PutMapping("/{orderId}/complete")
    @Operation(summary = "完成订单")
    public Result<Boolean> completeOrder(
            @Parameter(description = "订单ID") @PathVariable String orderId) {
        boolean success = goldRecycleOrderService.completeOrder(orderId);
        return Result.success(success);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "按状态查询订单")
    public Result<IPage<GoldRecycleOrderVO>> listOrdersByStatus(
            @Parameter(description = "订单状态") @PathVariable Integer status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "预估价格") @RequestParam(required = false) BigDecimal estimatedPrice) {
        Page<GoldRecycleOrder> page = new Page<>(pageNum, pageSize);
        IPage<GoldRecycleOrderVO> result = goldRecycleOrderService.listOrdersByStatus(status, page, estimatedPrice);
        return Result.success(result);
    }

    @GetMapping("/account/{account}")
    @Operation(summary = "根据账户查询订单列表")
    public Result<IPage<GoldRecycleOrderVO>> listOrdersByAccount(
            @Parameter(description = "账户") @PathVariable String account,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "预估价格") @RequestParam(required = false) BigDecimal estimatedPrice) {
        Page<GoldRecycleOrder> page = new Page<>(pageNum, pageSize);
        IPage<GoldRecycleOrderVO> result = goldRecycleOrderService.listOrdersByAccount(account, page, estimatedPrice);
        return Result.success(result);
    }

    @GetMapping("/{orderId}/image")
    @Operation(summary = "获取订单图片")
    public void getOrderImage(
            @Parameter(description = "订单ID") @PathVariable String orderId,
            HttpServletResponse response) throws IOException {
        GoldRecycleOrder order = goldRecycleOrderService.getOrderEntity(orderId);
        if (order != null && order.getImages() != null) {
            // 使用 HTTPS 域名
            String imageUrl = "https://www.iejhsgold.cn/mall/" + order.getImages();
            response.sendRedirect(imageUrl);
        }
    }

    @PostMapping("/api/minio/upload")
    @Operation(summary = "上传图片到Minio")
    public Result<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String objectPath = minioService.uploadFile(file, "mall");
            String fileUrl = "https://www.iejhsgold.cn/mall/" + objectPath;

            Map<String, String> resultData = new HashMap<>();
            resultData.put("fileName", file.getOriginalFilename());
            resultData.put("url", fileUrl);

            return Result.success(resultData);
        } catch (Exception e) {
            log.error("上传图片失败", e);
            return Result.error("上传图片失败: " + e.getMessage());
        }
    }

    @PutMapping("/{orderId}/pickup")
    @Operation(summary = "更新订单快递信息")
    public Result<GoldRecycleOrderVO> updateExpressInfo(
            @Parameter(description = "订单ID") @PathVariable String orderId,
            @Parameter(description = "快递公司") @RequestParam String expressCompany,
            @Parameter(description = "快递单号") @RequestParam String trackingNumber) {
        boolean success = goldRecycleOrderService.updateExpressInfo(orderId, expressCompany, trackingNumber);
        if (success) {
            // 获取更新后的订单详情
            GoldRecycleOrderVO order = goldRecycleOrderService.getOrderDetail(orderId);
            if (order != null && order.getAddressId() != null) {
                // 查询地址信息
                String addressInfo = userAddressService.getAddressInfo(order.getAddressId());
                order.setAddressInfo(addressInfo);
        }
            return Result.success(order);
        }
        return Result.error("更新快递信息失败");
    }

    @GetMapping("/{orderId}/pickup")
    @Operation(summary = "获取订单快递信息")
    public Result<Map<String, String>> getExpressInfo(
            @Parameter(description = "订单ID") @PathVariable String orderId) {
        GoldRecycleOrder order = goldRecycleOrderService.getOrderEntity(orderId);
        if (order != null) {
            Map<String, String> expressInfo = new HashMap<>();
            expressInfo.put("expressCompany", order.getExpressCompany());
            expressInfo.put("trackingNumber", order.getTrackingNumber());
            return Result.success(expressInfo);
        }
        return Result.error("订单不存在");
    }
} 