DROP TABLE IF EXISTS `app_account_sequence`;
CREATE TABLE `app_account_sequence` (
  `account_id` smallint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT '公司id',
  `db_name` varchar(50) NOT NULL DEFAULT 'pss_store' COMMENT '数据源',
  `table_name` varchar(10) DEFAULT NULL COMMENT '分表后缀',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `app_account`;
CREATE TABLE `app_account` (
  `account_id` bigint(20) NOT NULL COMMENT '账户id',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `salt` varchar(6) DEFAULT NULL COMMENT '密码盐值',
  `master` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否主账号',
  `activated` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否激活',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_pwd` bit(1) DEFAULT b'0' COMMENT '是否修改过密码 0未修改 1:已修改',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_mobile` (`mobile`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;