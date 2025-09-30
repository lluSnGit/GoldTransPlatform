package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.entity.UserLevelRebate;
import java.math.BigDecimal;

public interface UserLevelRebateService extends IService<UserLevelRebate> {
    
    /**
     * 设置用户等级返点比例
     * @param userLevel 用户等级
     * @param rebateRate 返点比例
     * @return 是否成功
     */
    boolean setUserLevelRebate(Integer userLevel, BigDecimal rebateRate);
    
    /**
     * 根据用户ID获取对应的返点规则
     * @param userId 用户ID
     * @return 返点规则
     */
    UserLevelRebate getByUserLevel(String userId);
    
    /**
     * 获取用户等级返点比例
     * @param userLevel 用户等级
     * @return 返点比例
     */
    BigDecimal getUserLevelRebate(Integer userLevel);
} 