package com.mall.service.impl;

import com.mall.dto.RebateCalculationDTO;
import com.mall.entity.PlatformFee;
import com.mall.entity.User;
import com.mall.service.*;
import com.mall.vo.RebateCalculationVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
public class RebateCalculationServiceImpl implements RebateCalculationService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PlatformFeeService platformFeeService;
    
    @Autowired
    private UserRelationService userRelationService;
    
    @Autowired
    private AccountService accountService;
    
    @Override
    @Transactional
    public RebateCalculationVO calculateAndDistributeRebate(RebateCalculationDTO dto) {
        // 1. 计算返点
        RebateCalculationVO result = calculateRebate(dto);
        
        // 2. 分配返点到上级用户
        if (result.getParentRebateAmount() != null && result.getParentRebateAmount().compareTo(BigDecimal.ZERO) != 0) {
            accountService.addRebate(result.getParentUserId(), result.getParentRebateAmount(), 1);
            log.info("为上级用户 {} 分配返点: {}", result.getParentUserId(), result.getParentRebateAmount());
        }
        
        // 3. 分配返点到上上级用户
        if (result.getGrandParentRebateAmount() != null && result.getGrandParentRebateAmount().compareTo(BigDecimal.ZERO) != 0) {
            accountService.addRebate(result.getGrandParentUserId(), result.getGrandParentRebateAmount(), 1);
            log.info("为上上级用户 {} 分配返点: {}", result.getGrandParentUserId(), result.getGrandParentRebateAmount());
        }
        
        return result;
    }
    
    @Override
    public RebateCalculationVO calculateRebate(RebateCalculationDTO dto) {
        RebateCalculationVO result = new RebateCalculationVO();
        result.setUserId(dto.getUserId());
        result.setCategory(dto.getCategory());
        result.setWeight(dto.getWeight());
        
        // 1. 获取用户信息
        User user = userService.getUserByAccount(dto.getUserId());
        if (user == null) {
            log.error("用户不存在: {}", dto.getUserId());
            return result;
        }
        
        result.setUserLevel(user.getUserLevel());
        
        // 2. 获取用户费率
        PlatformFee userFee = platformFeeService.getByUserLevelAndCategory(user.getUserLevel(), dto.getCategory());
        if (userFee == null) {
            log.error("用户费率不存在: userLevel={}, category={}", user.getUserLevel(), dto.getCategory());
            return result;
        }
        result.setUserFeeRate(userFee.getFeeRate());
        
        // 3. 获取上级用户信息
        String parentUserId = userRelationService.getParentIdByAccount(dto.getUserId());
        if (parentUserId != null && !parentUserId.isEmpty()) {
            User parentUser = userService.getUserByAccount(parentUserId);
            if (parentUser != null) {
                result.setParentUserId(parentUserId);
                result.setParentUserLevel(parentUser.getUserLevel());
                
                // 获取上级用户费率
                PlatformFee parentFee = platformFeeService.getByUserLevelAndCategory(parentUser.getUserLevel(), dto.getCategory());
                if (parentFee != null) {
                    result.setParentFeeRate(parentFee.getFeeRate());
                    
                    // 计算上级返点：用户费率 - 上级费率
                    BigDecimal parentRebate = userFee.getFeeRate().subtract(parentFee.getFeeRate());
                    if (parentRebate.compareTo(BigDecimal.ZERO) > 0) {
                        BigDecimal parentRebateAmount = parentRebate.multiply(dto.getWeight()).setScale(2, RoundingMode.HALF_UP);
                        result.setParentRebateAmount(parentRebateAmount);
                    }
                }
                
                // 4. 获取上上级用户信息
                String grandParentUserId = userRelationService.getParentIdByAccount(parentUserId);
                if (grandParentUserId != null && !grandParentUserId.isEmpty()) {
                    User grandParentUser = userService.getUserByAccount(grandParentUserId);
                    if (grandParentUser != null) {
                        result.setGrandParentUserId(grandParentUserId);
                        result.setGrandParentUserLevel(grandParentUser.getUserLevel());
                        
                        // 获取上上级用户费率
                        PlatformFee grandParentFee = platformFeeService.getByUserLevelAndCategory(grandParentUser.getUserLevel(), dto.getCategory());
                        if (grandParentFee != null) {
                            result.setGrandParentFeeRate(grandParentFee.getFeeRate());
                            
                            // 计算上上级返点：上级费率 - 上上级费率
                            BigDecimal grandParentRebate = parentFee.getFeeRate().subtract(grandParentFee.getFeeRate());
                            // 允许负数返点，表示需要扣除
                            BigDecimal grandParentRebateAmount = grandParentRebate.multiply(dto.getWeight()).setScale(2, RoundingMode.HALF_UP);
                            result.setGrandParentRebateAmount(grandParentRebateAmount);
                        }
                    }
                }
            }
        }
        
        return result;
    }
} 