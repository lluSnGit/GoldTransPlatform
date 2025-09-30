package com.mall.service;

import com.mall.dto.UserRelationFeeDTO;

public interface UserRelationFeeService {
    /**
     * 获取用户关系费率信息
     * @param account 用户账号
     * @return 用户关系费率信息
     */
    UserRelationFeeDTO getUserRelationFee(String account);
}