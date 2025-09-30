package com.mall.controller;

import com.mall.common.Result;
import com.mall.common.PageResult;
import com.mall.entity.AccountTransactionReview;
import com.mall.service.AccountTransactionReviewService;
import com.mall.dto.AccountTransactionReviewDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.mall.common.exception.BusinessException;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/account-review")
@Tag(name = "账户交易审核接口", description = "账户交易审核相关接口")
@Slf4j
public class AccountTransactionReviewController {

    @Autowired
    private AccountTransactionReviewService accountTransactionReviewService;

    @PostMapping("/submit")
    @Operation(summary = "提交账户交易审核请求")
    public Result<AccountTransactionReview> submitReviewRequest(
            @Parameter(description = "审核请求信息", required = true) 
            @RequestBody AccountTransactionReviewDTO reviewDTO) {
        try {
            return Result.success(accountTransactionReviewService.submitReviewRequest(reviewDTO));
        } catch (BusinessException e) {
            // 捕获BusinessException，并返回自定义错误消息
            return Result.error("当前已存在待审核申请，请耐心等待该笔申请结束后才能再次发起申请");
        }
    }

    @GetMapping("/list")
    @Operation(summary = "获取账户交易审核列表", description = "管理员获取账户交易审核记录列表")
    public Result<PageResult<AccountTransactionReview>> getReviewList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "交易类型(1:充值,2:提现)") @RequestParam(required = false) Integer transactionType,
            @Parameter(description = "审核状态(0:待审核,1:已通过,2:已拒绝)") @RequestParam(required = false) Integer reviewStatus) {
        return Result.success(accountTransactionReviewService.getReviewList(page, size, transactionType, reviewStatus));
    }

    @PostMapping("/review")
    @Operation(summary = "审核账户交易请求", description = "管理员审核用户的充值或提现请求")
    public Result<Boolean> reviewTransaction(
            @Parameter(description = "账户ID", required = true) @RequestParam String accountId,
            @Parameter(description = "交易ID", required = true) @RequestParam String transactionId,
            @Parameter(description = "审核结果 (1:通过, 2:拒绝)", required = true) @RequestParam Integer reviewStatus) {
        return Result.success(accountTransactionReviewService.reviewTransaction(accountId, transactionId, reviewStatus));
    }
} 