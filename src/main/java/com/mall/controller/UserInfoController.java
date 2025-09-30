package com.mall.controller;

import com.mall.common.api.CommonResult;
import com.mall.dto.UserInfoDTO;
import com.mall.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/info")
@Tag(name = "用户信息管理", description = "用户信息相关接口")
public class UserInfoController {
    
    @Autowired
    private UserInfoService userInfoService;
    
    @GetMapping("/{userId}")
    @Operation(summary = "获取用户信息")
    public CommonResult<UserInfoDTO> getUserInfo(@PathVariable String userId) {
        return CommonResult.success(userInfoService.getUserInfo(userId));
    }
    
    @PutMapping("/{userId}")
    @Operation(summary = "更新用户信息")
    public CommonResult<Boolean> updateUserInfo(@PathVariable String userId, 
                                              @Validated @RequestBody UserInfoDTO userInfoDTO) {
        return CommonResult.success(userInfoService.updateUserInfo(userId, userInfoDTO));
    }
} 