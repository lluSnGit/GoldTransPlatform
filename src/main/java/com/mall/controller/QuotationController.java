package com.mall.controller;

import com.mall.dto.QuotationDTO;
import com.mall.service.QuotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.Valid;
import com.mall.common.Result;

@RestController
@RequestMapping("/api/quotations")
@Tag(name = "报价管理接口")
public class QuotationController {

    @Autowired
    private QuotationService quotationService;

    @PostMapping
    @Operation(summary = "创建报价")
    public Result<QuotationDTO> createQuotation(
            @Parameter(description = "报价信息", schema = @Schema(implementation = QuotationDTO.class)) 
            @Valid @RequestBody QuotationDTO quotationDTO) {
        return Result.success(quotationService.createQuotation(quotationDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新报价")
    public Result<QuotationDTO> updateQuotation(
            @Parameter(description = "报价ID") @PathVariable String id,
            @Parameter(description = "报价信息", schema = @Schema(implementation = QuotationDTO.class)) 
            @Valid @RequestBody QuotationDTO quotationDTO) {
        quotationDTO.setId(id);
        return Result.success(quotationService.updateQuotation(quotationDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取报价详情")
    public Result<QuotationDTO> getQuotationById(
            @Parameter(description = "报价ID") @PathVariable String id) {
        return Result.success(quotationService.getQuotationById(id));
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "根据订单ID获取报价")
    public Result<List<QuotationDTO>> getQuotationByOrderId(@PathVariable String orderId) {
        List<QuotationDTO> quotations = quotationService.getQuotationByOrderId(orderId);
        return Result.success(quotations);
    }

    @GetMapping
    @Operation(summary = "获取所有报价列表")
    public Result<List<QuotationDTO>> getAllQuotations() {
        return Result.success(quotationService.getAllQuotations());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除报价")
    public Result<Void> deleteQuotation(
            @Parameter(description = "报价ID") @PathVariable String id) {
        quotationService.deleteQuotation(id);
        return Result.success();
    }

    @PostMapping("/batch")
    @Operation(summary = "批量创建报价")
    public Result<List<QuotationDTO>> batchCreateQuotations(
            @Parameter(description = "报价信息列表", schema = @Schema(implementation = QuotationDTO.class)) 
            @Valid @RequestBody List<QuotationDTO> quotationDTOs) {
        return Result.success(quotationService.batchCreateQuotations(quotationDTOs));
    }

    @GetMapping("/type/{goldType}")
    @Operation(summary = "根据黄金类型查询报价")
    public Result<List<QuotationDTO>> getQuotationsByGoldType(
            @Parameter(description = "黄金类型") @PathVariable String goldType) {
        return Result.success(quotationService.getQuotationsByGoldType(goldType));
    }

    @GetMapping("/{id}/total-price")
    @Operation(summary = "计算报价总价")
    public Result<BigDecimal> calculateTotalPrice(
            @Parameter(description = "报价ID") @PathVariable String id) {
        return Result.success(quotationService.calculateTotalPrice(id));
    }
} 