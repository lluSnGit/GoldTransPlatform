CREATE TABLE `price_control` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `recycle_price` decimal(10,2) NOT NULL COMMENT '回收价',
  `sell_price` decimal(10,2) NOT NULL COMMENT '出售价',
  `price_type` tinyint NOT NULL COMMENT '价格类型(1:金价,2:铂金价)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_price_type` (`price_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='价格控制表'; 