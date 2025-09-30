package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.entity.Account;
import com.mall.entity.AccountTransaction;
import com.mall.mapper.AccountMapper;
import com.mall.service.AccountService;
import com.mall.service.AccountTransactionService;
import com.mall.service.MinioService;
import com.mall.vo.AccountVO;
import com.mall.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {
    
    @Autowired
    private AccountTransactionService accountTransactionService;
    
    @Autowired
    private MinioService minioService;
    
    @Override
    public AccountVO getAccountByUserId(String userId) {
        Account account = getByUserId(userId);
        if (account == null) {
            return null;
        }
        AccountVO vo = new AccountVO();
        BeanUtils.copyProperties(account, vo);
        return vo;
    }
    
    @Override
    @Transactional
    public boolean deposit(String userId, BigDecimal amount, Integer type) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("充值金额必须大于0");
        }
        
        Account account = getByAccountId(userId);
        if (account == null) {
            // 创建新账户
            account = new Account();
            account.setUserId(userId);
            account.setAccountId(userId); // 使用用户账号作为账户ID
            account.setBalance(amount);
            account.setTotalRebate(BigDecimal.ZERO);
            boolean saved = save(account);
            if (saved) {
                String transactionId = UUID.randomUUID().toString().replace("-", "");
                accountTransactionService.recordTransaction(transactionId, account.getAccountId(), userId, type, amount, amount, BigDecimal.ZERO, 1, "账户充值", null);
            }
            return saved;
        }
        
        // 更新余额
        account.setBalance(account.getBalance().add(amount));
        boolean updated = updateById(account);
        if (updated) {
            String transactionId = UUID.randomUUID().toString().replace("-", "");
            accountTransactionService.recordTransaction(transactionId, account.getAccountId(), userId, type, amount, account.getBalance(), account.getTotalRebate(), 1, "账户充值", null);
        }
        return updated;
    }
    
    @Override
    @Transactional
    public boolean withdraw(String userId, BigDecimal amount, Integer type) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("提现金额必须大于0");
        }
        
        Account account = getByAccountId(userId);
        if (account == null) {
            throw new BusinessException("账户不存在");
        }
        
        if (account.getBalance().compareTo(amount) < 0) {
            throw new BusinessException("余额不足");
        }
        
        // 更新余额
        account.setBalance(account.getBalance().subtract(amount));
        boolean updated = updateById(account);
        if (updated) {
            String transactionId = UUID.randomUUID().toString().replace("-", "");
            accountTransactionService.recordTransaction(transactionId, account.getAccountId(), userId, type, amount.negate(), account.getBalance(), account.getTotalRebate(), 1, "账户提现", null);
        }
        return updated;
    }
    
    @Override
    @Transactional
    public boolean addRebate(String userId, BigDecimal amount, Integer type) {
        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            return true; // 金额为0，直接返回成功
        }
        
        // 根据用户账号查找账户
        Account account = getByAccountId(userId);
        if (account == null) {
            // 创建新账户
            account = new Account();
            account.setUserId(userId);
            account.setAccountId(userId); // 使用用户账号作为账户ID
            account.setBalance(BigDecimal.ZERO);
            account.setTotalRebate(amount);
            boolean saved = save(account);
            if (saved) {
                String transactionId = UUID.randomUUID().toString().replace("-", "");
                accountTransactionService.recordTransaction(transactionId, account.getAccountId(), userId, type, amount, account.getBalance(), account.getTotalRebate(), 1, "返点增加", null);
            }
            return saved;
        }
        
        // 同时更新返点和余额
        account.setTotalRebate(account.getTotalRebate().add(amount));
        account.setBalance(account.getBalance().add(amount));
        boolean updated = updateById(account);
        if (updated) {
            String transactionId = UUID.randomUUID().toString().replace("-", "");
            accountTransactionService.recordTransaction(transactionId, account.getAccountId(), userId, type, amount, account.getBalance(), account.getTotalRebate(), 1, "返点增加", null);
        }
        return updated;
    }
    
    @Override
    public BigDecimal getBalance(String userId) {
        Account account = getByAccountId(userId);
        return account != null ? account.getBalance() : BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getTotalRebate(String userId) {
        Account account = getByAccountId(userId);
        return account != null ? account.getTotalRebate() : BigDecimal.ZERO;
    }
    
    @Override
    public Account getByUserId(String userId) {
        LambdaQueryWrapper<Account> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Account::getUserId, userId);
        return getOne(wrapper);
    }
    
    @Override
    public Account createOrUpdateAccount(String userId, String account) {
        Account existingAccount = getByUserId(userId);
        if (existingAccount == null) {
            existingAccount = new Account();
            existingAccount.setUserId(userId);
            existingAccount.setAccountId(account);
            save(existingAccount);
        }
        return existingAccount;
    }
    
    @Override
    public Account getByAccountId(String accountId) {
        LambdaQueryWrapper<Account> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Account::getAccountId, accountId);
        List<Account> accounts = list(wrapper);
        return accounts.isEmpty() ? null : accounts.get(0);
    }
    
    @Override
    public AccountVO getAccountByAccountId(String accountId) {
        Account account = getByAccountId(accountId);
        if (account == null) {
            return null;
        }
        AccountVO vo = new AccountVO();
        BeanUtils.copyProperties(account, vo);
        return vo;
    }
    
    @Override
    @Transactional
    public AccountTransaction depositByAccountId(String accountId, BigDecimal amount, String transferImage, String remark, String transactionId) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("充值金额必须大于0");
        }

        Account account = getByAccountId(accountId);
        if (account == null) {
            throw new BusinessException("账户不存在");
        }

        account.setBalance(account.getBalance().add(amount));
        boolean updated = updateById(account);
        if (updated) {
            // recordTransaction 现在返回 AccountTransaction 实体
            return accountTransactionService.recordTransaction(transactionId, accountId, account.getUserId(), 1, amount, account.getBalance(), account.getTotalRebate(), 1, remark != null ? remark : "账户充值", transferImage);
        } else {
             throw new BusinessException("账户余额更新失败"); // 更新失败抛出异常
        }
    }
    
    @Override
    @Transactional
    public AccountTransaction withdrawByAccountId(String accountId, BigDecimal amount, Integer type, String transactionId) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("提现金额必须大于0");
        }

        Account account = getByAccountId(accountId);
        if (account == null) {
            throw new BusinessException("账户不存在");
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new BusinessException("余额不足");
        }

        account.setBalance(account.getBalance().subtract(amount));
        boolean updated = updateById(account);
        if (updated) {
            // recordTransaction 现在返回 AccountTransaction 实体
            return accountTransactionService.recordTransaction(transactionId, accountId, account.getUserId(), type, amount.negate(), account.getBalance(), account.getTotalRebate(), 1, "账户提现", null);
        } else {
             throw new BusinessException("账户余额更新失败"); // 更新失败抛出异常
        }
    }
    
    @Override
    @Transactional
    public boolean addRebateByAccountId(String accountId, BigDecimal amount, Integer type) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("返点金额必须大于0");
        }
        
        Account account = getByAccountId(accountId);
        if (account == null) {
            throw new BusinessException("账户不存在");
        }
        
        // 同时更新返点和余额
        account.setTotalRebate(account.getTotalRebate().add(amount));
        account.setBalance(account.getBalance().add(amount));
        boolean updated = updateById(account);
        if (updated) {
            String transactionId = UUID.randomUUID().toString().replace("-", "");
            accountTransactionService.recordTransaction(transactionId, accountId, account.getUserId(), type, amount, account.getBalance(), account.getTotalRebate(), 1, "返点增加", null);
        }
        return updated;
    }
    
    @Override
    public BigDecimal getBalanceByAccountId(String accountId) {
        Account account = getByAccountId(accountId);
        return account != null ? account.getBalance() : BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getTotalRebateByAccountId(String accountId) {
        Account account = getByAccountId(accountId);
        return account != null ? account.getTotalRebate() : BigDecimal.ZERO;
    }
    
    @Override
    @Transactional
    public boolean deductFee(String userId, BigDecimal amount) {
        Account account = getByAccountId(userId);
        if (account == null) {
            return false;
        }
        
        // 检查余额是否足够
        if (account.getBalance().compareTo(amount) < 0) {
            return false;
        }
        
        // 更新余额
        account.setBalance(account.getBalance().subtract(amount));
        account.setUpdateTime(LocalDateTime.now());
        
        // 记录交易，手续费金额保持为正数
        String transactionId = UUID.randomUUID().toString().replace("-", "");
        accountTransactionService.recordTransaction(transactionId, account.getAccountId(), userId, 5, amount, account.getBalance(), account.getTotalRebate(), 1, "手续费", null);
        
        return updateById(account);
    }
    
    @Override
    @Transactional
    public boolean deductFeeByAccountId(String accountId, BigDecimal amount) {
        Account account = getByAccountId(accountId);
        if (account == null) {
            return false;
        }
        
        // 检查余额是否足够
        if (account.getBalance().compareTo(amount) < 0) {
            return false;
        }
        
        // 更新余额
        account.setBalance(account.getBalance().subtract(amount));
        account.setUpdateTime(LocalDateTime.now());
        
        // 记录交易，手续费金额保持为正数
        String transactionId = UUID.randomUUID().toString().replace("-", "");
        accountTransactionService.recordTransaction(transactionId, accountId, account.getUserId(), 5, amount, account.getBalance(), account.getTotalRebate(), 1, "手续费", null);
        
        return updateById(account);
    }
} 