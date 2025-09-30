package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.UserRelation;
import com.mall.entity.User;
import com.mall.service.UserRelationService;
import com.mall.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "用户关系管理接口")
@RestController
@RequestMapping("/api/user-relation")
@Slf4j
public class UserRelationController {
    
    @Autowired
    private UserRelationService userRelationService;
    
    @Autowired
    private UserService userService;
    
    @Operation(summary = "测试用户关系")
    @GetMapping("/test/{account}")
    public Result<Map<String, Object>> testUserRelation(@Parameter(description = "用户账号") @PathVariable String account) {
        Map<String, Object> result = new HashMap<>();
        
        // 1. 检查用户是否存在
        User user = userService.getUserByAccount(account);
        result.put("user", user);
        
        if (user != null) {
            // 2. 检查用户关系
            List<UserRelation> relations = userRelationService.getRelationsByUserId(user.getUserId());
            result.put("relations", relations);
            
            // 3. 检查直接下级
            List<UserRelation> children = userRelationService.getChildRelations(user.getUserId());
            result.put("children", children);
            
            // 4. 检查下下级
            List<UserRelation> subChildren = userRelationService.getSubChildRelations(user.getUserId());
            result.put("subChildren", subChildren);
            
            // 5. 检查上上级
            String grandParentId = userRelationService.getGrandParentId(user.getUserId());
            result.put("grandParentId", grandParentId);
        }
        
        return Result.success(result);
    }
    
    @Operation(summary = "创建用户关系")
    @PostMapping
    public Result<UserRelation> createRelation(
        @RequestParam String account,
        @RequestParam String parentAccount
    ) {
        // 检查用户是否存在
        User user = userService.getUserByAccount(account);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        // 检查父级用户是否存在
        User parentUser = userService.getUserByAccount(parentAccount);
        if (parentUser == null) {
            return Result.error("父级用户不存在");
        }
        
        // 检查是否已经存在关系
        List<UserRelation> existingRelations = userRelationService.getRelationsByAccount(account);
        if (!existingRelations.isEmpty()) {
            return Result.error("该用户已经存在关系");
        }
        
        // 创建关系
        UserRelation relation = new UserRelation();
        relation.setUserId(account);
        relation.setParentId(parentAccount);
        userRelationService.save(relation);
        return Result.success(relation);
    }
    
    @Operation(summary = "获取用户关系")
    @GetMapping("/{account}")
    public Result<List<UserRelation>> getRelationsByAccount(@Parameter(description = "用户账号") @PathVariable String account) {
        return Result.success(userRelationService.getRelationsByAccount(account));
    }
    
    @Operation(summary = "获取用户直接下级")
    @GetMapping("/{account}/children")
    public Result<List<UserRelation>> getChildRelationsByAccount(@Parameter(description = "用户账号") @PathVariable String account) {
        return Result.success(userRelationService.getChildRelationsByAccount(account));
    }
    
    @Operation(summary = "获取用户下下级")
    @GetMapping("/{account}/sub-children")
    public Result<List<UserRelation>> getSubChildRelationsByAccount(@Parameter(description = "用户账号") @PathVariable String account) {
        return Result.success(userRelationService.getSubChildRelationsByAccount(account));
    }
    
    @Operation(summary = "获取用户上上级")
    @GetMapping("/{account}/grand-parent")
    public Result<String> getGrandParentIdByAccount(@Parameter(description = "用户账号") @PathVariable String account) {
        return Result.success(userRelationService.getGrandParentIdByAccount(account));
    }
    
    @Operation(summary = "删除用户关系")
    @DeleteMapping("/{id}")
    public Result<Void> deleteRelation(@Parameter(description = "关系ID") @PathVariable Long id) {
        userRelationService.removeById(id);
        return Result.success();
    }
} 