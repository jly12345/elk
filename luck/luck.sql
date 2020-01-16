/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.137.101
 Source Server Type    : MySQL
 Source Server Version : 100314
 Source Host           : 192.168.137.101:3306
 Source Schema         : luck

 Target Server Type    : MySQL
 Target Server Version : 100314
 File Encoding         : 65001

 Date: 17/01/2020 01:48:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_user_info
-- ----------------------------
DROP TABLE IF EXISTS `t_user_info`;
CREATE TABLE `t_user_info`  (
  `tenant_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `appid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `openid` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `uinfo` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_wx_config
-- ----------------------------
DROP TABLE IF EXISTS `t_wx_config`;
CREATE TABLE `t_wx_config`  (
  `tenant_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `appid` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `secret` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `token` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_wx_config
-- ----------------------------
INSERT INTO `t_wx_config` VALUES ('TN0000000001', 'wxdba63f4cfbc5a7e5', '8bd74339ad982d840fa88f89a8d0b12e', 'luck');

SET FOREIGN_KEY_CHECKS = 1;
