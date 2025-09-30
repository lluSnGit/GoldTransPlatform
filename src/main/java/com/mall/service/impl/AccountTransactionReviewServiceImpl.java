package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.entity.Account;
import com.mall.entity.AccountTransactionReview;
import com.mall.mapper.AccountTransactionReviewMapper;
import com.mall.service.AccountTransactionReviewService;
import com.mall.service.AccountService;
import com.mall.service.MinioService;
import com.mall.dto.AccountTransactionReviewDTO;
import com.mall.common.PageResult;
import com.mall.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccountTransactionReviewServiceImpl extends ServiceImpl<AccountTransactionReviewMapper, AccountTransactionReview> implements AccountTransactionReviewService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MinioService minioService;

    @Override
    @Transactional
    public AccountTransactionReview submitReviewRequest(AccountTransactionReviewDTO reviewDTO) {
        // 验证账户是否存在
        Account account = accountService.getByAccountId(reviewDTO.getAccountId());
        if (account == null) {
            throw new BusinessException("账户不存在");
        }

        // 检查该账户是否已有任何未被"删除"的交易审核记录
        long existingRecordsCount = count(Wrappers.<AccountTransactionReview>lambdaQuery()
                .eq(AccountTransactionReview::getAccountId, reviewDTO.getAccountId())
                .eq(AccountTransactionReview::getIsDeleted, 0)); // 将getDelete改为getIsDeleted
        if (existingRecordsCount > 0) {
            throw new BusinessException("该账户已有未处理的交易审核记录，无法再次提交申请");
        }

        String transactionId = UUID.randomUUID().toString().replace("-", "");

        // 创建审核记录实体
        AccountTransactionReview review = new AccountTransactionReview();
        BeanUtils.copyProperties(reviewDTO, review);
        review.setTransactionId(transactionId);
        review.setUserId(account.getUserId()); // 从账户信息中获取用户ID
        review.setReviewStatus(0); // 默认待审核
        review.setTransferImageUrl(reviewDTO.getTransferImage()); // 直接使用传入的URL
        review.setCreateTime(LocalDateTime.now());
        review.setUpdateTime(LocalDateTime.now());

        save(review);
        return review;
    }

    @Override
    public PageResult<AccountTransactionReview> getReviewList(Integer page, Integer size, Integer transactionType, Integer reviewStatus) {
        LambdaQueryWrapper<AccountTransactionReview> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(transactionType != null, AccountTransactionReview::getTransactionType, transactionType)
               .eq(reviewStatus != null, AccountTransactionReview::getReviewStatus, reviewStatus)
               .orderByDesc(AccountTransactionReview::getCreateTime);

        Page<AccountTransactionReview> pageResult = page(new Page<>(page, size), wrapper);

        return new PageResult<>(pageResult.getRecords(), pageResult.getTotal(), size, page);
    }

    @Override
    @Transactional
    public boolean reviewTransaction(String accountId, String transactionId, Integer reviewStatus) {
        // 1. 查找审核记录
        AccountTransactionReview review = getOne(Wrappers.<AccountTransactionReview>lambdaQuery()
                .eq(AccountTransactionReview::getAccountId, accountId)
                .eq(AccountTransactionReview::getTransactionId, transactionId));

        if (review == null) {
            throw new BusinessException("审核记录不存在");
        }

        // 2. 只有待审核状态的记录才能被审核
        if (review.getReviewStatus() != 0) {
            throw new BusinessException("当前审核记录已处理，无需重复操作");
        }

        // 3. 检查用户是否有其他待审核的申请 (排除当前正在审核的)
        if (reviewStatus != 0) { // 只有在审核通过或拒绝时才检查，避免循环检查
            long pendingCount = count(Wrappers.<AccountTransactionReview>lambdaQuery()
                    .eq(AccountTransactionReview::getAccountId, accountId)
                    .eq(AccountTransactionReview::getReviewStatus, 0) // 待审核
                    .ne(AccountTransactionReview::getTransactionId, transactionId)); // 排除当前记录
            if (pendingCount > 0) {
                throw new BusinessException("该用户已有其他待审核的申请，请先处理");
            }
        }

        Account account = accountService.getByAccountId(accountId);
        if (account == null) {
            throw new BusinessException("账户不存在");
        }

        // 4. 根据审核状态进行处理
        review.setReviewStatus(reviewStatus);
        review.setUpdateTime(LocalDateTime.now());
        review.setIsDeleted(1); // 将setDelete改为setIsDeleted

        if (reviewStatus == 1) { // 审核通过：无论充值还是提现，都扣除金额
            if (account.getBalance().compareTo(review.getAmount()) < 0) {
                throw new BusinessException("账户余额不足，操作失败"); // 统一提示，因为充值和提现都扣款
            }
            account.setBalance(account.getBalance().subtract(review.getAmount()));
            accountService.updateById(account); // 更新账户余额
        } else if (reviewStatus == 2) { // 审核拒绝
            review.setRemark("审核不通过"); // 设置拒绝备注
            log.info("交易ID: {} 的审核被拒绝", transactionId);
        } else { // 无效的审核状态（0是待审核，不应该作为审核结果传入）
            throw new BusinessException("无效的审核状态");
        }

        return updateById(review);
    }
} 