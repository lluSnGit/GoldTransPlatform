package com.mall.controller;

import com.mall.common.api.CommonResult;
import com.mall.service.PlatformFeeService;
import com.mall.vo.PlatformFeeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Tag(name = "用户等级相关接口", description = "获取用户等级信息和手续费率接口")
public class UserLevelController {

    @Autowired
    private PlatformFeeService platformFeeService;

    @GetMapping("/level")
    @Operation(summary = "获取所有用户等级手续费信息")
    public CommonResult<List<PlatformFeeVO>> getAllUserLevels() {
        return CommonResult.success(platformFeeService.getAllUserLevels());
    }
} 