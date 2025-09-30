-- 删除account的唯一键约束
ALTER TABLE gold_recycle_order
    DROP INDEX uk_account;

-- 添加account的普通索引
ALTER TABLE gold_recycle_order
    ADD INDEX idx_account (account);

-- 确保order_id是唯一的
ALTER TABLE gold_recycle_order
    ADD UNIQUE INDEX uk_order_id (order_id); 