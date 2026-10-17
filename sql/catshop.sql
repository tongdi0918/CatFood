-- ============================================
-- catshop 一键建库 + 建表 + 初始化数据脚本
-- 使用方法（命令行）：
--   mysql -uroot -pmysql8 --default-character-set=utf8mb4 < catshop.sql
-- 或在 Navicat / IDEA 中直接运行本文件
-- 可重复执行，不会报错
-- ============================================

-- 1. 建库
CREATE DATABASE IF NOT EXISTS `catshop` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `catshop`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for product
-- ----------------------------
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

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (1, '幼猫奶糕猫粮', 119, '幼猫', 120, 'img/ym.jpg', '适合2‑12月幼猫，高奶蛋白', 1);
INSERT INTO `product` VALUES (2, '全期无谷猫粮', 139, '全期', 80, 'img/qjqg.jpg', '成幼猫通用，无谷配方', 1);
INSERT INTO `product` VALUES (3, '成猫减脂猫粮', 109, '成猫', 95, 'img/cmjz.jpg', '控制体重，低脂配方', 1);

SET FOREIGN_KEY_CHECKS = 1;
