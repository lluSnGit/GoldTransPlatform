-- 修改quotation表结构
ALTER TABLE quotation
    DROP COLUMN image_url,
    DROP COLUMN gold_type,
    DROP COLUMN weight,
    DROP COLUMN price,
    DROP COLUMN gold_content,
    DROP COLUMN melted_weight,
    DROP COLUMN pure_gold_weight,
    MODIFY COLUMN id VARCHAR(36) NOT NULL COMMENT '报价ID',
    MODIFY COLUMN order_id VARCHAR(36) NOT NULL COMMENT '订单ID',
    ADD COLUMN voucher_amount DECIMAL(10,2) NULL COMMENT '代金券金额' AFTER other_items;

-- 创建quotation_item表
CREATE TABLE quotation_item (
    id VARCHAR(36) NOT NULL COMMENT '报价项ID',
    quotation_id VARCHAR(36) NOT NULL COMMENT '报价ID',
    gold_type VARCHAR(50) NOT NULL COMMENT '黄金类型',
    image_url VARCHAR(255) COMMENT '图片链接',
    weight DECIMAL(10,2) NOT NULL COMMENT '克重',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    gold_content DECIMAL(10,2) NOT NULL COMMENT '含金量',
    melted_weight DECIMAL(10,2) NOT NULL COMMENT '熔后重',
    pure_gold_weight DECIMAL(10,2) NOT NULL COMMENT '纯金重',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_quotation_id (quotation_id),
    CONSTRAINT fk_quotation_item_quotation FOREIGN KEY (quotation_id) REFERENCES quotation(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报价项表';

-- 修改quotation_item表结构
ALTER TABLE quotation_item
    MODIFY COLUMN id VARCHAR(36) NOT NULL COMMENT '报价项ID',
    MODIFY COLUMN quotation_id VARCHAR(36) NOT NULL COMMENT '报价ID'; 