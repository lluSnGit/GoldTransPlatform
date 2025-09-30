package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.Account;
import com.mall.entity.AccountTransaction;
import com.mall.service.AccountService;
import com.mall.vo.AccountVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "账户管理接口", description = "账户相关接口")
@Slf4j
public class AccountController {
    
    @Autowired
    private AccountService accountService;
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "通过用户ID获取账户信息")
    public Result<AccountVO> getAccountByUserId(@Parameter(description = "用户ID") @PathVariable String userId) {
        return Result.success(accountService.getAccountByUserId(userId));
    }
    
    @GetMapping("/account/{accountId}")
    @Operation(summary = "通过账户ID获取账户信息")
    public Result<AccountVO> getAccountByAccountId(@Parameter(description = "账户ID") @PathVariable String accountId) {
        return Result.success(accountService.getAccountByAccountId(accountId));
    }
    
    @PostMapping("/user/{userId}/deposit")
    @Operation(summary = "通过用户ID充值")
    public Result<Boolean> depositByUserId(
            @Parameter(description = "用户ID") @PathVariable String userId,
            @Parameter(description = "充值金额") @RequestParam BigDecimal amount,
            @Parameter(description = "交易类型(1:充值,2:提现,3:返点增加,4:返点减少,5:手续费)") @RequestParam Integer type) {
        return Result.success(accountService.deposit(userId, amount, type));
    }
    
    @PostMapping(value = "/account/{accountId}/deposit")
    @Operation(summary = "通过账户ID充值")
    public Result<AccountTransaction> depositByAccountId(
            @Parameter(description = "账户ID") @PathVariable String accountId,
            @Parameter(description = "充值金额", required = true) @RequestParam BigDecimal amount,
            @Parameter(description = "转账截图URL", required = false) @RequestParam(value = "transferImage", required = false) String transferImage,
            @Parameter(description = "备注信息", required = false) @RequestParam(required = false) String remark,
            @Parameter(description = "交易ID", required = true) @RequestParam String transactionId) {
        AccountTransaction transaction = accountService.depositByAccountId(accountId, amount, transferImage, remark, transactionId);
        return Result.success(transaction);
    }
    
    @PostMapping("/user/{userId}/withdraw")
    @Operation(summary = "通过用户ID提现")
    public Result<Boolean> withdrawByUserId(
            @Parameter(description = "用户ID") @PathVariable String userId,
            @Parameter(description = "提现金额") @RequestParam BigDecimal amount,
            @Parameter(description = "交易类型(1:充值,2:提现,3:返点增加,4:返点减少,5:手续费)") @RequestParam Integer type) {
        return Result.success(accountService.withdraw(userId, amount, type));
    }
    
    @PostMapping("/account/{accountId}/withdraw")
    @Operation(summary = "通过账户ID提现")
    public Result<AccountTransaction> withdrawByAccountId(
            @Parameter(description = "账户ID") @PathVariable String accountId,
            @Parameter(description = "提现金额") @RequestParam BigDecimal amount,
            @Parameter(description = "支付类型1：支付宝、2：微信、3：银行卡") @RequestParam Integer type,
            @Parameter(description = "交易ID", required = true) @RequestParam String transactionId) {
        AccountTransaction transaction = accountService.withdrawByAccountId(accountId, amount, type, transactionId);
        return Result.success(transaction);
    }
    
    @PostMapping("/user/{userId}/rebate")
    @Operation(summary = "通过用户ID添加返点")
    public Result<Boolean> addRebateByUserId(
            @Parameter(description = "用户ID") @PathVariable String userId,
            @Parameter(description = "返点金额") @RequestParam BigDecimal amount,
            @Parameter(description = "交易类型(1:充值,2:提现,3:返点增加,4:返点减少,5:手续费)") @RequestParam Integer type) {
        return Result.success(accountService.addRebate(userId, amount, type));
    }
    
    @PostMapping("/account/{accountId}/rebate")
    @Operation(summary = "通过账户ID添加返点")
    public Result<Boolean> addRebateByAccountId(
            @Parameter(description = "账户ID") @PathVariable String accountId,
            @Parameter(description = "返点金额") @RequestParam BigDecimal amount,
            @Parameter(description = "交易类型(1:充值,2:提现,3:返点减少,4:返点减少,5:手续费)") @RequestParam Integer type) {
        return Result.success(accountService.addRebateByAccountId(accountId, amount, type));
    }
    
    @GetMapping("/user/{userId}/balance")
    @Operation(summary = "通过用户ID查询余额")
    public Result<BigDecimal> getBalanceByUserId(@Parameter(description = "用户ID") @PathVariable String userId) {
        return Result.success(accountService.getBalance(userId));
    }
    
    @GetMapping("/account/{accountId}/balance")
    @Operation(summary = "通过账户ID查询余额")
    public Result<BigDecimal> getBalanceByAccountId(@Parameter(description = "账户ID") @PathVariable String accountId) {
        return Result.success(accountService.getBalanceByAccountId(accountId));
    }
    
    @GetMapping("/user/{userId}/rebate")
    @Operation(summary = "通过用户ID查询返点")
    public Result<BigDecimal> getTotalRebateByUserId(@Parameter(description = "用户ID") @PathVariable String userId) {
        return Result.success(accountService.getTotalRebate(userId));
    }
    
    @GetMapping("/account/{accountId}/rebate")
    @Operation(summary = "通过账户ID查询返点")
    public Result<BigDecimal> getTotalRebateByAccountId(@Parameter(description = "账户ID") @PathVariable String accountId) {
        return Result.success(accountService.getTotalRebateByAccountId(accountId));
    }
    
    @PostMapping("/user/{userId}/fee")
    @Operation(summary = "通过用户ID扣除手续费")
    public Result<Boolean> deductFeeByUserId(
            @Parameter(description = "用户ID") @PathVariable String userId,
            @Parameter(description = "手续费金额") @RequestParam BigDecimal amount) {
        return Result.success(accountService.deductFee(userId, amount));
    }
    
    @PostMapping("/account/{accountId}/fee")
    @Operation(summary = "通过账户ID扣除手续费")
    public Result<Boolean> deductFeeByAccountId(
            @Parameter(description = "账户ID") @PathVariable String accountId,
            @Parameter(description = "手续费金额") @RequestParam BigDecimal amount) {
        return Result.success(accountService.deductFeeByAccountId(accountId, amount));
    }
} 