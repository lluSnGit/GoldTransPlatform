package com.mall.controller;

import com.mall.service.UserLevelRebateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/user-level-rebates")
@Tag(name = "用户等级返点管理", description = "用户等级返点相关接口")
public class UserLevelRebateController {
    
    @Autowired
    private UserLevelRebateService userLevelRebateService;
    
    @PostMapping("/{userLevel}")
    @Operation(summary = "设置用户等级返点")
    public boolean setUserLevelRebate(@PathVariable Integer userLevel, @RequestParam BigDecimal rebateAmount) {
        return userLevelRebateService.setUserLevelRebate(userLevel, rebateAmount);
    }
    
    @GetMapping("/{userLevel}")
    @Operation(summary = "获取用户等级返点")
    public BigDecimal getUserLevelRebate(@PathVariable Integer userLevel) {
        return userLevelRebateService.getUserLevelRebate(userLevel);
    }
} 