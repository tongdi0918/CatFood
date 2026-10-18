-- ============================================
-- catshop 一键建库 + 建表 + 初始化数据脚本
-- 使用方法（命令行）：
--   mysql -uroot -pmysql8 --default-character-set=utf8mb4 < catshop.sql
-- 或在 Navicat / IDEA 中直接运行本文件
-- 可重复执行，不会报错

-- mysql -uroot -pmysql8

-- ============================================


CREATE DATABASE IF NOT EXISTS `catshop` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;


SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
USE catshop;

DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `pid` int NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `pname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品名称',
  `price` double NOT NULL COMMENT '价格',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类',
  `stock` int NULL DEFAULT NULL COMMENT '库存',
  `image` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片路径',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '商品描述',
  `status` int NULL DEFAULT NULL COMMENT '1上架，0下架',
  PRIMARY KEY (`pid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;


USE `catshop`;
INSERT INTO product(pid,pname,price,category,stock,image,description,status) VALUES
                 (1,'幼猫奶糕猫粮',119.0,'幼猫',50,'/images/kitten_milk_cake.jpg','适合1‑4月龄幼猫，奶糕配方，好消化，助力幼猫发育。',1),
                 (2,'全期无谷鲜肉猫粮',218.0,'全期',22,'/images/all_stage_grain_free_fresh_meat.jpg','无谷配方，高肉含量，幼猫成猫通用，易消化吸收。',1),
                 (3,'成猫减脂猫粮',109.0,'成猫',95,'/images/adult_cat_fat_loss.jpg','专为室内易胖成猫调配，低脂高蛋白，帮助温和控制体重，守护猫咪健康体态。',1),
                 (4,'幼猫冻干双拼猫粮',179.0,'幼猫',28,'/images/kitten_freezed_dry_mix.jpg','添加鸡肉冻干，高蛋白，满足幼猫生长需求。',1),
                 (5,'成猫美毛猫粮',145.0,'成猫',40,'/images/adult_cat_beauty_fur.jpg','添加鱼油，滋养毛发，减少掉毛，呵护皮肤。',1),
                 (6,'全期低脂猫粮',132.0,'全期',16,'/images/all_stage_low_fat.jpg','低脂配方，适合易肥胖猫咪，保护胰腺健康。',1),
                 (7,'老年成猫专用粮',168.0,'成猫',12,'/images/senior_cat_special.jpg','7岁以上老年猫专用，呵护肾脏，易消化吸收。',1),
                 (8,'幼猫试吃装猫粮',29.9,'幼猫',100,'/images/kitten_trial_pack.jpg','小包装试吃，方便猫咪换粮尝试，净含量200g。',1),
                 (9,'成猫室内专用猫粮',156.0,'成猫',35,'/images/indoor_adult_cat.jpg','室内成猫专用，减少毛球，控制体重，保护泌尿系统健康。',1);
SET FOREIGN_KEY_CHECKS = 1;


USE `catshop`;
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id`          INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username`    VARCHAR(50)  NOT NULL COMMENT '用户名',
    `password`    VARCHAR(100) NOT NULL COMMENT '密码',
    `name`    VARCHAR(50)  NULL COMMENT '姓名',
    `email`       VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `phone`       VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
    `address`     VARCHAR(255) DEFAULT NULL COMMENT '收货地址',
    `role`        VARCHAR(20)  NOT NULL DEFAULT 'user' COMMENT '普通用户or管理员',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '用户表';

USE `catshop`;
INSERT INTO `user` (`username`, `password`, `phone`, `address`,`role`) VALUES
    ('admin', '123456', '13800138000', '四川省宜宾市四川轻化工大学','admin');




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




