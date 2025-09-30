package com.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.entity.Quotation;
import com.mall.entity.QuotationItem;
import com.mall.dto.QuotationDTO;
import com.mall.dto.QuotationItemDTO;
import com.mall.mapper.QuotationMapper;
import com.mall.mapper.QuotationItemMapper;
import com.mall.service.QuotationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class QuotationServiceImpl extends ServiceImpl<QuotationMapper, Quotation> implements QuotationService {

    private final QuotationItemMapper quotationItemMapper;

    public QuotationServiceImpl(QuotationItemMapper quotationItemMapper) {
        this.quotationItemMapper = quotationItemMapper;
    }

    @Override
    @Transactional
    public QuotationDTO createQuotation(QuotationDTO quotationDTO) {
        // 1. 创建报价主表记录
        Quotation quotation = new Quotation();
        BeanUtils.copyProperties(quotationDTO, quotation);
        quotation.setId(UUID.randomUUID().toString());
        quotation.setCreateTime(LocalDateTime.now());
        quotation.setUpdateTime(LocalDateTime.now());
        save(quotation);

        // 2. 创建报价项记录
        List<QuotationItem> items = quotationDTO.getItems().stream().map(itemDTO -> {
            QuotationItem item = new QuotationItem();
            BeanUtils.copyProperties(itemDTO, item);
            item.setId(UUID.randomUUID().toString());
            item.setQuotationId(quotation.getId());
            item.setCreateTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            return item;
        }).collect(Collectors.toList());
        
        // 3. 批量保存报价项
        items.forEach(quotationItemMapper::insert);

        // 4. 设置返回结果
        BeanUtils.copyProperties(quotation, quotationDTO);
        List<QuotationItemDTO> itemDTOs = items.stream().map(item -> {
            QuotationItemDTO itemDTO = new QuotationItemDTO();
            BeanUtils.copyProperties(item, itemDTO);
            return itemDTO;
        }).collect(Collectors.toList());
        quotationDTO.setItems(itemDTOs);

        return quotationDTO;
    }

    @Override
    @Transactional
    public QuotationDTO updateQuotation(QuotationDTO quotationDTO) {
        Quotation quotation = getById(quotationDTO.getId());
        if (quotation != null) {
            BeanUtils.copyProperties(quotationDTO, quotation);
            quotation.setUpdateTime(LocalDateTime.now());
            updateById(quotation);
            BeanUtils.copyProperties(quotation, quotationDTO);
        }
        return quotationDTO;
    }

    @Override
    public QuotationDTO getQuotationById(String id) {
        Quotation quotation = getById(id);
        QuotationDTO quotationDTO = new QuotationDTO();
        if (quotation != null) {
            BeanUtils.copyProperties(quotation, quotationDTO);
        }
        return quotationDTO;
    }

    @Override
    public List<QuotationDTO> getQuotationByOrderId(String orderId) {
        // 获取所有报价记录
        List<Quotation> quotations = lambdaQuery()
            .eq(Quotation::getOrderId, orderId)
            .orderByDesc(Quotation::getCreateTime)
            .list();
            
        if (quotations.isEmpty()) {
            return Collections.emptyList();
        }

        // 转换为DTO并获取关联的报价项
        return quotations.stream().map(quotation -> {
            QuotationDTO quotationDTO = new QuotationDTO();
            BeanUtils.copyProperties(quotation, quotationDTO);
            
            // 获取关联的报价项
            List<QuotationItem> items = quotationItemMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<QuotationItem>()
                    .eq(QuotationItem::getQuotationId, quotation.getId())
            );
            
            List<QuotationItemDTO> itemDTOs = items.stream().map(item -> {
                QuotationItemDTO itemDTO = new QuotationItemDTO();
                BeanUtils.copyProperties(item, itemDTO);
                return itemDTO;
            }).collect(Collectors.toList());
            
            quotationDTO.setItems(itemDTOs);
            return quotationDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<QuotationDTO> getAllQuotations() {
        List<Quotation> quotations = list();
        return quotations.stream().map(quotation -> {
            QuotationDTO dto = new QuotationDTO();
            BeanUtils.copyProperties(quotation, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteQuotation(String id) {
        removeById(id);
    }

    @Override
    @Transactional
    public List<QuotationDTO> batchCreateQuotations(List<QuotationDTO> quotationDTOs) {
        List<Quotation> quotations = quotationDTOs.stream().map(dto -> {
            Quotation quotation = new Quotation();
            BeanUtils.copyProperties(dto, quotation);
            quotation.setCreateTime(LocalDateTime.now());
            quotation.setUpdateTime(LocalDateTime.now());
            return quotation;
        }).collect(Collectors.toList());
        
        saveBatch(quotations);
        
        return quotations.stream().map(quotation -> {
            QuotationDTO dto = new QuotationDTO();
            BeanUtils.copyProperties(quotation, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<QuotationDTO> getQuotationsByGoldType(String goldType) {
        List<QuotationItem> items = quotationItemMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<QuotationItem>()
                .eq(QuotationItem::getGoldType, goldType)
        );
        
        return items.stream().map(item -> {
            Quotation quotation = getById(item.getQuotationId());
            QuotationDTO dto = new QuotationDTO();
            if (quotation != null) {
                BeanUtils.copyProperties(quotation, dto);

                QuotationItemDTO itemDTO = new QuotationItemDTO();
                BeanUtils.copyProperties(item, itemDTO);
                dto.setItems(Collections.singletonList(itemDTO));
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public BigDecimal calculateTotalPrice(String quotationId) {
        Quotation quotation = getById(quotationId);
        if (quotation == null) {
            return BigDecimal.ZERO;
        }
        
        List<QuotationItem> items = quotationItemMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<QuotationItem>()
                .eq(QuotationItem::getQuotationId, quotationId)
        );
        
        BigDecimal itemsTotal = items.stream()
            .map(QuotationItem::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        return itemsTotal
            .add(quotation.getShippingFee())
            .add(quotation.getInsuranceFee())
            .add(quotation.getServiceFee());
    }
} 