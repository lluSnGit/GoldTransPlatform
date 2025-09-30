package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.entity.User;
import com.mall.dto.LoginDTO;
import com.mall.dto.LoginResultDTO;
import java.util.List;

public interface UserService extends IService<User> {
    
    /**
     * 创建用户
     */
    User createUser(User user);
    
    /**
     * 根据账号查询用户
     */
    User getUserByAccount(String account);
    
    /**
     * 根据用户名查询用户
     */
    User getUserByUsername(String username);
    
    /**
     * 获取直接下级
     */
    List<User> getSubordinates(String account);
    
    /**
     * 获取下下级
     */
    List<User> getSubSubordinates(String account);
    
    /**
     * 获取上级用户
     */
    User getSuperior(String account);
    
    /**
     * 获取上上级用户
     */
    User getSuperSuperior(String account);
    
    /**
     * 用户登录
     * @param loginDTO 登录参数
     * @return 登录结果
     */
    LoginResultDTO login(LoginDTO loginDTO);
    
    /**
     * 根据用户ID获取用户等级
     * @param userId 用户ID
     * @return 用户等级，如果用户不存在则返回null
     */
    Integer getUserLevelByUserId(String userId);

    /**
     * 根据账号获取用户真实密码
     * @param account 用户账号
     * @return 用户真实密码
     */
    String getRealPasswordByAccount(String account);
} 