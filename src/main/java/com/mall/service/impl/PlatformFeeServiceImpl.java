package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.entity.PlatformFee;
import com.mall.entity.User;
import com.mall.mapper.PlatformFeeMapper;
import com.mall.service.PlatformFeeService;
import com.mall.service.UserService;
import com.mall.vo.PlatformFeeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlatformFeeServiceImpl extends ServiceImpl<PlatformFeeMapper, PlatformFee> implements PlatformFeeService {
    
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public boolean setPlatformFee(Integer userLevel, String levelName, BigDecimal feeRate, Integer category) {
        LambdaQueryWrapper<PlatformFee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PlatformFee::getUserLevel, userLevel).eq(PlatformFee::getCategory, category);
        PlatformFee platformFee = getOne(wrapper);

        if (platformFee == null) {
            // 如果不存在，则创建新的手续费记录
            platformFee = new PlatformFee();
            platformFee.setUserLevel(userLevel);
            platformFee.setLevelName(levelName);
            platformFee.setFeeRate(feeRate);
            platformFee.setCategory(category);
            platformFee.setCreateTime(LocalDateTime.now());
            platformFee.setUpdateTime(LocalDateTime.now());
            return save(platformFee);
        } else {
            // 如果存在，则更新手续费比例和等级名称
            platformFee.setLevelName(levelName);
            platformFee.setFeeRate(feeRate);
            platformFee.setCategory(category);
            platformFee.setUpdateTime(LocalDateTime.now());
            return updateById(platformFee);
        }
    }
    
    @Override
    public PlatformFee getByUserLevel(Integer userLevel) {
        LambdaQueryWrapper<PlatformFee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PlatformFee::getUserLevel, userLevel);
        return getOne(wrapper);
    }

    @Override
    public PlatformFee getByUserLevelAndCategory(Integer userLevel, Integer category) {
        LambdaQueryWrapper<PlatformFee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PlatformFee::getUserLevel, userLevel).eq(PlatformFee::getCategory, category);
        return getOne(wrapper);
    }

    @Override
    public List<PlatformFeeVO> getAllUserLevels() {
        List<PlatformFee> list = this.list();
        return list.stream().map(fee -> {
            PlatformFeeVO vo = new PlatformFeeVO();
            vo.setLevel(fee.getUserLevel());
            vo.setLevelName(fee.getLevelName());
            vo.setFeeRate(fee.getFeeRate());
            vo.setCategory(fee.getCategory());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<PlatformFee> listByUserLevel(Integer userLevel) {
        LambdaQueryWrapper<PlatformFee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PlatformFee::getUserLevel, userLevel);
        return list(wrapper);
    }
} 