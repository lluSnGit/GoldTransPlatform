package com.mall.service;

import com.mall.dto.AdminDTO;
import com.mall.dto.LoginResult;
import com.mall.entity.Admin;
import java.util.List;

public interface AdminService {
    /**
     * 获取所有管理员列表
     */
    List<AdminDTO> getAllAdmins();
    
    /**
     * 创建管理员
     * @return 返回token和adminId
     */
    LoginResult createAdmin(AdminDTO adminDTO);
    
    /**
     * 管理员登录
     * @return 返回token和adminId
     */
    LoginResult login(String username, String password);
    
    /**
     * 根据用户名查询管理员
     */
    Admin getByUsername(String username);
    
    /**
     * 更新管理员信息
     */
    boolean updateAdmin(String adminId, AdminDTO adminDTO);
    
    /**
     * 更新管理员状态
     */
    boolean updateStatus(String adminId, Integer status);
    
    /**
     * 删除管理员
     */
    boolean deleteAdmin(String adminId);
    
    /**
     * 根据adminId查询管理员
     */
    Admin getByAdminId(String adminId);
} 