package com.mall.controller;

import com.mall.common.api.CommonResult;
import com.mall.dto.UserAddressDTO;
import com.mall.service.UserAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/address")
@Tag(name = "用户地址管理", description = "用户地址相关接口")
public class UserAddressController {
    
    @Autowired
    private UserAddressService userAddressService;
    
    @PostMapping("/list")
    @Operation(summary = "获取用户地址列表")
    public CommonResult<List<UserAddressDTO>> getAddressList(@RequestParam String account) {
        return CommonResult.success(userAddressService.getAddressList(account));
    }
    
    @PostMapping("/detail")
    @Operation(summary = "获取地址详情")
    public CommonResult<UserAddressDTO> getAddressDetail(@RequestParam String addressId) {
        return CommonResult.success(userAddressService.getAddressDetail(addressId));
    }
    
    @PostMapping("/add")
    @Operation(summary = "新增收货地址")
    public CommonResult<UserAddressDTO> addAddress(@ModelAttribute @Validated UserAddressDTO dto) {
        UserAddressDTO savedAddress = userAddressService.addAddress(dto);
        if (savedAddress != null) {
            return CommonResult.success(savedAddress);
        } else {
            return CommonResult.failed("新增收货地址失败");
        }
    }
    
    @PutMapping("/update")
    @Operation(summary = "更新收货地址")
    public CommonResult<Boolean> updateAddress(@ModelAttribute @Validated UserAddressDTO dto) {
        return CommonResult.success(userAddressService.updateAddress(dto));
    }
    
    @DeleteMapping("/delete")
    @Operation(summary = "删除收货地址")
    public CommonResult<Boolean> deleteAddress(@RequestParam String addressId) {
        return CommonResult.success(userAddressService.deleteAddress(addressId));
    }
    
    @PutMapping("/default")
    @Operation(summary = "设置默认地址")
    public CommonResult<Boolean> setDefaultAddress(@RequestParam String addressId) {
        return CommonResult.success(userAddressService.setDefaultAddress(addressId));
    }
} 