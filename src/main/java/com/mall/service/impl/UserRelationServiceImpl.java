package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.entity.UserRelation;
import com.mall.entity.User;
import com.mall.mapper.UserRelationMapper;
import com.mall.service.UserRelationService;
import com.mall.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserRelationServiceImpl extends ServiceImpl<UserRelationMapper, UserRelation> implements UserRelationService {
    
    @Autowired
    private UserService userService;
    
    @Override
    public List<UserRelation> getRelationsByUserId(String userId) {
        log.info("通过用户ID查询关系: {}", userId);
        List<UserRelation> relations = list(new LambdaQueryWrapper<UserRelation>()
            .eq(UserRelation::getUserId, userId));
        log.info("查询结果: {}", relations);
        return relations;
    }
    
    @Override
    public List<UserRelation> getRelationsByAccount(String account) {
        log.info("通过账号查询关系: {}", account);
        // 直接使用account作为user_id查询
        List<UserRelation> relations = list(new LambdaQueryWrapper<UserRelation>()
            .eq(UserRelation::getUserId, account));
        log.info("查询结果: {}", relations);
        return relations;
    }
    
    @Override
    public List<UserRelation> getChildRelations(String userId) {
        log.info("查询用户直接下级: {}", userId);
        List<UserRelation> relations = list(new LambdaQueryWrapper<UserRelation>()
            .eq(UserRelation::getParentId, userId));
        log.info("查询结果: {}", relations);
        return relations;
    }
    
    @Override
    public List<UserRelation> getChildRelationsByAccount(String account) {
        log.info("通过账号查询用户直接下级: {}", account);
        // 直接使用account作为parent_id查询
        List<UserRelation> relations = list(new LambdaQueryWrapper<UserRelation>()
            .eq(UserRelation::getParentId, account));
        log.info("查询结果: {}", relations);
        return relations;
    }
    
    @Override
    public List<UserRelation> getSubChildRelations(String userId) {
        log.info("查询用户下下级: {}", userId);
        // 先获取直接下级
        List<UserRelation> directChildren = getChildRelations(userId);
        if (directChildren.isEmpty()) {
            log.info("用户没有直接下级");
            return new ArrayList<>();
        }
        
        // 获取直接下级的用户ID列表
        List<String> directChildIds = directChildren.stream()
            .map(UserRelation::getUserId)
            .collect(Collectors.toList());
        log.info("直接下级ID列表: {}", directChildIds);
            
        // 获取下下级
        List<UserRelation> subChildren = list(new LambdaQueryWrapper<UserRelation>()
            .in(UserRelation::getParentId, directChildIds));
        log.info("下下级查询结果: {}", subChildren);
        return subChildren;
    }
    
    @Override
    public List<UserRelation> getSubChildRelationsByAccount(String account) {
        log.info("通过账号查询用户下下级: {}", account);
        // 1. 先查询直接下级
        List<UserRelation> directChildren = getChildRelationsByAccount(account);
        log.info("直接下级关系: {}", directChildren);
        
        if (directChildren.isEmpty()) {
            log.info("用户没有直接下级");
            return new ArrayList<>();
        }
        
        // 2. 获取直接下级的用户ID列表
        List<String> directChildIds = directChildren.stream()
            .map(UserRelation::getUserId)
            .collect(Collectors.toList());
        log.info("直接下级ID列表: {}", directChildIds);
            
        // 3. 查询这些直接下级的下级关系
        List<UserRelation> subChildren = list(new LambdaQueryWrapper<UserRelation>()
            .in(UserRelation::getParentId, directChildIds));
        log.info("下下级查询结果: {}", subChildren);
        return subChildren;
    }
    
    @Override
    public String getGrandParentId(String userId) {
        log.info("查询用户上上级: {}", userId);
        // 先获取用户的上级
        UserRelation relation = getOne(new LambdaQueryWrapper<UserRelation>()
            .eq(UserRelation::getUserId, userId));
            
        if (relation == null) {
            log.warn("未找到用户的上级关系");
            return null;
        }
        log.info("找到用户上级关系: {}", relation);
        
        // 再获取上级的上级
        UserRelation parentRelation = getOne(new LambdaQueryWrapper<UserRelation>()
            .eq(UserRelation::getUserId, relation.getParentId()));
            
        if (parentRelation == null) {
            log.warn("未找到上上级关系");
            return null;
        }
        log.info("找到上上级关系: {}", parentRelation);
        return parentRelation.getParentId();
    }
    
    @Override
    public String getGrandParentIdByAccount(String account) {
        log.info("通过账号查询用户上上级: {}", account);
        // 先获取用户的上级
        UserRelation relation = getOne(new LambdaQueryWrapper<UserRelation>()
            .eq(UserRelation::getUserId, account));
            
        if (relation == null) {
            log.warn("未找到用户的上级关系");
            return null;
        }
        log.info("找到用户上级关系: {}", relation);
        
        // 再获取上级的上级
        UserRelation parentRelation = getOne(new LambdaQueryWrapper<UserRelation>()
            .eq(UserRelation::getUserId, relation.getParentId()));
            
        if (parentRelation == null) {
            log.warn("未找到上上级关系");
            return null;
        }
        log.info("找到上上级关系: {}", parentRelation);
        return parentRelation.getParentId();
    }

    @Override
    public UserRelation getByAccount(String account) {
        log.info("通过账号查询用户关系: {}", account);
        UserRelation relation = getOne(new LambdaQueryWrapper<UserRelation>()
            .eq(UserRelation::getUserId, account));
        log.info("查询结果: {}", relation);
        return relation;
    }
    
    @Override
    public String getParentIdByAccount(String account) {
        log.info("通过账号查询用户直接上级: {}", account);
        UserRelation relation = getOne(new LambdaQueryWrapper<UserRelation>()
            .eq(UserRelation::getUserId, account));
            
        if (relation == null) {
            log.warn("未找到用户的上级关系");
            return null;
        }
        log.info("找到用户上级关系: {}", relation);
        return relation.getParentId();
    }
} 