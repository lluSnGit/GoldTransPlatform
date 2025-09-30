package com.mall.service.impl;

import com.mall.dto.UserRelationFeeDTO;
import com.mall.entity.User;
import com.mall.entity.UserRelation;
import com.mall.entity.PlatformFee;
import com.mall.service.UserRelationFeeService;
import com.mall.service.UserRelationService;
import com.mall.service.UserService;
import com.mall.service.PlatformFeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserRelationFeeServiceImpl implements UserRelationFeeService {

    @Autowired
    private UserRelationService userRelationService;

    @Autowired
    private UserService userService;

    @Autowired
    private PlatformFeeService platformFeeService;

    @Override
    public UserRelationFeeDTO getUserRelationFee(String account) {
        log.info("获取用户关系费率信息: {}", account);
        UserRelationFeeDTO dto = new UserRelationFeeDTO();

        // 获取上级关系
        UserRelation parentRelation = userRelationService.getByAccount(account);
        if (parentRelation != null) {
            dto.setParentId(parentRelation.getParentId());
            
            // 获取上级用户信息
            User parentUser = userService.getUserByAccount(parentRelation.getParentId());
            if (parentUser != null) {
                dto.setParentLevel(parentUser.getUserLevel());
                
                // 获取上级费率
                PlatformFee parentFee = platformFeeService.getByUserLevel(parentUser.getUserLevel());
                if (parentFee != null) {
                    dto.setParentFeeRate(parentFee.getFeeRate());
                }
            }
        }

        // 获取上上级关系
        String grandParentId = userRelationService.getGrandParentIdByAccount(account);
        if (grandParentId != null) {
            dto.setGrandParentId(grandParentId);
            
            // 获取上上级用户信息
            User grandParentUser = userService.getUserByAccount(grandParentId);
            if (grandParentUser != null) {
                dto.setGrandParentLevel(grandParentUser.getUserLevel());
                
                // 获取上上级费率
                PlatformFee grandParentFee = platformFeeService.getByUserLevel(grandParentUser.getUserLevel());
                if (grandParentFee != null) {
                    dto.setGrandParentFeeRate(grandParentFee.getFeeRate());
                }
            }
        }

        log.info("用户关系费率信息: {}", dto);
        return dto;
    }
} 