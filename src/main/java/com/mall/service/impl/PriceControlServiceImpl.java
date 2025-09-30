package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.entity.PriceControl;
import com.mall.mapper.PriceControlMapper;
import com.mall.service.PriceControlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PriceControlServiceImpl extends ServiceImpl<PriceControlMapper, PriceControl> implements PriceControlService {
    
    @Override
    public PriceControl getByPriceType(Integer priceType) {
        LambdaQueryWrapper<PriceControl> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PriceControl::getPriceType, priceType);
        return getOne(wrapper);
    }
    
    @Override
    @Transactional
    public boolean updatePriceControl(PriceControl priceControl) {
        return updateById(priceControl);
    }
    
    @Override
    public List<PriceControl> getAllPriceControls() {
        return list();
    }
} 