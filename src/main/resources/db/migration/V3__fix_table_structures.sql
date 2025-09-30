-- 创建订单表
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    order_code VARCHAR(32) NOT NULL COMMENT '订单编号',
    user_id VARCHAR(32) NOT NULL COMMENT '用户ID',
    product_code VARCHAR(32) NOT NULL COMMENT '商品编号',
    status INT NOT NULL DEFAULT 1 COMMENT '订单状态(1:待审核,2:已审核,3:待取件,4:已收货,5:待检测,6:已检测,7:已定价,8:已完成)',
    delivery_type INT NOT NULL COMMENT '交货方式(1:到店,2:邮寄)',
    estimated_price DECIMAL(10,2) NOT NULL COMMENT '预估价格',
    final_price DECIMAL(10,2) COMMENT '最终价格',
    platform_fee DECIMAL(10,2) COMMENT '平台手续费',
    user_rebate DECIMAL(10,2) COMMENT '用户返点',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (product_code) REFERENCES products(product_code)
) COMMENT '订单表';

-- 修改user_level_rebate表结构
ALTER TABLE user_level_rebate
    DROP FOREIGN KEY IF EXISTS user_level_rebate_ibfk_1,
    MODIFY COLUMN user_id VARCHAR(32) NOT NULL COMMENT '用户ID',
    ADD CONSTRAINT user_level_rebate_ibfk_1 FOREIGN KEY (user_id) REFERENCES user(user_id);

-- 创建用户关系表
CREATE TABLE user_relation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '关系ID',
    user_id VARCHAR(32) NOT NULL COMMENT '用户ID',
    parent_id VARCHAR(32) NOT NULL COMMENT '上级用户ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户关系表';

-- 迁移数据
INSERT INTO user_relation (user_id, parent_id, create_time, update_time)
SELECT 
    user_id,
    parent_id,
    create_time,
    update_time
FROM user_relation;

-- 删除旧表
DROP TABLE user_relation;

-- 重命名新表
RENAME TABLE user_relation TO user_relation; 