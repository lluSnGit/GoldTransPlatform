package com.mall.controller;

import com.mall.common.Result;
import com.mall.dto.PriceQuoteDTO;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/price-quotes")
@Tag(name = "报价管理", description = "报价相关接口")
public class PriceQuoteController {
    
    /**
     * 创建报价
     */
    @PostMapping("/{orderId}")
    public Result<Void> createQuote(
            @PathVariable Long orderId,
            @RequestBody @Valid PriceQuoteDTO dto) {
        // TODO: 实现创建报价逻辑
        return Result.success();
    }
    
    /**
     * 确认报价
     */
    @PutMapping("/{orderId}/confirm")
    public Result<Void> confirmQuote(@PathVariable Long orderId) {
        // TODO: 实现确认报价逻辑
        return Result.success();
    }
    
    /**
     * 拒绝报价
     */
    @PutMapping("/{orderId}/reject")
    public Result<Void> rejectQuote(
            @PathVariable Long orderId,
            @RequestParam String reason) {
        // TODO: 实现拒绝报价逻辑
        return Result.success();
    }
} 