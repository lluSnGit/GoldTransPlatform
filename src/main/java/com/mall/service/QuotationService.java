package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.entity.Quotation;
import com.mall.dto.QuotationDTO;
import java.util.List;
import java.math.BigDecimal;

public interface QuotationService extends IService<Quotation> {
    
    // 创建报价
    QuotationDTO createQuotation(QuotationDTO quotationDTO);
    
    // 更新报价
    QuotationDTO updateQuotation(QuotationDTO quotationDTO);
    
    // 获取报价详情
    QuotationDTO getQuotationById(String id);
    
    /**
     * 根据订单ID获取报价
     * @param orderId 订单ID
     * @return 报价列表
     */
    List<QuotationDTO> getQuotationByOrderId(String orderId);
    
    // 获取所有报价列表
    List<QuotationDTO> getAllQuotations();
    
    // 删除报价
    void deleteQuotation(String id);
    
    // 批量创建报价
    List<QuotationDTO> batchCreateQuotations(List<QuotationDTO> quotationDTOs);
    
    // 根据黄金类型查询报价
    List<QuotationDTO> getQuotationsByGoldType(String goldType);
    
    // 计算总价（包含所有费用）
    BigDecimal calculateTotalPrice(String quotationId);
} 