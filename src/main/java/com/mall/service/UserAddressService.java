package com.mall.service;

import com.mall.dto.UserAddressDTO;
import java.util.List;

public interface UserAddressService {
    /**
     * 获取用户地址列表
     */
    List<UserAddressDTO> getAddressList(String account);

    /**
     * 获取地址详情
     */
    UserAddressDTO getAddressDetail(String addressId);

    /**
     * 新增收货地址
     */
    UserAddressDTO addAddress(UserAddressDTO dto);

    /**
     * 更新收货地址
     */
    boolean updateAddress(UserAddressDTO dto);

    /**
     * 删除收货地址
     */
    boolean deleteAddress(String addressId);

    /**
     * 设置默认地址
     */
    boolean setDefaultAddress(String addressId);

    String getAddressInfo(String addressId);
} 