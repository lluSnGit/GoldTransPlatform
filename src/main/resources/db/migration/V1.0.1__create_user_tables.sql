-- 用户表
CREATE TABLE `user` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id` varchar(32) NOT NULL COMMENT '用户随机ID',
    `account` varchar(50) NOT NULL COMMENT '账号',
    `password` varchar(100) NOT NULL COMMENT '密码',
    `username` varchar(50) NOT NULL COMMENT '用户名',
    `user_level` int NOT NULL DEFAULT '0' COMMENT '用户等级(0:普通用户,1:VIP用户,2:SVIP用户)',
    `role_level` int NOT NULL DEFAULT '0' COMMENT '角色等级(0:普通用户,1:商家,2:管理员)',
    `parent_id` varchar(32) DEFAULT NULL COMMENT '上级ID',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    UNIQUE KEY `uk_account` (`account`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 用户信息表
CREATE TABLE `user_info` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id` varchar(32) NOT NULL COMMENT '用户随机ID',
    `account` varchar(50) NOT NULL COMMENT '账号',
    `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
    `gender` tinyint NOT NULL DEFAULT '0' COMMENT '性别(0:未知,1:男,2:女)',
    `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
    `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
    `id_card` varchar(18) DEFAULT NULL COMMENT '身份证号',
    `birthday` datetime DEFAULT NULL COMMENT '生日',
    `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
    -- 地址信息
    `receiver_name` varchar(50) DEFAULT NULL COMMENT '收货人姓名',
    `receiver_phone` varchar(20) DEFAULT NULL COMMENT '收货人电话',
    `province` varchar(50) DEFAULT NULL COMMENT '省份',
    `city` varchar(50) DEFAULT NULL COMMENT '城市',
    `district` varchar(50) DEFAULT NULL COMMENT '区县',
    `detail_address` varchar(200) DEFAULT NULL COMMENT '详细地址',
    `post_code` varchar(10) DEFAULT NULL COMMENT '邮政编码',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    UNIQUE KEY `uk_account` (`account`),
    KEY `idx_phone` (`phone`),
    KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- 用户地址表
CREATE TABLE `user_address` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id` varchar(32) NOT NULL COMMENT '用户随机ID',
    `account` varchar(50) NOT NULL COMMENT '账号',
    `receiver_name` varchar(50) NOT NULL COMMENT '收货人姓名',
    `receiver_phone` varchar(20) NOT NULL COMMENT '收货人电话',
    `province` varchar(50) NOT NULL COMMENT '省份',
    `city` varchar(50) NOT NULL COMMENT '城市',
    `district` varchar(50) NOT NULL COMMENT '区县',
    `detail_address` varchar(200) NOT NULL COMMENT '详细地址',
    `post_code` varchar(10) DEFAULT NULL COMMENT '邮政编码',
    `is_default` tinyint NOT NULL DEFAULT '0' COMMENT '是否默认地址(0:否,1:是)',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_account` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户地址表'; 