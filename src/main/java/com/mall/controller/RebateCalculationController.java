package com.mall.controller;

import com.mall.common.Result;
import com.mall.dto.RebateCalculationDTO;
import com.mall.service.RebateCalculationService;
import com.mall.vo.RebateCalculationVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rebate-calculation")
@Tag(name = "返点计算管理", description = "返点计算相关接口")
@Slf4j
public class RebateCalculationController {
    
    @Autowired
    private RebateCalculationService rebateCalculationService;
    
    @PostMapping("/calculate")
    @Operation(summary = "计算返点", description = "根据用户ID、贵金属类型和克重计算返点（不分配）")
    public Result<RebateCalculationVO> calculateRebate(@RequestBody RebateCalculationDTO dto) {
        try {
            RebateCalculationVO result = rebateCalculationService.calculateRebate(dto);
            return Result.success(result);
        } catch (Exception e) {
            log.error("计算返点失败", e);
            return Result.error("计算返点失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/calculate-and-distribute")
    @Operation(summary = "计算并分配返点", description = "根据用户ID、贵金属类型和克重计算返点并自动分配到上级和上上级用户账户")
    public Result<RebateCalculationVO> calculateAndDistributeRebate(@RequestBody RebateCalculationDTO dto) {
        try {
            RebateCalculationVO result = rebateCalculationService.calculateAndDistributeRebate(dto);
            return Result.success(result);
        } catch (Exception e) {
            log.error("计算并分配返点失败", e);
            return Result.error("计算并分配返点失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/calculate-simple")
    @Operation(summary = "简单计算返点", description = "通过URL参数计算返点（不分配）")
    public Result<RebateCalculationVO> calculateRebateSimple(
            @Parameter(description = "用户ID") @RequestParam String userId,
            @Parameter(description = "贵金属类型 0=黄金 1=铂金 2=钯金") @RequestParam Integer category,
            @Parameter(description = "克重") @RequestParam java.math.BigDecimal weight) {
        
        RebateCalculationDTO dto = new RebateCalculationDTO();
        dto.setUserId(userId);
        dto.setCategory(category);
        dto.setWeight(weight);
        
        try {
            RebateCalculationVO result = rebateCalculationService.calculateRebate(dto);
            return Result.success(result);
        } catch (Exception e) {
            log.error("计算返点失败", e);
            return Result.error("计算返点失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/test")
    @Operation(summary = "测试返点计算", description = "测试返点计算功能")
    public Result<String> test() {
        return Result.success("返点计算服务正常运行");
    }
} 