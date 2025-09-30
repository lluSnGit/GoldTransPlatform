package com.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.common.Result;
import com.mall.dto.AdminDTO;
import com.mall.dto.LoginResult;
import com.mall.dto.UserAddressDTO;
import com.mall.entity.GoldRecycleOrder;
import com.mall.entity.User;
import com.mall.entity.UserRelation;
import com.mall.service.AdminService;
import com.mall.service.GoldRecycleOrderService;
import com.mall.service.UserAddressService;
import com.mall.service.UserRelationService;
import com.mall.service.UserService;
import com.mall.vo.GoldRecycleOrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import org.hibernate.validator.constraints.Length;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "管理员接口", description = "管理员相关接口")
@Slf4j
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRelationService userRelationService;
    
    @Autowired
    private GoldRecycleOrderService goldRecycleOrderService;
    
    @Autowired
    private UserAddressService userAddressService;
    
    @PostMapping("/login")
    @Operation(summary = "管理员登录")
    public Result<LoginResult> login(
            @RequestParam String username,
            @RequestParam String password) {
        LoginResult loginResult = adminService.login(username, password);
        if (loginResult != null) {
            return Result.success(loginResult);
        }
        return Result.error("用户名或密码错误");
    }
    
    @PostMapping("/register")
    @Operation(summary = "创建管理员")
    public Result<LoginResult> createAdmin(@RequestBody @Validated AdminDTO adminDTO) {
        LoginResult loginResult = adminService.createAdmin(adminDTO);
        if (loginResult != null) {
            return Result.success(loginResult);
        }
        return Result.error("创建管理员失败，可能用户名已存在");
    }
    
    @PutMapping("/{adminId}/status")
    @Operation(summary = "更新管理员状态")
    public Result<Boolean> updateStatus(
            @PathVariable String adminId,
            @RequestParam Integer status) {
        return Result.success(adminService.updateStatus(adminId, status));
    }
    
    @DeleteMapping("/{adminId}")
    @Operation(summary = "删除管理员")
    public Result<Boolean> deleteAdmin(@PathVariable String adminId) {
        return Result.success(adminService.deleteAdmin(adminId));
    }
    
    @Operation(summary = "获取用户列表")
    @GetMapping("/users")
    public Result<List<User>> getUserList() {
        return Result.success(userService.list());
    }
    
    @Operation(summary = "获取用户详情")
    @GetMapping("/users/{account}")
    public Result<User> getUserDetail(@Parameter(description = "用户账号") @PathVariable String account) {
        User user = userService.getUserByAccount(account);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(user);
    }
    
    @Operation(summary = "创建用户")
    @PostMapping("/users")
    public Result<User> createUser(
        @Parameter(description = "账号") @RequestParam @NotBlank(message = "账号不能为空") @Pattern(regexp = "^[a-zA-Z0-9_]{4,16}$", message = "账号必须是4-16位字母、数字或下划线") String account,
        @Parameter(description = "密码") @RequestParam @NotBlank(message = "密码不能为空") @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$", message = "密码必须包含大小写字母和数字，且长度至少8位") String password,
        @Parameter(description = "用户名") @RequestParam @NotBlank(message = "用户名不能为空") @Length(min = 2, max = 20, message = "用户名长度必须在2-20个字符之间") String username,
        @Parameter(description = "用户等级") @RequestParam(defaultValue = "0") @Min(0) @Max(3) Integer userLevel,
        @Parameter(description = "角色等级") @RequestParam(defaultValue = "0") @Min(0) @Max(2) Integer roleLevel
    ) {
        User user = new User();
        user.setAccount(account);
        user.setPassword(password);
        user.setUsername(username);
        user.setUserLevel(userLevel);
        user.setRoleLevel(roleLevel);
        return Result.success(userService.createUser(user));
    }
    
    @Operation(summary = "修改用户")
    @PutMapping("/users/{account}")
    public Result<User> updateUser(
        @Parameter(description = "用户账号") @PathVariable String account,
        @Parameter(description = "用户名") @RequestParam(required = false) @Length(min = 2, max = 20, message = "用户名长度必须在2-20个字符之间") String username,
        @Parameter(description = "用户等级") @RequestParam(required = false) @Min(0) @Max(3) Integer userLevel,
        @Parameter(description = "角色等级") @RequestParam(required = false) @Min(0) @Max(2) Integer roleLevel
    ) {
        User user = userService.getUserByAccount(account);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        if (username != null) {
            user.setUsername(username);
        }
        if (userLevel != null) {
            user.setUserLevel(userLevel);
        }
        if (roleLevel != null) {
            user.setRoleLevel(roleLevel);
        }
        
        userService.updateById(user);
        return Result.success(user);
    }
    
    @Operation(summary = "删除用户")
    @DeleteMapping("/users/{account}")
    public Result<Void> deleteUser(@Parameter(description = "用户账号") @PathVariable String account) {
        User user = userService.getUserByAccount(account);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        // 删除用户关系
        List<UserRelation> relations = userRelationService.getRelationsByAccount(account);
        for (UserRelation relation : relations) {
            userRelationService.removeById(relation.getId());
        }
        
        // 删除用户
        userService.removeById(user.getUserId());
        return Result.success();
    }
    
    @Operation(summary = "修改用户状态")
    @PutMapping("/users/{account}/status")
    public Result<User> updateUserStatus(
        @Parameter(description = "用户账号") @PathVariable String account,
        @Parameter(description = "用户等级") @RequestParam @Min(0) @Max(3) Integer userLevel
    ) {
        User user = userService.getUserByAccount(account);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        user.setUserLevel(userLevel);
        userService.updateById(user);
        return Result.success(user);
    }
    
    @Operation(summary = "获取用户关系树")
    @GetMapping("/users/{account}/relation-tree")
    public Result<Map<String, Object>> getUserRelationTree(@Parameter(description = "用户账号") @PathVariable String account) {
        Map<String, Object> tree = new HashMap<>();

        // 获取用户信息
        User user = userService.getUserByAccount(account);
        if (user == null) {
            return Result.error("用户不存在");
        }
        tree.put("user", user);

        // 获取上级
        User parent = null;
        List<UserRelation> relations = userRelationService.getRelationsByAccount(account);
        if (!relations.isEmpty()) {
            String parentId = relations.get(0).getParentId();
            if (parentId != null) {
                parent = userService.getUserByAccount(parentId);
            }
        }
        tree.put("parent", parent);

        // 获取上上级
        String grandParentId = userRelationService.getGrandParentIdByAccount(account);
        User grandParent = null;
        if (grandParentId != null) {
            grandParent = userService.getUserByAccount(grandParentId);
        }
        tree.put("grandParent", grandParent);

        // 获取直接下级
        List<UserRelation> childrenRelations = userRelationService.getChildRelationsByAccount(account);
        List<User> children = childrenRelations.stream()
                .map(r -> userService.getUserByAccount(r.getUserId()))
                .collect(Collectors.toList());
        tree.put("children", children);

        // 获取下下级
        List<UserRelation> subChildrenRelations = userRelationService.getSubChildRelationsByAccount(account);
        List<User> subChildren = subChildrenRelations.stream()
                .map(r -> userService.getUserByAccount(r.getUserId()))
                .collect(Collectors.toList());
        tree.put("subChildren", subChildren);

        return Result.success(tree);
    }

    // 回收订单管理接口
    @GetMapping("/orders/{orderId}")
    @Operation(summary = "获取订单详情")
    public Result<GoldRecycleOrderVO> getOrderDetail(
            @Parameter(description = "订单ID") @PathVariable String orderId) {
        return Result.success(goldRecycleOrderService.getOrderDetail(orderId));
    }

    @GetMapping("/orders/list")
    @Operation(summary = "分页查询所有订单")
    public Result<IPage<GoldRecycleOrderVO>> listAllOrders(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "预估价格") @RequestParam(required = false) BigDecimal estimatedPrice) {
        Page<GoldRecycleOrder> page = new Page<>(pageNum, pageSize);
        return Result.success(goldRecycleOrderService.listAllOrders(page, estimatedPrice));
    }

    @PutMapping("/orders/{orderId}/status")
    @Operation(summary = "更新订单状态")
    public Result<Boolean> updateOrderStatus(
            @Parameter(description = "订单ID") @PathVariable String orderId,
            @Parameter(description = "订单状态") @RequestParam Integer status) {
        return Result.success(goldRecycleOrderService.updateOrderStatus(orderId, status));
    }

    @PutMapping("/orders/{orderId}/estimated-price")
    @Operation(summary = "设置预估价格")
    public Result<Boolean> setEstimatedPrice(
            @Parameter(description = "订单ID") @PathVariable String orderId,
            @Parameter(description = "预估价格") @RequestParam BigDecimal estimatedPrice) {
        return Result.success(goldRecycleOrderService.setEstimatedPrice(orderId, estimatedPrice));
    }

    @PutMapping("/orders/{orderId}/final-price")
    @Operation(summary = "设置最终价格")
    public Result<Boolean> setFinalPrice(
            @Parameter(description = "订单ID") @PathVariable String orderId,
            @Parameter(description = "最终价格") @RequestParam BigDecimal finalPrice) {
        return Result.success(goldRecycleOrderService.setFinalPrice(orderId, finalPrice));
    }

    @PutMapping("/orders/{orderId}/inspection-result")
    @Operation(summary = "更新检测结果")
    public Result<Boolean> updateInspectionResult(
            @Parameter(description = "订单ID") @PathVariable String orderId,
            @Parameter(description = "检测结果") @RequestParam String result) {
        return Result.success(goldRecycleOrderService.updateInspectionResult(orderId, result));
    }

    // 地址管理接口
    @GetMapping("/address/list")
    @Operation(summary = "获取用户地址列表")
    public Result<List<UserAddressDTO>> getAddressList(@RequestParam String account) {
        return Result.success(userAddressService.getAddressList(account));
    }

    @GetMapping("/address/detail")
    @Operation(summary = "获取地址详情")
    public Result<UserAddressDTO> getAddressDetail(@RequestParam String addressId) {
        return Result.success(userAddressService.getAddressDetail(addressId));
    }

    @PostMapping("/address/add")
    @Operation(summary = "新增收货地址")
    public Result<UserAddressDTO> addAddress(@ModelAttribute @Valid UserAddressDTO dto) {
        return Result.success(userAddressService.addAddress(dto));
    }

    @PutMapping("/address/update")
    @Operation(summary = "更新收货地址")
    public Result<Boolean> updateAddress(@ModelAttribute @Valid UserAddressDTO dto) {
        return Result.success(userAddressService.updateAddress(dto));
    }

    @DeleteMapping("/address/delete")
    @Operation(summary = "删除收货地址")
    public Result<Boolean> deleteAddress(@RequestParam String addressId) {
        return Result.success(userAddressService.deleteAddress(addressId));
    }

    // 用户关系管理接口
    @PostMapping("/user-relation")
    @Operation(summary = "创建用户关系")
    public Result<UserRelation> createRelation(
            @RequestParam String account,
            @RequestParam String parentAccount) {
        UserRelation relation = new UserRelation();
        relation.setUserId(account);
        relation.setParentId(parentAccount);
        userRelationService.save(relation);
        return Result.success(relation);
    }

    @GetMapping("/user-relation/{account}")
    @Operation(summary = "获取用户关系")
    public Result<List<UserRelation>> getRelationsByAccount(
            @Parameter(description = "用户账号") @PathVariable String account) {
        return Result.success(userRelationService.getRelationsByAccount(account));
    }

    @GetMapping("/user-relation/{account}/children")
    @Operation(summary = "获取用户直接下级")
    public Result<List<UserRelation>> getChildRelationsByAccount(
            @Parameter(description = "用户账号") @PathVariable String account) {
        return Result.success(userRelationService.getChildRelationsByAccount(account));
    }

    @GetMapping("/user-relation/{account}/sub-children")
    @Operation(summary = "获取用户下下级")
    public Result<List<UserRelation>> getSubChildRelationsByAccount(
            @Parameter(description = "用户账号") @PathVariable String account) {
        return Result.success(userRelationService.getSubChildRelationsByAccount(account));
    }

    @GetMapping("/user-relation/{account}/grand-parent")
    @Operation(summary = "获取用户上上级")
    public Result<String> getGrandParentIdByAccount(
            @Parameter(description = "用户账号") @PathVariable String account) {
        return Result.success(userRelationService.getGrandParentIdByAccount(account));
    }

    @DeleteMapping("/user-relation/{id}")
    @Operation(summary = "删除用户关系")
    public Result<Void> deleteRelation(@Parameter(description = "关系ID") @PathVariable Long id) {
        userRelationService.removeById(id);
        return Result.success();
    }

    @GetMapping("/list")
    @Operation(summary = "获取所有管理员列表")
    public Result<List<AdminDTO>> getAllAdmins() {
        return Result.success(adminService.getAllAdmins());
    }
} 