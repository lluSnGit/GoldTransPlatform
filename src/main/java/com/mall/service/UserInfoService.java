package com.mall.service;

import com.mall.dto.UserInfoDTO;

public interface UserInfoService {
    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    UserInfoDTO getUserInfo(String userId);
    
    /**
     * 更新用户信息
     * @param userId 用户ID
     * @param userInfoDTO 用户信息
     * @return 是否成功
     */
    boolean updateUserInfo(String userId, UserInfoDTO userInfoDTO);
} 