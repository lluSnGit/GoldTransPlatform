package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.entity.AccountTransaction;
import com.mall.common.PageResult;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface AccountTransactionService extends IService<AccountTransaction> {
    
    /**
     * 记录账户交易
     * @return 创建的账户交易记录实体
     */
    AccountTransaction recordTransaction(String transactionId, String accountId, String userId, Integer type, BigDecimal amount, BigDecimal balance, BigDecimal totalRebate, Integer status, String remark, String transferImageUrl);
                             
    /**
     * 查询账户交易记录
     * @param page 页码
     * @param size 每页大小
     * @param accountId 账户ID
     * @param userId 用户ID
     * @param type 交易类型
     * @param status 交易状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 分页结果
     */
    PageResult<AccountTransaction> getTransactionList(Integer page, Integer size, String accountId, 
                                                    String userId, Integer type, Integer status,
                                                    LocalDateTime startTime, LocalDateTime endTime);
} 