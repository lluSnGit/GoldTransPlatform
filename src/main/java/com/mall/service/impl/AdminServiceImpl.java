package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall.entity.Admin;
import com.mall.mapper.AdminMapper;
import com.mall.service.AdminService;
import com.mall.dto.AdminDTO;
import com.mall.dto.LoginResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public LoginResult createAdmin(AdminDTO adminDTO) {
        // 检查用户名是否已存在
        if (getByUsername(adminDTO.getUsername()) != null) {
            return null;
        }
        
        Admin admin = new Admin();
        BeanUtils.copyProperties(adminDTO, admin);
        
        // 生成adminId
        String adminId = UUID.randomUUID().toString().replace("-", "");
        admin.setAdminId(adminId);
        
        // 加密密码
        admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
        admin.setPhone(adminDTO.getPhone());
        admin.setEmail(adminDTO.getEmail());
        admin.setStatus(1);
        admin.setAdminType(adminDTO.getAdminType() != null ? adminDTO.getAdminType() : 0);
        admin.setCreateTime(LocalDateTime.now());
        admin.setUpdateTime(LocalDateTime.now());
        
        boolean success = adminMapper.insert(admin) > 0;
        if (success) {
            // 生成token
            String token = UUID.randomUUID().toString().replace("-", "");
            return new LoginResult(token, adminId);
        }
        return null;
    }

    @Override
    public LoginResult login(String username, String password) {
        Admin admin = getByUsername(username);
        if (admin == null) {
            return null;
        }
        
        // 检查密码
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            return null;
        }
        
        // 检查状态
        if (admin.getStatus() != 1) {
            return null;
        }
        
        // 生成token
        String token = UUID.randomUUID().toString().replace("-", "");
        
        return new LoginResult(token, admin.getAdminId());
    }

    @Override
    public Admin getByUsername(String username) {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername, username);
        return adminMapper.selectOne(wrapper);
    }

    @Override
    public Admin getByAdminId(String adminId) {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getAdminId, adminId);
        return adminMapper.selectOne(wrapper);
    }

    @Override
    public boolean updateAdmin(String adminId, AdminDTO adminDTO) {
        Admin admin = getByAdminId(adminId);
        if (admin == null) {
            return false;
        }
        
        BeanUtils.copyProperties(adminDTO, admin);
        if (adminDTO.getPassword() != null && !adminDTO.getPassword().isEmpty()) {
            admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
        }
        admin.setUpdateTime(LocalDateTime.now());
        
        return adminMapper.updateById(admin) > 0;
    }

    @Override
    public boolean updateStatus(String adminId, Integer status) {
        Admin admin = getByAdminId(adminId);
        if (admin == null) {
            return false;
        }
        
        admin.setStatus(status);
        admin.setUpdateTime(LocalDateTime.now());
        
        return adminMapper.updateById(admin) > 0;
    }

    @Override
    public boolean deleteAdmin(String adminId) {
        Admin admin = getByAdminId(adminId);
        if (admin == null) {
            return false;
        }
        return adminMapper.deleteById(admin.getId()) > 0;
    }

    @Override
    public List<AdminDTO> getAllAdmins() {
        List<Admin> admins = adminMapper.selectList(null);
        return admins.stream().map(admin -> {
            AdminDTO dto = new AdminDTO();
            BeanUtils.copyProperties(admin, dto);
            return dto;
        }).collect(Collectors.toList());
    }
} 