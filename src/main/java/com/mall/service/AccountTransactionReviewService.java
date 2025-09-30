package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.entity.AccountTransactionReview;
import com.mall.dto.AccountTransactionReviewDTO;
import com.mall.common.PageResult;

public interface AccountTransactionReviewService extends IService<AccountTransactionReview> {

    /**
     * 用户提交充值或提现审核请求
     * @param reviewDTO 审核请求信息
     * @return 新创建的审核记录实体
     */
    AccountTransactionReview submitReviewRequest(AccountTransactionReviewDTO reviewDTO);

    /**
     * 管理员获取待审核交易列表
     * @param page 页码
     * @param size 每页数量
     * @param transactionType 交易类型
     * @param reviewStatus 审核状态
     * @return 分页结果
     */
    PageResult<AccountTransactionReview> getReviewList(Integer page, Integer size, Integer transactionType, Integer reviewStatus);

    /**
     * 管理员审核交易请求
     * @param accountId 账户ID
     * @param transactionId 交易ID
     * @param reviewStatus 审核结果 (1:通过, 2:拒绝)
     * @return 是否成功
     */
    boolean reviewTransaction(String accountId, String transactionId, Integer reviewStatus);
} 