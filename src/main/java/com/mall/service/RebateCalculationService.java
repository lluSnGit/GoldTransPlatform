package com.mall.service;

import com.mall.dto.RebateCalculationDTO;
import com.mall.vo.RebateCalculationVO;

public interface RebateCalculationService {
    
    /**
     * 计算并分配返点
     * @param dto 计算参数
     * @return 计算结果
     */
    RebateCalculationVO calculateAndDistributeRebate(RebateCalculationDTO dto);
    
    /**
     * 仅计算返点（不分配）
     * @param dto 计算参数
     * @return 计算结果
     */
    RebateCalculationVO calculateRebate(RebateCalculationDTO dto);
} 