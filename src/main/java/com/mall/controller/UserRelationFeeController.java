package com.mall.controller;

import com.mall.common.Result;
import com.mall.dto.UserRelationFeeDTO;
import com.mall.service.UserRelationFeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/user-relation-fee")
@Tag(name = "用户关系费率接口", description = "用户关系费率相关接口")
@Slf4j
public class UserRelationFeeController {
    
    @Autowired
    private UserRelationFeeService userRelationFeeService;
    
    @GetMapping("/{account}")
    @Operation(summary = "获取用户关系费率信息")
    public Result<UserRelationFeeDTO> getUserRelationFee(
            @PathVariable @Parameter(description = "用户账号") String account) {
        return Result.success(userRelationFeeService.getUserRelationFee(account));
    }
}