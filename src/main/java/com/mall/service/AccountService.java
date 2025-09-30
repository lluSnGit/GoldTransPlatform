package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.entity.Account;
import com.mall.entity.AccountTransaction;
import com.mall.vo.AccountVO;
import java.math.BigDecimal;
import org.springframework.web.multipart.MultipartFile;

public interface AccountService extends IService<Account> {
    
    /**
     * 创建或更新账户
     * @param userId 用户随机ID
     * @param account 账号
     * @return 账户信息
     */
    Account createOrUpdateAccount(String userId, String account);
    
    /**
     * 根据账户ID查询账户
     */
    Account getByAccountId(String accountId);
    
    /**
     * 根据用户ID获取账户
     * @param userId 用户随机ID
     * @return 账户信息
     */
    Account getByUserId(String userId);
    
    /**
     * 根据用户ID获取账户信息
     */
    AccountVO getAccountByUserId(String userId);
    
    /**
     * 根据账户ID获取账户信息
     */
    AccountVO getAccountByAccountId(String accountId);
    
    /**
     * 通过用户ID充值
     */
    boolean deposit(String userId, BigDecimal amount, Integer type);
    
    /**
     * 通过账户ID充值
     * @return 对应的交易记录实体
     */
    AccountTransaction depositByAccountId(String accountId, BigDecimal amount, String transferImage, String remark, String transactionId);
    
    /**
     * 通过用户ID提现
     */
    boolean withdraw(String userId, BigDecimal amount, Integer type);
    
    /**
     * 通过账户ID提现
     * @return 对应的交易记录实体
     */
    AccountTransaction withdrawByAccountId(String accountId, BigDecimal amount, Integer type, String transactionId);
    
    /**
     * 通过用户ID添加返点
     */
    boolean addRebate(String userId, BigDecimal amount, Integer type);
    
    /**
     * 通过账户ID添加返点
     */
    boolean addRebateByAccountId(String accountId, BigDecimal amount, Integer type);
    
    /**
     * 通过用户ID查询余额
     */
    BigDecimal getBalance(String userId);
    
    /**
     * 通过账户ID查询余额
     */
    BigDecimal getBalanceByAccountId(String accountId);
    
    /**
     * 通过用户ID查询返点
     */
    BigDecimal getTotalRebate(String userId);
    
    /**
     * 通过账户ID查询返点
     */
    BigDecimal getTotalRebateByAccountId(String accountId);
    
    /**
     * 通过用户ID扣除手续费
     */
    boolean deductFee(String userId, BigDecimal amount);
    
    /**
     * 通过账户ID扣除手续费
     */
    boolean deductFeeByAccountId(String accountId, BigDecimal amount);
} 