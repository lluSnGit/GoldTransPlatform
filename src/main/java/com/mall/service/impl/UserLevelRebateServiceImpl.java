package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.entity.User;
import com.mall.entity.UserLevelRebate;
import com.mall.mapper.UserLevelRebateMapper;
import com.mall.service.UserLevelRebateService;
import com.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class UserLevelRebateServiceImpl extends ServiceImpl<UserLevelRebateMapper, UserLevelRebate> implements UserLevelRebateService {
    
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public boolean setUserLevelRebate(Integer userLevel, BigDecimal rebateRate) {
        UserLevelRebate rebate = new UserLevelRebate();
        rebate.setUserLevel(userLevel);
        rebate.setRebateRate(rebateRate);
        return save(rebate);
    }
    
    @Override
    public UserLevelRebate getByUserLevel(String userId) {
        LambdaQueryWrapper<UserLevelRebate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserLevelRebate::getUserId, userId);
        return getOne(wrapper);
    }
    
    @Override
    public BigDecimal getUserLevelRebate(Integer userLevel) {
        LambdaQueryWrapper<UserLevelRebate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserLevelRebate::getUserLevel, userLevel);
        UserLevelRebate rebate = getOne(wrapper);
        return rebate != null ? rebate.getRebateRate() : BigDecimal.ZERO;
    }
} 