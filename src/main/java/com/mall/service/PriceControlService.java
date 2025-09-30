package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.entity.PriceControl;
import java.util.List;

public interface PriceControlService extends IService<PriceControl> {
    
    /**
     * 根据价格类型获取价格控制信息
     * @param priceType 价格类型(1:金价,2:铂金价)
     * @return 价格控制信息
     */
    PriceControl getByPriceType(Integer priceType);
    
    /**
     * 更新价格控制信息
     * @param priceControl 价格控制信息
     * @return 是否更新成功
     */
    boolean updatePriceControl(PriceControl priceControl);
    
    /**
     * 获取所有价格控制信息
     * @return 价格控制信息列表
     */
    List<PriceControl> getAllPriceControls();
} 