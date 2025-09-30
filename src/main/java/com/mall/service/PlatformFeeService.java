package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.entity.PlatformFee;
import com.mall.vo.PlatformFeeVO;
import java.math.BigDecimal;
import java.util.List;

public interface PlatformFeeService extends IService<PlatformFee> {
    
    /**
     * 设置平台手续费
     * @param userLevel 用户等级
     * @param levelName 用户等级名称
     * @param feeRate 手续费比例
     * @param category 类别 0=黄金 1=铂金 2=钯金
     * @return 是否成功
     */
    boolean setPlatformFee(Integer userLevel, String levelName, BigDecimal feeRate, Integer category);
    
    /**
     * 获取平台手续费比例
     * @return 手续费比例
     */
    
    /**
     * 根据用户等级获取对应的平台手续费规则
     * @param userLevel 用户等级
     * @return 平台手续费规则
     */
    PlatformFee getByUserLevel(Integer userLevel);

    /**
     * 根据用户等级和类别获取对应的平台手续费规则
     * @param userLevel 用户等级
     * @param category 类别
     * @return 平台手续费规则
     */
    PlatformFee getByUserLevelAndCategory(Integer userLevel, Integer category);

    List<PlatformFeeVO> getAllUserLevels();

    /**
     * 根据用户等级获取所有类别的手续费记录
     */
    List<PlatformFee> listByUserLevel(Integer userLevel);
} 