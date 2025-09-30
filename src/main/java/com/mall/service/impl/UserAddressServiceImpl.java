package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.entity.UserAddress;
import com.mall.mapper.UserAddressMapper;
import com.mall.service.UserAddressService;
import com.mall.dto.UserAddressDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {

    @Override
    public List<UserAddressDTO> getAddressList(String account) {
        LambdaQueryWrapper<UserAddress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAddress::getAccount, account);
        return list(wrapper).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public UserAddressDTO getAddressDetail(String addressId) {
        LambdaQueryWrapper<UserAddress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAddress::getAddressId, addressId);
        return convertToDTO(getOne(wrapper));
    }

    @Override
    @Transactional
    public UserAddressDTO addAddress(UserAddressDTO dto) {
        UserAddress address = new UserAddress();
        BeanUtils.copyProperties(dto, address);
        // 生成随机地址ID
        address.setAddressId(UUID.randomUUID().toString().replace("-", ""));
        boolean isSaved = save(address);
        if (isSaved) {
            // 返回保存后的地址信息，包括生成的ID
            return convertToDTO(address);
        } else {
            return null; // 保存失败返回 null
        }
    }

    @Override
    @Transactional
    public boolean updateAddress(UserAddressDTO dto) {
        LambdaQueryWrapper<UserAddress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAddress::getAddressId, dto.getAddressId());
        UserAddress address = getOne(wrapper);
        if (address == null) {
            return false;
        }
        BeanUtils.copyProperties(dto, address);
        return updateById(address);
    }

    @Override
    @Transactional
    public boolean deleteAddress(String addressId) {
         LambdaQueryWrapper<UserAddress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAddress::getAddressId, addressId);
        return remove(wrapper);
    }

    @Override
    @Transactional
    public boolean setDefaultAddress(String addressId) {
        LambdaQueryWrapper<UserAddress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAddress::getAddressId, addressId);
        UserAddress address = getOne(wrapper);
        if (address == null) {
            return false;
        }
        
        // 先将该账号下所有地址设为非默认
        LambdaQueryWrapper<UserAddress> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(UserAddress::getAccount, address.getAccount());
        UserAddress updateAddress = new UserAddress();
        updateAddress.setIsDefault(0);
        update(updateAddress, updateWrapper);
        
        // 设置当前地址为默认
        address.setIsDefault(1);
        return updateById(address);
    }

    @Override
    public String getAddressInfo(String addressId) {
        if (addressId == null) {
            return null;
        }
        LambdaQueryWrapper<UserAddress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAddress::getAddressId, addressId);
        UserAddress address = getOne(wrapper);
        if (address == null) {
            return null;
        }
        // 拼接完整的地址信息
        return String.format("%s%s%s%s", 
            address.getProvince() != null ? address.getProvince() : "",
            address.getCity() != null ? address.getCity() : "",
            address.getDistrict() != null ? address.getDistrict() : "",
            address.getDetailAddress() != null ? address.getDetailAddress() : "");
    }

    private UserAddressDTO convertToDTO(UserAddress address) {
        if (address == null) {
            return null;
        }
        UserAddressDTO dto = new UserAddressDTO();
        BeanUtils.copyProperties(address, dto);
        return dto;
    }
} 