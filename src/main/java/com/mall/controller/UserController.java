package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.User;
import com.mall.service.UserService;
import com.mall.service.UserRelationService;
import com.mall.dto.LoginDTO;
import com.mall.dto.LoginResultDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import org.hibernate.validator.constraints.Length;
import com.mall.util.EncryptUtil;

@RestController
@RequestMapping("/api/user")
@Tag(name = "用户管理", description = "用户相关接口")
@Slf4j
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRelationService userRelationService;
    
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<LoginResultDTO> login(@RequestBody LoginDTO loginDTO) {
        return Result.success(userService.login(loginDTO));
    }
    
    @PostMapping
    @Operation(summary = "创建用户")
    public Result<User> createUser(
            @Parameter(description = "账号") @RequestParam String account,
            @Parameter(description = "密码") @RequestParam String password,
            @Parameter(description = "用户名") @RequestParam String username,
            @Parameter(description = "用户等级") @RequestParam(defaultValue = "0")  Integer userLevel,
            @Parameter(description = "角色等级") @RequestParam(defaultValue = "0") Integer roleLevel) {
        User user = new User();
        user.setAccount(account);
        user.setPassword(password);
        user.setUsername(username);
        user.setUserLevel(userLevel);   
        user.setRoleLevel(roleLevel);
        return Result.success(userService.createUser(user));
    }
    
    @GetMapping("/account/{account}")
    @Operation(summary = "获取用户信息", description = "根据账号获取用户信息")
    public Result<User> getUserByAccount(@Parameter(description = "用户账号") @PathVariable String account) {
        return Result.success(userService.getUserByAccount(account));
    }
    
    @GetMapping("/account/{account}/subordinates")
    @Operation(summary = "获取下级用户")
    public Result<List<User>> getSubordinatesByAccount(@Parameter(description = "用户账号") @PathVariable String account) {
        return Result.success(userService.getSubordinates(account));
    }
    
    @GetMapping("/account/{account}/sub-subordinates")
    @Operation(summary = "获取下下级用户")
    public Result<List<User>> getSubSubordinatesByAccount(@Parameter(description = "用户账号") @PathVariable String account) {
        return Result.success(userService.getSubSubordinates(account));
    }
    
    @GetMapping("/account/{account}/superior")
    @Operation(summary = "获取上级用户")
    public Result<User> getSuperior(@Parameter(description = "用户账号") @PathVariable String account) {
        return Result.success(userService.getSuperior(account));
    }
    
    @GetMapping("/account/{account}/super-superior")
    @Operation(summary = "获取上上级用户")
    public Result<User> getSuperSuperior(@Parameter(description = "用户账号") @PathVariable String account) {
        return Result.success(userService.getSuperSuperior(account));
    }
    
    @GetMapping("/level/user/{userLevel}")
    @Operation(summary = "根据用户等级查询用户列表")
    public Result<List<User>> getUsersByUserLevel(@Parameter(description = "用户等级") @PathVariable Integer userLevel) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserLevel, userLevel);
        return Result.success(userService.list(wrapper));
    }
    
    @GetMapping("/level/role/{roleLevel}")
    @Operation(summary = "根据角色等级查询用户列表")
    public Result<List<User>> getUsersByRoleLevel(@Parameter(description = "角色等级") @PathVariable Integer roleLevel) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getRoleLevel, roleLevel);
        return Result.success(userService.list(wrapper));
    }
    
    @PutMapping("/account/{account}/user-level/{userLevel}")
    @Operation(summary = "更新用户等级")
    public Result<Boolean> updateUserLevel(
            @Parameter(description = "用户账号") @PathVariable String account,
            @Parameter(description = "用户等级") @PathVariable @Min(value = 1, message = "用户等级最小为1") @Max(value = 3, message = "用户等级最大为3") Integer userLevel) {
        User user = userService.getUserByAccount(account);
        if (user == null) {
            return Result.error("用户不存在");
        }   
        user.setUserLevel(userLevel);
        return Result.success(userService.updateById(user));
    }
    
    @PutMapping("/account/{account}/role-level/{roleLevel}")
    @Operation(summary = "更新角色等级")
    public Result<Boolean> updateRoleLevel(
            @Parameter(description = "用户账号") @PathVariable String account,
            @Parameter(description = "角色等级") @PathVariable @Min(value = 1, message = "角色等级最小为1") @Max(value = 3, message = "角色等级最大为3") Integer roleLevel) {
        User user = userService.getUserByAccount(account);
        if (user == null) {
            return Result.error("用户不存在");
        }
        user.setRoleLevel(roleLevel);
        return Result.success(userService.updateById(user));
    }
    
    @GetMapping("/level/stats")
    @Operation(summary = "获取用户等级和角色等级统计信息")
    public Result<Map<String, Object>> getLevelStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 统计用户等级分布
        List<Map<String, Object>> userLevelStats = userService.list().stream()
            .collect(Collectors.groupingBy(User::getUserLevel))
            .entrySet().stream()
            .map(entry -> {
                Map<String, Object> levelStat = new HashMap<>();
                levelStat.put("level", entry.getKey());
                levelStat.put("count", entry.getValue().size());
                return levelStat;
            })
            .collect(Collectors.toList());
        
        // 统计角色等级分布
        List<Map<String, Object>> roleLevelStats = userService.list().stream()
            .collect(Collectors.groupingBy(User::getRoleLevel))
            .entrySet().stream()
            .map(entry -> {
                Map<String, Object> levelStat = new HashMap<>();
                levelStat.put("level", entry.getKey());
                levelStat.put("count", entry.getValue().size());
                return levelStat;
            })
            .collect(Collectors.toList());
        
        stats.put("userLevelStats", userLevelStats);
        stats.put("roleLevelStats", roleLevelStats);
        
        return Result.success(stats);
    }
    
    @PutMapping("/account/{account}")
    @Operation(summary = "更新用户信息", description = "根据账号更新用户信息")
    public Result<Boolean> updateUser(
            @Parameter(description = "用户账号") @PathVariable String account,
            @Parameter(description = "用户名") @RequestParam(required = false) String username,
            @Parameter(description = "密码") @RequestParam(required = false) String password,
            @Parameter(description = "用户等级") @RequestParam(required = false) Integer userLevel,
            @Parameter(description = "角色等级") @RequestParam(required = false) Integer roleLevel) {
        User user = userService.getUserByAccount(account);
        if (user == null) {
            return Result.error("用户不存在");
        }
        if (username != null) user.setUsername(username);
        if (password != null) user.setPassword(EncryptUtil.encrypt(password));
        if (userLevel != null) user.setUserLevel(userLevel);
        if (roleLevel != null) user.setRoleLevel(roleLevel);
        return Result.success(userService.updateById(user));
    }
    
    @DeleteMapping("/account/{account}")
    @Operation(summary = "删除用户", description = "根据账号删除用户")
    public Result<Boolean> deleteUser(@Parameter(description = "用户账号") @PathVariable String account) {
        User user = userService.getUserByAccount(account);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(userService.removeById(user.getId()));
    }
    
    @GetMapping("/password/{account}")
    @Operation(summary = "获取用户真实密码")
    public Result<String> getRealPassword(@Parameter(description = "用户账号") @PathVariable String account) {
        return Result.success(userService.getRealPasswordByAccount(account));
    }
} 