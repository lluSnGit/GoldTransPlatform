-- 创建商品表
CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商品ID',
    name VARCHAR(100) NOT NULL COMMENT '商品名称',
    description TEXT COMMENT '商品描述',
    estimated_price DECIMAL(10,2) NOT NULL COMMENT '预估价格',
    final_price DECIMAL(10,2) COMMENT '最终价格',
    inspection_result TEXT COMMENT '检测结果',
    status INT NOT NULL DEFAULT 1 COMMENT '商品状态(1:待检测,2:已检测,3:已定价)',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '商品表';

-- 创建订单表
CREATE TABLE orders (
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

-- 创建用户等级返点表
CREATE TABLE user_level_rebates (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    user_level INT NOT NULL COMMENT '用户等级',
    rebate_amount DECIMAL(10,2) NOT NULL COMMENT '返点金额',
    total_rebate DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '累计返点金额',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES user(id),
    UNIQUE KEY uk_user_id (user_id),
    KEY idx_user_level (user_level)
) COMMENT '用户等级返点表';

-- 创建平台手续费表
CREATE TABLE platform_fees (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_level INT NOT NULL COMMENT '用户等级',
    fee_type INT NOT NULL COMMENT '手续费类型(1:固定金额,2:百分比)',
    fee_value DECIMAL(10,2) NOT NULL COMMENT '手续费值(固定金额或百分比)',
    min_amount DECIMAL(10,2) COMMENT '最小金额',
    max_amount DECIMAL(10,2) COMMENT '最大金额',
    status INT NOT NULL DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
    description VARCHAR(200) COMMENT '描述',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_user_level (user_level)
) COMMENT '平台手续费表';

-- 插入默认的平台手续费记录
INSERT INTO platform_fees (user_level, fee_type, fee_value, min_amount, max_amount, status, description) VALUES 
(1, 1, 8.00, 0.00, 1000.00, 1, '一级用户基础手续费8元'),
(1, 2, 3.00, 1000.00, NULL, 1, '一级用户超过1000元收取3%手续费'),
(2, 1, 10.00, 0.00, 1000.00, 1, '二级用户基础手续费10元'),
(2, 2, 5.00, 1000.00, NULL, 1, '二级用户超过1000元收取5%手续费');

-- 插入默认的用户等级返点记录
INSERT INTO user_level_rebates (user_id, user_level, rebate_amount) VALUES 
(1, 1, 2.00),  -- 一级用户返点2元
(1, 2, 1.00);  -- 二级用户返点1元 