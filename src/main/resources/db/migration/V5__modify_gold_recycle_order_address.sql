-- 修改gold_recycle_order表，将receiver_address改为address_id
ALTER TABLE gold_recycle_order
    DROP COLUMN receiver_address,
    ADD COLUMN address_id BIGINT NOT NULL COMMENT '收货地址ID' AFTER receiver_phone; 