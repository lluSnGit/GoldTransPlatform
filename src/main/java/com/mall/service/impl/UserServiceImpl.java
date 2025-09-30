package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.entity.User;
import com.mall.entity.Account;
import com.mall.entity.UserRelation;
import com.mall.mapper.UserMapper;
import com.mall.mapper.UserRelationMapper;
import com.mall.service.UserService;
import com.mall.service.AccountService;
import com.mall.dto.LoginDTO;
import com.mall.dto.LoginResultDTO;
import com.mall.common.exception.BusinessException;
import com.mall.util.EncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.time.LocalDateTime;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    @Autowired
    private UserRelationMapper userRelationMapper;
    
    @Autowired
    private AccountService accountService;
    
    @Override
    @Transactional
    public User createUser(User user) {
        // 先检查用户表中是否存在该账号
        LambdaQueryWrapper<User> accountWrapper = new LambdaQueryWrapper<>();
        accountWrapper.eq(User::getAccount, user.getAccount());
        User existingUser = getOne(accountWrapper);
        
        if (existingUser != null) {
            throw new BusinessException("账号已存在");
        }
        
        // 生成新的用户随机ID
        String userId = UUID.randomUUID().toString().replace("-", "");
        user.setUserId(userId);
        
        // 加密密码
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(EncryptUtil.encrypt(user.getPassword()));
        }
        
        // 设置创建时间
        user.setCreateTime(LocalDateTime.now());
        
        // 保存用户
        save(user);
        
        // 创建或更新账户
        accountService.createOrUpdateAccount(user.getUserId(), user.getAccount());
        
        return user;
    }
    
    @Override
    public User getUserByAccount(String account) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getAccount, account);
        return getOne(wrapper);
    }
    
    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return getOne(wrapper);
    }
    
    @Override
    public List<User> getSubordinates(String account) {
        User user = getUserByAccount(account);
        if (user == null) {
            return new ArrayList<>();
        }
        
        // 查询下级用户
        return baseMapper.selectList(
            new LambdaQueryWrapper<User>()
                .inSql(User::getUserId, 
                    "SELECT user_id FROM user_relation WHERE parent_id = '" + user.getUserId() + "'")
        );
    }
    
    @Override
    public List<User> getSubSubordinates(String account) {
        User user = getUserByAccount(account);
        if (user == null) {
            return new ArrayList<>();
        }
        
        // 查询下下级用户
        return baseMapper.selectList(
            new LambdaQueryWrapper<User>()
                .inSql(User::getUserId, 
                    "SELECT r1.user_id FROM user_relation r1 " +
                    "JOIN user_relation r2 ON r1.parent_id = r2.user_id " +
                    "WHERE r2.parent_id = '" + user.getUserId() + "'")
        );
    }
    
    @Override
    public LoginResultDTO login(LoginDTO loginDTO) {
        // 根据账号查询用户
        User user = getUserByAccount(loginDTO.getAccount());
        if (user == null) {
            throw new BusinessException("账号不存在");
        }
        
        // 验证密码
        String encryptedPassword = EncryptUtil.encrypt(loginDTO.getPassword());
        if (!user.getPassword().equals(encryptedPassword)) {
            throw new BusinessException("密码错误");
        }
        
        // 生成访问令牌
        String token = UUID.randomUUID().toString().replace("-", "");
        
        // 构建返回结果
        return LoginResultDTO.builder()
                .userId(user.getUserId())
                .account(user.getAccount())
                .username(user.getUsername())
                .userLevel(user.getUserLevel())
                .roleLevel(user.getRoleLevel())
                .token(token)
                .build();
    }
    
    @Override
    public Integer getUserLevelByUserId(String userId) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserId, userId);
        User user = getOne(wrapper);
        return user != null ? user.getUserLevel() : null;
    }

    @Override
    public User getSuperior(String account) {
        User user = getUserByAccount(account);
        if (user == null) {
            return null;
        }
        
        // 查询上级用户
        return baseMapper.selectOne(
            new LambdaQueryWrapper<User>()
                .inSql(User::getUserId, 
                    "SELECT parent_id FROM user_relation WHERE user_id = '" + user.getUserId() + "'")
        );
    }

    @Override
    public User getSuperSuperior(String account) {
        User user = getUserByAccount(account);
        if (user == null) {
            return null;
        }
        
        // 查询上上级用户
        return baseMapper.selectOne(
            new LambdaQueryWrapper<User>()
                .inSql(User::getUserId, 
                    "SELECT r1.parent_id FROM user_relation r1 " +
                    "JOIN user_relation r2 ON r1.user_id = r2.parent_id " +
                    "WHERE r2.user_id = '" + user.getUserId() + "'")
        );
    }

    @Override
    public String getRealPasswordByAccount(String account) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getAccount, account);
        User user = getOne(wrapper);
        if (user == null) {
            return null;
        }
        // 解密密码
        return EncryptUtil.decrypt(user.getPassword());
    }

    @Override
    public boolean updateById(User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            if (!isEncrypted(user.getPassword())) {
                user.setPassword(EncryptUtil.encrypt(user.getPassword()));
            }
        }
        return super.updateById(user);
    }

    // 判断密码是否为密文（简单规则：长度大于16且包含=或/或+）
    private boolean isEncrypted(String password) {
        return password.length() > 16 && password.matches(".*[=/+].*");
    }
} 