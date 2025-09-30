package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.entity.UserInfo;
import com.mall.mapper.UserInfoMapper;
import com.mall.service.UserInfoService;
import com.mall.dto.UserInfoDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Override
    public UserInfoDTO getUserInfo(String userId) {
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getUserId, userId);
        return convertToDTO(getOne(wrapper));
    }

    @Override
    @Transactional
    public boolean updateUserInfo(String userId, UserInfoDTO userInfoDTO) {
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getUserId, userId);
        UserInfo userInfo = getOne(wrapper);
        
        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setUserId(userId);
        }
        
        BeanUtils.copyProperties(userInfoDTO, userInfo);
        return saveOrUpdate(userInfo);
    }

    private UserInfoDTO convertToDTO(UserInfo userInfo) {
        if (userInfo == null) {
            return null;
        }
        UserInfoDTO dto = new UserInfoDTO();
        BeanUtils.copyProperties(userInfo, dto);
        return dto;
    }
} 