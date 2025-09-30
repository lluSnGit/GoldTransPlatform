package com.mall.controller;

import com.mall.common.Result;
import com.mall.common.PageResult;
import com.mall.entity.AccountTransaction;
import com.mall.service.AccountTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/account-transactions")
@Tag(name = "账户交易记录", description = "账户交易记录相关接口")
public class AccountTransactionController {
    
    @Autowired
    private AccountTransactionService accountTransactionService;
    
    @PostMapping("/record")
    @Operation(summary = "记录账户交易")
    public Result<AccountTransaction> recordTransaction(
            @Parameter(description = "交易ID", required = true) @RequestParam String transactionId,
            @Parameter(description = "账户ID", required = true) @RequestParam String accountId,
            @Parameter(description = "用户ID", required = true) @RequestParam String userId,
            @Parameter(description = "交易类型(1:充值,2:提现,3:返点增加,4:返点减少)") @RequestParam Integer type,
            @Parameter(description = "交易金额") @RequestParam BigDecimal amount,
            @Parameter(description = "交易后余额") @RequestParam BigDecimal balance,
            @Parameter(description = "交易后返点") @RequestParam BigDecimal totalRebate,
            @Parameter(description = "交易状态(0:失败,1:成功)") @RequestParam Integer status,
            @Parameter(description = "备注") @RequestParam(required = false) String remark,
            @Parameter(description = "转账截图URL") @RequestParam(required = false) String transferImageUrl) {
        AccountTransaction transaction = accountTransactionService.recordTransaction(transactionId, accountId, userId, type, amount, balance, totalRebate, status, remark, transferImageUrl);
        return Result.success(transaction);
    }
    
    @GetMapping("/list")
    @Operation(summary = "查询账户交易记录")
    public Result<PageResult<AccountTransaction>> getTransactionList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "账户ID") @RequestParam(required = false) String accountId,
            @Parameter(description = "用户ID") @RequestParam(required = false) String userId,
            @Parameter(description = "交易类型(1:充值,2:提现,3:返点增加,4:返点减少)") @RequestParam(required = false) Integer type,
            @Parameter(description = "交易状态(0:失败,1:成功)") @RequestParam(required = false) Integer status,
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(accountTransactionService.getTransactionList(page, size, accountId, userId, type, status, startTime, endTime));
    }
    
    @GetMapping("/detail/{id}")
    @Operation(summary = "获取交易记录详情")
    public Result<AccountTransaction> getTransactionDetail(@Parameter(description = "交易记录ID") @PathVariable Long id) {
        return Result.success(accountTransactionService.getById(id));
    }
} 