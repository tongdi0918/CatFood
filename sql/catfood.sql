-- =============================================
-- 猫粮线上商店数据库脚本
-- MySQL 8.0
-- =============================================

CREATE DATABASE IF NOT EXISTS catfood_store
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE catfood_store;

-- ----------------------------
-- 用户表
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                        `id`          INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                        `username`    VARCHAR(50)  NOT NULL COMMENT '用户名',
                        `password`    VARCHAR(100) NOT NULL COMMENT '密码',
                        `email`       VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
                        `phone`       VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
                        `address`     VARCHAR(255) DEFAULT NULL COMMENT '收货地址',
                        `role`        TINYINT      NOT NULL DEFAULT 0 COMMENT '角色：0-普通用户，1-管理员',
                        `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `uk_username` (`username`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '用户表';

-- ----------------------------
-- 商品表（猫粮）
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
                           `id`          INT UNSIGNED    NOT NULL AUTO_INCREMENT COMMENT '商品ID',
                           `name`        VARCHAR(100)    NOT NULL COMMENT '商品名称',
                           `category`    VARCHAR(50)     NOT NULL COMMENT '分类：幼猫粮/成猫粮/处方粮/零食等',
                           `brand`       VARCHAR(50)     DEFAULT NULL COMMENT '品牌',
                           `price`       DECIMAL(10, 2)  NOT NULL COMMENT '价格',
                           `stock`       INT             NOT NULL DEFAULT 0 COMMENT '库存',
                           `image_url`   VARCHAR(255)    DEFAULT NULL COMMENT '图片路径',
                           `description` TEXT            COMMENT '商品描述',
                           `status`      TINYINT         NOT NULL DEFAULT 1 COMMENT '状态：1-上架，0-下架',
                           `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           PRIMARY KEY (`id`),
                           KEY `idx_category` (`category`),
                           KEY `idx_brand` (`brand`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '商品表';

-- ----------------------------
-- 订单表
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
                          `id`           INT UNSIGNED    NOT NULL AUTO_INCREMENT COMMENT '订单ID',
                          `order_no`     VARCHAR(32)     NOT NULL COMMENT '订单编号',
                          `user_id`      INT UNSIGNED    NOT NULL COMMENT '用户ID',
                          `total_amount` DECIMAL(10, 2)  NOT NULL COMMENT '订单总金额',
                          `receiver`     VARCHAR(50)     NOT NULL COMMENT '收货人',
                          `phone`        VARCHAR(20)     NOT NULL COMMENT '联系电话',
                          `address`      VARCHAR(255)    NOT NULL COMMENT '收货地址',
                          `status`       TINYINT         NOT NULL DEFAULT 0 COMMENT '状态：0-待付款，1-已付款，2-已发货，3-已完成，4-已取消',
                          `create_time`  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
                          `pay_time`     DATETIME        DEFAULT NULL COMMENT '支付时间',
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `uk_order_no` (`order_no`),
                          KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '订单表';

-- ----------------------------
-- 订单明细表
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
                              `id`          INT UNSIGNED   NOT NULL AUTO_INCREMENT COMMENT '明细ID',
                              `order_id`    INT UNSIGNED   NOT NULL COMMENT '订单ID',
                              `product_id`  INT UNSIGNED   NOT NULL COMMENT '商品ID',
                              `product_name` VARCHAR(100)  NOT NULL COMMENT '商品名称（快照）',
                              `price`       DECIMAL(10, 2) NOT NULL COMMENT '下单时单价',
                              `quantity`    INT            NOT NULL COMMENT '购买数量',
                              PRIMARY KEY (`id`),
                              KEY `idx_order_id` (`order_id`),
                              KEY `idx_product_id` (`product_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '订单明细表';

-- =============================================
-- 初始化测试数据
-- =============================================

-- 管理员账号：admin / 123456
INSERT INTO `user` (`username`, `password`, `email`, `role`)
VALUES ('admin', '123456', 'admin@catfood.com', 1);

-- 普通用户：test / 123456
INSERT INTO `user` (`username`, `password`, `email`, `phone`, `address`, `role`)
VALUES ('test', '123456', 'test@qq.com', '13800138000', '四川省宜宾市四川轻化工大学', 0);

-- 猫粮商品数据
INSERT INTO `product` (`name`, `category`, `brand`, `price`, `stock`, `image_url`, `description`) VALUES
                                            ('皇家幼猫K36猫粮 2kg', '幼猫粮', '皇家', 159.00, 100, '/images/product1.jpg', '适合4-12月龄幼猫，富含DHA和Omega-3，助力大脑与视力发育。'),
                                            ('渴望原味成猫粮 1.8kg', '成猫粮', '渴望', 328.00, 50, '/images/product2.jpg', '无谷鸡肉配方，高蛋白低碳水，适合全阶段成猫。'),
                                            ('皇家处方泌尿道猫粮 1.5kg', '处方粮', '皇家', 189.00, 30, '/images/product3.jpg', '专为泌尿道疾病猫咪设计，控制镁离子摄入，促进饮水。'),
                                            ('麦富迪冻干双拼成猫粮 1.5kg', '成猫粮', '麦富迪', 89.00, 200, '/images/product4.jpg', '含冻干鸡肉粒，肉含量高达80%，美毛亮肤。'),
                                            ('富力鲜猫零食罐头 80g*24', '猫零食', '富力鲜', 129.00, 150, '/images/product5.jpg', '泰国进口白肉罐头，多种口味组合，补水营养。'),
                                            ('巅峰牛肉干猫粮 400g', '成猫粮', '巅峰', 268.00, 20, '/images/product6.jpg', '96%高肉含量，含牛肉配方，天然无谷无添加。'),
                                            ('宠儿香猫益生菌 5g*10', '猫零食', '宠儿香', 45.00, 80, '/images/product7.jpg', '调理猫咪肠胃，缓解腹泻呕吐，增强免疫力。'),
                                            ('冠能幼猫全价猫粮 2.5kg', '幼猫粮', '冠能', 145.00, 60, '/images/product8.jpg', '牛初乳配方，提升幼猫自身免疫力，适合离乳期。');
