-- 创建用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id` VARCHAR(32) NOT NULL COMMENT '用户随机ID',
    `account` VARCHAR(50) NOT NULL COMMENT '账号',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `user_level` INT NOT NULL DEFAULT 1 COMMENT '用户等级',
    `role_level` INT NOT NULL DEFAULT 1 COMMENT '角色等级',
    `parent_id` BIGINT DEFAULT NULL COMMENT '上级用户ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    UNIQUE KEY `uk_account` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 创建账户表
CREATE TABLE IF NOT EXISTS `account` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `account_id` VARCHAR(50) NOT NULL COMMENT '账户ID',
    `user_id` VARCHAR(32) NOT NULL COMMENT '用户ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_account_id` (`account_id`),
    UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户表'; 