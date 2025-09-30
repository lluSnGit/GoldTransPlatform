package com.mall.controller;

import com.mall.service.PlatformFeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import com.mall.vo.PlatformFeeVO;
import com.mall.entity.PlatformFee;
import com.mall.entity.User;
import com.mall.service.UserService;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/platform-fees")
@Tag(name = "平台手续费管理", description = "平台手续费相关接口")
public class PlatformFeeController {
    
    @Autowired
    private PlatformFeeService platformFeeService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    @Operation(summary = "设置平台手续费", description = "根据用户等级和类别设置或更新平台手续费比例")
    public PlatformFeeVO setPlatformFee(@RequestBody PlatformFeeVO feeVO) {
        platformFeeService.setPlatformFee(
            feeVO.getLevel(),
            feeVO.getLevelName(),
            feeVO.getFeeRate(),
            feeVO.getCategory()
        );
        return feeVO;
    }
    
    @GetMapping
    @Operation(summary = "获取所有用户等级手续费信息", description = "获取所有已设置的用户等级对应的平台手续费比例")
    public List<PlatformFeeVO> getAllUserLevels() {
        return platformFeeService.getAllUserLevels();
    }

    @GetMapping("/{userLevel}")
    @Operation(summary = "根据用户等级获取手续费信息", description = "根据指定的用户等级获取对应的平台手续费规则")
    public PlatformFee getPlatformFeeByUserLevel(
            @Parameter(description = "用户等级") @PathVariable Integer userLevel,
            @Parameter(description = "类别 0=黄金 1=铂金 2=钯金") @RequestParam(required = false, defaultValue = "0") Integer category) {
        return platformFeeService.getByUserLevelAndCategory(userLevel, category);
    }

    @GetMapping("/by-account/{account}")
    @Operation(summary = "根据用户账号获取手续费信息", description = "根据账号查找用户等级，再查找对应等级的所有手续费信息")
    public List<PlatformFeeVO> getPlatformFeeByAccount(@Parameter(description = "用户账号") @PathVariable String account) {
        User user = userService.getUserByAccount(account);
        if (user == null) {
            return new ArrayList<>();
        }
        List<PlatformFee> feeList = platformFeeService.listByUserLevel(user.getUserLevel());
        List<PlatformFeeVO> voList = new ArrayList<>();
        for (PlatformFee fee : feeList) {
            PlatformFeeVO vo = new PlatformFeeVO();
            vo.setAccount(account);
            vo.setFeeRate(fee.getFeeRate());
            vo.setLevel(fee.getUserLevel());
            vo.setLevelName(fee.getLevelName());
            vo.setCategory(fee.getCategory());
            voList.add(vo);
        }
        return voList;
    }
} 