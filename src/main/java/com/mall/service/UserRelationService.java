package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.entity.UserRelation;
import java.util.List;
 
public interface UserRelationService extends IService<UserRelation> {
    /**
     * 根据用户ID获取关系
     */
    List<UserRelation> getRelationsByUserId(String userId);
    
    /**
     * 根据账号获取关系
     */
    List<UserRelation> getRelationsByAccount(String account);
    
    /**
     * 获取用户的直接下级关系列表
     */
    List<UserRelation> getChildRelations(String userId);
    
    /**
     * 根据账号获取用户的直接下级关系列表
     */
    List<UserRelation> getChildRelationsByAccount(String account);
    
    /**
     * 获取用户的下下级关系列表
     */
    List<UserRelation> getSubChildRelations(String userId);
    
    /**
     * 根据账号获取用户的下下级关系列表
     */
    List<UserRelation> getSubChildRelationsByAccount(String account);
    
    /**
     * 获取用户的上上级用户ID
     */
    String getGrandParentId(String userId);
    
    /**
     * 根据账号获取用户的上上级用户ID
     */
    String getGrandParentIdByAccount(String account);

    /**
     * 根据账号获取用户关系
     * @param account 用户账号
     * @return 用户关系
     */
    UserRelation getByAccount(String account);
    
    /**
     * 根据账号获取用户的直接上级账号
     * @param account 用户账号
     * @return 直接上级账号
     */
    String getParentIdByAccount(String account);
} 