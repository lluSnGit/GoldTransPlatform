package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.entity.GoldRecycleOrder;
import com.mall.mapper.GoldRecycleOrderMapper;
import com.mall.service.GoldRecycleOrderService;
import com.mall.dto.GoldRecycleOrderDTO;
import com.mall.vo.GoldRecycleOrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Base64;
import org.springframework.web.multipart.MultipartFile;
import com.mall.service.MinioService;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class GoldRecycleOrderServiceImpl implements GoldRecycleOrderService {

    private static final Logger log = LoggerFactory.getLogger(GoldRecycleOrderServiceImpl.class);

    @Autowired
    private GoldRecycleOrderMapper goldRecycleOrderMapper;

    @Autowired
    private MinioService minioService;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private String generateOrderId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GoldRecycleOrderVO createOrder(GoldRecycleOrderDTO orderDTO, GoldRecycleOrder order) {
        // 设置基本信息
        order.setAccount(orderDTO.getAccount());
        order.setGoldType(orderDTO.getGoldType());
        order.setEstimatedWeight(orderDTO.getEstimatedWeight());
        order.setEstimatedPrice(orderDTO.getEstimatedPrice());
        order.setPurity(orderDTO.getPurity());
        order.setGoldCondition(orderDTO.getGoldCondition());
        order.setDescription(orderDTO.getDescription());
        order.setReceiverName(orderDTO.getReceiverName());
        order.setReceiverPhone(orderDTO.getReceiverPhone());
        order.setAddressId(orderDTO.getAddressId());
        
        // 生成订单号
        order.setOrderId(generateOrderId());
        
        // 设置初始状态
        order.setStatus(1); // 待检测
        order.setCreateTime(LocalDateTime.now());
        
        // 保存订单
        goldRecycleOrderMapper.insert(order);
        
        // 转换为VO返回
        return convertToVO(order);
    }

    @Override
    public GoldRecycleOrderVO getOrderDetail(String orderId) {
        GoldRecycleOrder order = getOrderEntity(orderId);
        return convertToVO(order);
    }

    /**
     * 获取订单实体
     */
    @Override
    public GoldRecycleOrder getOrderEntity(String orderId) {
        LambdaQueryWrapper<GoldRecycleOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GoldRecycleOrder::getOrderId, orderId);
        return goldRecycleOrderMapper.selectOne(wrapper);
    }

    @Override
    public IPage<GoldRecycleOrderVO> listAllOrders(Page<GoldRecycleOrder> page, BigDecimal estimatedPrice) {
        // 确保分页参数有效
        if (page == null) {
            page = new Page<>(1, 10);
        }
        
        // 确保页码和页大小有效
        if (page.getCurrent() <= 0) {
            page.setCurrent(1);
        }
        if (page.getSize() <= 0) {
            page.setSize(10);
        }
        
        // 设置分页参数
        page.setOptimizeCountSql(true);  // 优化 count 查询
        page.setSearchCount(true);       // 开启 count 查询
        
        LambdaQueryWrapper<GoldRecycleOrder> wrapper = new LambdaQueryWrapper<>();
        
        // 添加预估价格过滤条件
        if (estimatedPrice != null) {
            wrapper.eq(GoldRecycleOrder::getEstimatedPrice, estimatedPrice);
        }
        
        // 按创建时间倒序排列
        wrapper.orderByDesc(GoldRecycleOrder::getCreateTime);
        
        // 添加详细的日志记录
        log.info("=== 分页查询开始 ===");
        log.info("输入参数 - 页码：{}，每页数量：{}，预估价格：{}", page.getCurrent(), page.getSize(), estimatedPrice);
        log.info("Page对象详情 - current: {}, size: {}, offset: {}, searchCount: {}, optimizeCountSql: {}", 
                page.getCurrent(), page.getSize(), page.offset(), page.searchCount(), page.optimizeCountSql());
        
        // 先查询总数（用于调试）
        Long totalCount = goldRecycleOrderMapper.selectCount(wrapper);
        log.info("数据库总记录数：{}", totalCount);
        
        // 执行分页查询
        IPage<GoldRecycleOrder> orderPage = goldRecycleOrderMapper.selectPage(page, wrapper);
        
        // 记录详细的查询结果
        log.info("分页查询结果详情：");
        log.info("- 总记录数：{}", orderPage.getTotal());
        log.info("- 当前页记录数：{}", orderPage.getRecords().size());
        log.info("- 总页数：{}", orderPage.getPages());
        log.info("- 当前页码：{}", orderPage.getCurrent());
        log.info("- 每页大小：{}", orderPage.getSize());
        log.info("- 是否有下一页：{}", orderPage.getCurrent() < orderPage.getPages());
        log.info("- 是否有上一页：{}", orderPage.getCurrent() > 1);
        
        // 输出实际返回的记录ID（用于调试）
        if (!orderPage.getRecords().isEmpty()) {
            log.info("返回的订单ID列表：{}", 
                    orderPage.getRecords().stream()
                            .map(GoldRecycleOrder::getOrderId)
                            .collect(Collectors.toList()));
        }
        log.info("=== 分页查询结束 ===");
        
        // 验证分页是否生效
        if (orderPage.getRecords().size() > page.getSize()) {
            log.error("分页未生效！返回记录数({})超过页大小({})", orderPage.getRecords().size(), page.getSize());
        }
        
        // 转换为VO并返回
        return orderPage.convert(this::convertToVO);
    }

    @Override
    public boolean updateOrderStatus(String orderId, Integer status) {
        GoldRecycleOrder order = getOrderEntity(orderId);
        if (order == null) {
            return false;
        }
        order.setStatus(status);
        order.setUpdateTime(LocalDateTime.now());
        boolean result = goldRecycleOrderMapper.updateById(order) > 0;
        // 新增：如果状态为4，启动15分钟（TimeUnit.MINUTES, 15）后自动转为5的定时任务
        if (result && status == 4) {
            scheduler.schedule(() -> {
                GoldRecycleOrder latestOrder = getOrderEntity(orderId);
                if (latestOrder != null && latestOrder.getStatus() == 4) {
                    latestOrder.setStatus(5);
                    latestOrder.setUpdateTime(LocalDateTime.now());
                    goldRecycleOrderMapper.updateById(latestOrder);
                }
            }, 15, TimeUnit.MINUTES);
        }
        return result;
    }

    @Override
    public boolean setEstimatedPrice(String orderId, BigDecimal estimatedPrice) {
        GoldRecycleOrder order = getOrderEntity(orderId);
        if (order == null) {
            return false;
        }
        
        order.setEstimatedPrice(estimatedPrice);
        order.setUpdateTime(LocalDateTime.now());
        return goldRecycleOrderMapper.updateById(order) > 0;
    }

    @Override
    public boolean setFinalPrice(String orderId, BigDecimal finalPrice) {
        GoldRecycleOrder order = getOrderEntity(orderId);
        if (order == null) {
            return false;
        }
        
        order.setFinalPrice(finalPrice);
        order.setUpdateTime(LocalDateTime.now());
        return goldRecycleOrderMapper.updateById(order) > 0;
    }

    @Override
    public boolean updateInspectionResult(String orderId, String result) {
        GoldRecycleOrder order = getOrderEntity(orderId);
        if (order == null) {
            return false;
        }
        
        order.setInspectionResult(result);
        order.setUpdateTime(LocalDateTime.now());
        return goldRecycleOrderMapper.updateById(order) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(String orderId) {
        GoldRecycleOrder order = getOrderEntity(orderId);
        if (order == null) {
            return false;
        }
        
        // 检查订单状态是否允许取消
        if (order.getStatus() >= 4) { // 4-已完成 5-已取消
            return false;
        }
        
        order.setStatus(5); // 5: 已取消
        order.setUpdateTime(LocalDateTime.now());
        return goldRecycleOrderMapper.updateById(order) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean completeOrder(String orderId) {
        GoldRecycleOrder order = getOrderEntity(orderId);
        if (order == null) {
            return false;
        }
        
        // 检查订单状态是否允许完成
        // 只有待检测(1)、检测中(2)、待确认(3)状态的订单才能完成
        if (order.getStatus() < 1 || order.getStatus() > 3) {
            return false;
        }
        
        order.setStatus(4); // 4: 已完成
        order.setUpdateTime(LocalDateTime.now());
        return goldRecycleOrderMapper.updateById(order) > 0;
    }

    @Override
    public IPage<GoldRecycleOrderVO> listOrdersByStatus(Integer status, Page<GoldRecycleOrder> page, BigDecimal estimatedPrice) {
        // 确保分页参数有效
        if (page == null) {
            page = new Page<>(1, 10);
        }
        
        // 确保页码和页大小有效
        if (page.getCurrent() <= 0) {
            page.setCurrent(1);
        }
        if (page.getSize() <= 0) {
            page.setSize(10);
        }
        
        // 设置分页参数
        page.setOptimizeCountSql(true);
        page.setSearchCount(true);
        
        LambdaQueryWrapper<GoldRecycleOrder> wrapper = new LambdaQueryWrapper<>();
        
        // 添加状态过滤条件
        wrapper.eq(GoldRecycleOrder::getStatus, status);
        
        // 添加预估价格过滤条件
        if (estimatedPrice != null) {
            wrapper.eq(GoldRecycleOrder::getEstimatedPrice, estimatedPrice);
        }
        
        // 按创建时间倒序排列
        wrapper.orderByDesc(GoldRecycleOrder::getCreateTime);
        
        // 执行分页查询
        IPage<GoldRecycleOrder> orderPage = goldRecycleOrderMapper.selectPage(page, wrapper);
        
        log.info("按状态查询订单，状态：{}，页码：{}，每页数量：{}，总记录数：{}，当前页记录数：{}", 
                status, page.getCurrent(), page.getSize(), orderPage.getTotal(), orderPage.getRecords().size());
        
        // 验证分页是否生效
        if (orderPage.getRecords().size() > page.getSize()) {
            log.error("分页未生效！返回记录数({})超过页大小({})", orderPage.getRecords().size(), page.getSize());
        }
        
        return orderPage.convert(this::convertToVO);
    }

    @Override
    public IPage<GoldRecycleOrderVO> listOrdersByAccount(String account, Page<GoldRecycleOrder> page, BigDecimal estimatedPrice) {
        // 确保分页参数有效
        if (page == null) {
            page = new Page<>(1, 10);
        }
        
        // 确保页码和页大小有效
        if (page.getCurrent() <= 0) {
            page.setCurrent(1);
        }
        if (page.getSize() <= 0) {
            page.setSize(10);
        }
        
        // 设置分页参数
        page.setOptimizeCountSql(true);
        page.setSearchCount(true);
        
        LambdaQueryWrapper<GoldRecycleOrder> wrapper = new LambdaQueryWrapper<>();
        
        // 添加账户过滤条件
        wrapper.eq(GoldRecycleOrder::getAccount, account);
        
        // 添加预估价格过滤条件
        if (estimatedPrice != null) {
            wrapper.eq(GoldRecycleOrder::getEstimatedPrice, estimatedPrice);
        }
        
        // 按创建时间倒序排列
        wrapper.orderByDesc(GoldRecycleOrder::getCreateTime);
        
        // 执行分页查询
        IPage<GoldRecycleOrder> orderPage = goldRecycleOrderMapper.selectPage(page, wrapper);
        
        log.info("按账户查询订单，账户：{}，页码：{}，每页数量：{}，总记录数：{}，当前页记录数：{}", 
                account, page.getCurrent(), page.getSize(), orderPage.getTotal(), orderPage.getRecords().size());
        
        // 验证分页是否生效
        if (orderPage.getRecords().size() > page.getSize()) {
            log.error("分页未生效！返回记录数({})超过页大小({})", orderPage.getRecords().size(), page.getSize());
        }
        
        return orderPage.convert(this::convertToVO);
    }

    @Override
    public boolean updateExpressInfo(String orderId, String expressCompany, String trackingNumber) {
        GoldRecycleOrder order = getOrderEntity(orderId);
        if (order == null) {
            return false;
        }
        
        order.setExpressCompany(expressCompany);
        order.setTrackingNumber(trackingNumber);
        order.setUpdateTime(LocalDateTime.now());
        
        return goldRecycleOrderMapper.updateById(order) > 0;
    }

    /**
     * 将实体类转换为VO
     */
    private GoldRecycleOrderVO convertToVO(GoldRecycleOrder order) {
        if (order == null) {
            return null;
        }
        GoldRecycleOrderVO vo = new GoldRecycleOrderVO();
        BeanUtils.copyProperties(order, vo);
        
        // 设置图片URL列表
        if (order.getImages() != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                List<String> imageList = objectMapper.readValue(order.getImages(), List.class);
                vo.setImageList(imageList);
                vo.setImageBase64(order.getImages()); // 保持向后兼容
            } catch (JsonProcessingException e) {
                log.error("解析图片JSON失败", e);
                // 如果解析失败，尝试使用逗号分隔的方式
                vo.setImageList(Arrays.asList(order.getImages().split(",")));
            vo.setImageBase64(order.getImages());
            }
        }
        
        return vo;
    }
} 