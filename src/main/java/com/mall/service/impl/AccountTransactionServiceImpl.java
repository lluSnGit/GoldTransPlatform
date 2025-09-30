package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.entity.AccountTransaction;
import com.mall.mapper.AccountTransactionMapper;
import com.mall.service.AccountTransactionService;
import com.mall.common.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Service
public class AccountTransactionServiceImpl extends ServiceImpl<AccountTransactionMapper, AccountTransaction> implements AccountTransactionService {
    
    @Override
    @Transactional
    public AccountTransaction recordTransaction(String transactionId, String accountId, String userId, Integer type, BigDecimal amount, BigDecimal balance, BigDecimal totalRebate, Integer status, String remark, String transferImageUrl) {
        AccountTransaction transaction = new AccountTransaction();
        transaction.setTransactionId(transactionId);
        transaction.setAccountId(accountId);
        transaction.setUserId(userId);
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setBalance(balance);
        transaction.setTotalRebate(totalRebate);
        transaction.setStatus(status);
        transaction.setRemark(remark);
        transaction.setTransferImageUrl(transferImageUrl);
        transaction.setCreateTime(LocalDateTime.now());
        transaction.setUpdateTime(LocalDateTime.now());

        save(transaction);
        return transaction;
    }
    
    @Override
    public PageResult<AccountTransaction> getTransactionList(Integer page, Integer size, String accountId,
                                                          String userId, Integer type, Integer status,
                                                          LocalDateTime startTime, LocalDateTime endTime) {
        // 构建查询条件
        LambdaQueryWrapper<AccountTransaction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(accountId != null, AccountTransaction::getAccountId, accountId)
               .eq(userId != null, AccountTransaction::getUserId, userId)
               .eq(type != null, AccountTransaction::getType, type)
               .eq(status != null, AccountTransaction::getStatus, status)
               .ge(startTime != null, AccountTransaction::getCreateTime, startTime)
               .le(endTime != null, AccountTransaction::getCreateTime, endTime)
               .orderByDesc(AccountTransaction::getCreateTime);
        
        // 执行分页查询
        Page<AccountTransaction> pageResult = page(new Page<>(page, size), wrapper);
        
        // 构建返回结果
        return new PageResult<>(pageResult.getRecords(), pageResult.getTotal(), size, page);
    }
} 