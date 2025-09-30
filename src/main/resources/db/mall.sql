SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `account`
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `account_id` varchar(255) DEFAULT NULL COMMENT '账户ID',
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户随机ID',
  `balance` decimal(10,2) DEFAULT NULL COMMENT '账户余额',
  `total_rebate` decimal(10,2) DEFAULT NULL COMMENT '累计返点',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 COMMENT='账户实体';

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('1', '13368156279', '2a361057a73f45ff9d263282ba88faee', '400.00', '10.00', null, null);
INSERT INTO `account` VALUES ('2', '131', '2ee852f61ea24d3e9bac3eb9726bbe70', '0.00', '0.00', null, null);
INSERT INTO `account` VALUES ('3', '13834040079', '20c5ce0158194bb9b82e15154b4f14ae', '1000.00', '10.00', null, null);

-- ----------------------------
-- Table structure for `account_transaction`
-- ----------------------------
DROP TABLE IF EXISTS `account_transaction`;
CREATE TABLE `account_transaction` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `transaction_id` varchar(255) DEFAULT NULL COMMENT '交易ID',
  `account_id` varchar(255) DEFAULT NULL COMMENT '账户ID',
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户ID',
  `type` int DEFAULT NULL COMMENT '交易类型(1:充值,2:提现,3:返点增加,4:返点减少)',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '交易金额',
  `balance` decimal(10,2) DEFAULT NULL COMMENT '交易后余额',
  `total_rebate` decimal(10,2) DEFAULT NULL COMMENT '交易后返点',
  `status` int DEFAULT NULL COMMENT '交易状态(0:失败,1:成功)',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `transfer_image_url` varchar(255) DEFAULT NULL COMMENT '转账截图URL',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3 COMMENT='账户交易记录实体';

-- ----------------------------
-- Records of account_transaction
-- ----------------------------
INSERT INTO `account_transaction` VALUES ('1', '13368156279', '2a361057a73f45ff9d263282ba88faee', '2a361057a73f45ff9d263282ba88faee', '1', '400.00', '400.00', '0.00', '1', '账户充值', '2025-05-18 16:43:41', '2025-05-18 16:43:41');
INSERT INTO `account_transaction` VALUES ('2', '13368156279', '2a361057a73f45ff9d263282ba88faee', '2a361057a73f45ff9d263282ba88faee', '3', '10.00', '400.00', '10.00', '1', '返点增加', '2025-05-18 16:43:51', '2025-05-18 16:43:51');
INSERT INTO `account_transaction` VALUES ('3', '13834040079', '20c5ce0158194bb9b82e15154b4f14ae', '20c5ce0158194bb9b82e15154b4f14ae', '1', '500.00', '500.00', '0.00', '1', '账户充值', '2025-05-18 17:37:59', '2025-05-18 17:37:59');
INSERT INTO `account_transaction` VALUES ('4', '13834040079', '20c5ce0158194bb9b82e15154b4f14ae', '20c5ce0158194bb9b82e15154b4f14ae', '3', '10.00', '500.00', '10.00', '1', '返点增加', '2025-05-18 17:38:05', '2025-05-18 17:38:05');
INSERT INTO `account_transaction` VALUES ('5', '13834040079', '20c5ce0158194bb9b82e15154b4f14ae', '20c5ce0158194bb9b82e15154b4f14ae', '1', '500.00', '1000.00', '10.00', '1', '账户充值', '2025-05-19 21:04:26', '2025-05-19 21:04:26');

-- ----------------------------
-- Table structure for `admin`
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `admin_id` varchar(32) NOT NULL COMMENT '管理员ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0禁用，1启用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_admin_id` (`admin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 COMMENT='管理员表';

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('2', '64e58cdf20bc4fa8a23466b670b11d75', 'root', '$2a$10$024O.8B70vZ94ZzD/hGT3.FJv4LL4ko/tub82gpMiSuy8YuFWT4l6', '12312', '12312', '12121', '1', '2025-05-19 20:35:29', '2025-05-19 20:35:29', null, null); 

-- ----------------------------
-- Table structure for `account_transaction_review`
-- ----------------------------
DROP TABLE IF EXISTS `account_transaction_review`;
CREATE TABLE `account_transaction_review` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `transaction_id` varchar(255) DEFAULT NULL COMMENT '交易ID',
  `account_id` varchar(255) DEFAULT NULL COMMENT '账户ID',
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户ID',
  `transaction_type` int DEFAULT NULL COMMENT '交易类型(1:充值,2:提现)',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '交易金额',
  `payment_type` int DEFAULT NULL COMMENT '支付类型(针对提现：1:支付宝,2:微信,3:银行卡)',
  `transfer_image_url` varchar(255) DEFAULT NULL COMMENT '转账截图URL(针对充值)',
  `review_status` int DEFAULT NULL COMMENT '审核状态(0:待审核,1:已通过,2:已拒绝)',
  `remark` varchar(255) DEFAULT NULL COMMENT '用户备注',
  `reviewer_id` varchar(255) DEFAULT NULL COMMENT '审核人ID',
  `review_time` datetime DEFAULT NULL COMMENT '审核时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户交易审核记录实体';

-- ----------------------------
-- Records of account_transaction_review
-- ----------------------------

-- ----------------------------
-- Table structure for `account_transaction_review`
-- ----------------------------
DROP TABLE IF EXISTS `account_transaction_review`;
CREATE TABLE `account_transaction_review` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `transaction_id` varchar(255) DEFAULT NULL COMMENT '交易ID',
  `account_id` varchar(255) DEFAULT NULL COMMENT '账户ID',
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户ID',
  `transaction_type` int DEFAULT NULL COMMENT '交易类型(1:充值,2:提现)',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '交易金额',
  `payment_type` int DEFAULT NULL COMMENT '支付类型(针对提现：1:支付宝,2:微信,3:银行卡)',
  `transfer_image_url` varchar(255) DEFAULT NULL COMMENT '转账截图URL(针对充值)',
  `review_status` int DEFAULT NULL COMMENT '审核状态(0:待审核,1:已通过,2:已拒绝)',
  `remark` varchar(255) DEFAULT NULL COMMENT '用户备注',
  `reviewer_id` varchar(255) DEFAULT NULL COMMENT '审核人ID',
  `review_time` datetime DEFAULT NULL COMMENT '审核时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户交易审核记录实体';

-- ----------------------------
-- Records of account_transaction_review
-- ---------------------------- 