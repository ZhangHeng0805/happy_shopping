# 工程简介
> 之前的服务器[File-Management-Server](https://github.com/ZhangHeng0805/File-Management-Server)因为有太多的接口，太杂了，所以我把乐在购物系统单独分离出来了
# 系统架构图
![系统架构图](https://user-images.githubusercontent.com/74289276/163973512-3d7da8d8-c00c-4a30-96ce-23152a955e03.jpg)



* 配置文件application.properties
```properties
# 应用服务 WEB 访问端口（必填）
server.port=8081
# 数据库连接地址（必填）
spring.datasource.url=jdbc:mysql://localhost:3306/happy_shopping?serverTimezone=UTC&characterEncoding=utf-8
# 数据库用户名（必填）
spring.datasource.username=root
#数据库密码（必填
spring.datasource.password=root


# 管理员账号 长度[8,20]（必填）
login-admin.account=13733430842
# 管理员密码 长度[6,18]（必填）
login-admin.password=305666
# 管理员邮箱（非必填，不填则不通知）
login-admin.email=zhangheng.0805@qq.com

# 最大重试机会 范围[3,10]次（必填）
setting.max_count=5
# 间隔等待时间（登录，邮箱验证码）范围[1,1440]分钟（必填）
setting.wait_time=5
# 顾客重置的初始密码 长度[6,18]（必填）
setting.customer_reset_pwd=123456
# 商家重置的初始密码 长度[6,18]（必填）
setting.merchants_reset_pwd=666666
```

# 数据库happy_shopping
## 数据库设计图
![数据库设计](https://user-images.githubusercontent.com/74289276/163973618-04ca6f51-6c38-402f-a4e4-bebb5a5ad0ed.jpg)
## 数据库建表SQL
```sql
/*
  Navicat Premium Data Transfer
 
  Source Server         : 张恒
  Source Server Type    : MySQL
  Source Server Version : 50535
  Source Host           : localhost:3306
  Source Schema         : happy_shopping
 
  Target Server Type    : MySQL
  Target Server Version : 50535
  File Encoding         : 65001
 
  Date: 19/04/2022 17:13:33
 */
 
 SET NAMES utf8mb4;
 SET FOREIGN_KEY_CHECKS = 0;
 
 -- ----------------------------
 -- Table structure for chatconfig
 -- ----------------------------
 DROP TABLE IF EXISTS `chatconfig`;
 CREATE TABLE `chatconfig`  (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
   `ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务器ip地址',
   `port` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务器端口号',
   `local_port` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '本地端口号',
   `explain` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '说明',
   PRIMARY KEY (`id`) USING BTREE
 ) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;
 
 -- ----------------------------
 -- Table structure for customer
 -- ----------------------------
 DROP TABLE IF EXISTS `customer`;
 CREATE TABLE `customer`  (
   `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '手机号',
   `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
   `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
   `sex` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '性别',
   `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
   `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货地址',
   `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '注册时间',
   `state` int(11) NULL DEFAULT 0 COMMENT '账号状态',
   PRIMARY KEY (`phone`) USING BTREE
 ) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;
 
 -- ----------------------------
 -- Table structure for goods
 -- ----------------------------
 DROP TABLE IF EXISTS `goods`;
 CREATE TABLE `goods`  (
   `goods_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '商品id',
   `goods_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
   `goods_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品图片',
   `goods_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品类型',
   `goods_introduction` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品简介',
   `goods_sales` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '销量',
   `goods_price` double(10, 2) NULL DEFAULT NULL COMMENT '价格',
   `store_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '店铺名称',
   `store_id` int(11) NULL DEFAULT NULL COMMENT '店铺id',
   `time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新时间',
   `state` int(11) NULL DEFAULT NULL COMMENT '商品状态(0:上线；1:下线；2:审核中)',
   `goods_num` int(11) NULL DEFAULT NULL COMMENT '商品库存',
   PRIMARY KEY (`goods_id`) USING BTREE,
   INDEX `goods_name`(`goods_name`(191)) USING BTREE,
   INDEX `store_id`(`store_id`) USING BTREE,
   CONSTRAINT `goods_ibfk_1` FOREIGN KEY (`store_id`) REFERENCES `stores` (`store_id`) ON DELETE CASCADE ON UPDATE CASCADE
 ) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;
 
 -- ----------------------------
 -- Table structure for goods_list
 -- ----------------------------
 DROP TABLE IF EXISTS `goods_list`;
 CREATE TABLE `goods_list`  (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
   `goods_id` int(11) NULL DEFAULT NULL COMMENT '商品id',
   `store_id` int(11) NULL DEFAULT NULL COMMENT '店铺id',
   `goods_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名',
   `goods_price` double(10, 2) NULL DEFAULT NULL COMMENT '商品价格',
   `num` int(11) NULL DEFAULT NULL COMMENT '数量',
   `state` int(11) NULL DEFAULT NULL COMMENT '订单状态[0:未处理；1:拒绝发货；2:待收货；3:已收货；4:退货]',
   `list_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单id',
   PRIMARY KEY (`id`) USING BTREE,
   INDEX `list_id`(`list_id`) USING BTREE,
   INDEX `goods_id`(`goods_id`) USING BTREE,
   INDEX `store_id`(`store_id`) USING BTREE,
   CONSTRAINT `goods_list_ibfk_1` FOREIGN KEY (`list_id`) REFERENCES `submit_goodslist` (`submit_id`) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT `goods_list_ibfk_2` FOREIGN KEY (`goods_id`) REFERENCES `goods` (`goods_id`) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT `goods_list_ibfk_3` FOREIGN KEY (`store_id`) REFERENCES `stores` (`store_id`) ON DELETE CASCADE ON UPDATE CASCADE
 ) ENGINE = InnoDB AUTO_INCREMENT = 149 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;
 
 -- ----------------------------
 -- Table structure for goods_type
 -- ----------------------------
 DROP TABLE IF EXISTS `goods_type`;
 CREATE TABLE `goods_type`  (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
   `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品类型',
   PRIMARY KEY (`id`, `type`) USING BTREE
 ) ENGINE = MyISAM AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;
 
 -- ----------------------------
 -- Table structure for location
 -- ----------------------------
 DROP TABLE IF EXISTS `location`;
 CREATE TABLE `location`  (
   `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
   `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
   `latitude` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '经度',
   `longitude` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维度',
   `time` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时间',
   `state` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
   PRIMARY KEY (`phone`) USING BTREE
 ) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;
 
 -- ----------------------------
 -- Table structure for merchants
 -- ----------------------------
 DROP TABLE IF EXISTS `merchants`;
 CREATE TABLE `merchants`  (
   `phonenum` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
   `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
   `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
   `account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
   `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
   `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址',
   `gender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '性别',
   `store_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '店铺名称',
   `store_id` int(11) NULL DEFAULT NULL COMMENT '店铺id',
   `store_introduce` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '店铺介绍',
   `time` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新时间',
   `state` int(11) NULL DEFAULT NULL COMMENT '账号状态[0:正常,1:违规封禁。2:账号异常]',
   PRIMARY KEY (`phonenum`) USING BTREE,
   INDEX `name`(`name`(191), `store_name`(191), `store_introduce`(191), `time`) USING BTREE,
   INDEX `store_name`(`store_name`(191)) USING BTREE,
   INDEX `store_id`(`store_id`) USING BTREE
 ) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;
 
 -- ----------------------------
 -- Table structure for operation_log
 -- ----------------------------
 DROP TABLE IF EXISTS `operation_log`;
 CREATE TABLE `operation_log`  (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
   `count` int(2) NULL DEFAULT NULL COMMENT '操作次数',
   `type` int(11) NULL DEFAULT NULL COMMENT '操作类型',
   `info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作信息',
   `request` varchar(10000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问request',
   `tel` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账号',
   `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作时间',
   PRIMARY KEY (`id`) USING BTREE
 ) ENGINE = MyISAM AUTO_INCREMENT = 70 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;
 
 -- ----------------------------
 -- Table structure for phone_info
 -- ----------------------------
 DROP TABLE IF EXISTS `phone_info`;
 CREATE TABLE `phone_info`  (
   `id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备ID',
   `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备IP地址',
   `model` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备型号',
   `sdk` int(11) NULL DEFAULT NULL COMMENT 'sdk版本',
   `release` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'android版本',
   `app_version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'app型号',
   `tel` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
   `tel_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机运营商',
   `last_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最近一次访问时间',
   `notice` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提示信息',
   PRIMARY KEY (`id`) USING BTREE
 ) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;
 
 -- ----------------------------
 -- Table structure for stores
 -- ----------------------------
 DROP TABLE IF EXISTS `stores`;
 CREATE TABLE `stores`  (
   `store_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '店铺id',
   `store_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '店铺名称',
   `store_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '店铺图片',
   `boss_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '老板姓名',
   `start_time` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '注册时间',
   `store_introduce` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '店铺简介',
   `turnover` double(10, 2) NULL DEFAULT 0.00 COMMENT '店铺营业额',
   PRIMARY KEY (`store_id`) USING BTREE,
   INDEX `store_name`(`store_name`(191)) USING BTREE
 ) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;
 
 -- ----------------------------
 -- Table structure for submit_goodslist
 -- ----------------------------
 DROP TABLE IF EXISTS `submit_goodslist`;
 CREATE TABLE `submit_goodslist`  (
   `submit_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单id',
   `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货人',
   `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货地址',
   `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
   `count_price` double(10, 2) NULL DEFAULT NULL COMMENT '订单价格',
   `time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单时间',
   PRIMARY KEY (`submit_id`) USING BTREE,
   INDEX `phone`(`phone`) USING BTREE,
   CONSTRAINT `submit_goodslist_ibfk_1` FOREIGN KEY (`phone`) REFERENCES `customer` (`phone`) ON DELETE CASCADE ON UPDATE CASCADE
 ) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;
 
 -- ----------------------------
 -- Table structure for verification_code
 -- ----------------------------
 DROP TABLE IF EXISTS `verification_code`;
 CREATE TABLE `verification_code`  (
   `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '验证码id',
   `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '验证码',
   PRIMARY KEY (`id`) USING BTREE
 ) ENGINE = MyISAM CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;
 
 -- ----------------------------
 -- Triggers structure for table customer
 -- ----------------------------
 DROP TRIGGER IF EXISTS `username`;
 delimiter ;;
 CREATE TRIGGER `username` AFTER UPDATE ON `customer` FOR EACH ROW BEGIN
 UPDATE submit_goodslist SET name=new.username WHERE name=old.username;
 END
 ;;
 delimiter ;
 
 -- ----------------------------
 -- Triggers structure for table goods
 -- ----------------------------
 DROP TRIGGER IF EXISTS `goods_name`;
 delimiter ;;
 CREATE TRIGGER `goods_name` AFTER UPDATE ON `goods` FOR EACH ROW begin
 update goods_list set goods_name=new.goods_name where goods_name=old.goods_name;
 end
 ;;
 delimiter ;
 
 -- ----------------------------
 -- Triggers structure for table goods_type
 -- ----------------------------
 DROP TRIGGER IF EXISTS `goods_type`;
 delimiter ;;
 CREATE TRIGGER `goods_type` AFTER UPDATE ON `goods_type` FOR EACH ROW begin
 update goods set goods_type=new.type where goods_type=old.type;
 end
 ;;
 delimiter ;
 
 -- ----------------------------
 -- Triggers structure for table goods_type
 -- ----------------------------
 DROP TRIGGER IF EXISTS `type`;
 delimiter ;;
 CREATE TRIGGER `type` AFTER DELETE ON `goods_type` FOR EACH ROW begin
 update goods set goods_type='暂无分类',state=2 where goods_type=old.type;
 end
 ;;
 delimiter ;
 
 -- ----------------------------
 -- Triggers structure for table merchants
 -- ----------------------------
 DROP TRIGGER IF EXISTS `update`;
 delimiter ;;
 CREATE TRIGGER `update` AFTER UPDATE ON `merchants` FOR EACH ROW begin
 update stores set store_name=new.store_name where store_name=old.store_name;
 update stores set store_introduce=new.store_introduce where store_introduce=old.store_introduce;
 update stores set boss_name=new.name where boss_name=old.name;
 end
 ;;
 delimiter ;
 
 -- ----------------------------
 -- Triggers structure for table merchants
 -- ----------------------------
 DROP TRIGGER IF EXISTS `delete`;
 delimiter ;;
 CREATE TRIGGER `delete` AFTER DELETE ON `merchants` FOR EACH ROW begin
 delete from stores where store_id =old.store_id;
 end
 ;;
 delimiter ;
 
 -- ----------------------------
 -- Triggers structure for table stores
 -- ----------------------------
 DROP TRIGGER IF EXISTS `store_name`;
 delimiter ;;
 CREATE TRIGGER `store_name` AFTER UPDATE ON `stores` FOR EACH ROW BEGIN
 update goods set store_name=new.store_name where store_name=old.store_name;
 END
 ;;
 delimiter ;
 
 -- ----------------------------
 -- Triggers structure for table submit_goodslist
 -- ----------------------------
 DROP TRIGGER IF EXISTS `删除`;
 delimiter ;;
 CREATE TRIGGER `删除` AFTER DELETE ON `submit_goodslist` FOR EACH ROW begin
 delete from goods_list where list_id =old.submit_id;
 end
 ;;
 delimiter ;
 
 SET FOREIGN_KEY_CHECKS = 1;


-- ----------------------------
-- Records of verification_code
-- ----------------------------
INSERT INTO `verification_code` VALUES ('3742', '691837');
INSERT INTO `verification_code` VALUES ('3215', '789511');
INSERT INTO `verification_code` VALUES ('3942', '592167');
INSERT INTO `verification_code` VALUES ('1692', '773661');
INSERT INTO `verification_code` VALUES ('9511', '469922');
INSERT INTO `verification_code` VALUES ('1239', '687682');
INSERT INTO `verification_code` VALUES ('9623', '789631');
INSERT INTO `verification_code` VALUES ('8899', '123545');
INSERT INTO `verification_code` VALUES ('2634', '489622');
INSERT INTO `verification_code` VALUES ('1236', '773610');
INSERT INTO `verification_code` VALUES ('2456', '692178');
INSERT INTO `verification_code` VALUES ('2134', '110916');
INSERT INTO `verification_code` VALUES ('2236', '091613');
INSERT INTO `verification_code` VALUES ('1368', '132116');
INSERT INTO `verification_code` VALUES ('2399', '092115');
INSERT INTO `verification_code` VALUES ('2526', '161609');
INSERT INTO `verification_code` VALUES ('0742', '171121');
INSERT INTO `verification_code` VALUES ('0616', '170916');
INSERT INTO `verification_code` VALUES ('3052', '674209');
INSERT INTO `verification_code` VALUES ('2890', '717756');

```


