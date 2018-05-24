/*
Navicat MySQL Data Transfer

Source Server         : sunny
Source Server Version : 50628
Source Host           : localhost:3306
Source Database       : activiti2

Target Server Type    : MYSQL
Target Server Version : 50628
File Encoding         : 65001

Date: 2018-05-21 13:08:20
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for a_oa_leave
-- ----------------------------
DROP TABLE IF EXISTS `a_oa_leave`;
CREATE TABLE `a_oa_leave` (
  `id` int(11) NOT NULL,
  `TASK_ID` varchar(100) DEFAULT NULL,
  `PRO_ID` varchar(100) DEFAULT NULL,
  `USER` varchar(255) DEFAULT NULL,
  `USER_ID` varchar(100) DEFAULT NULL,
  `CREATE_TIME` varchar(100) DEFAULT NULL,
  `CONTENT` varchar(255) DEFAULT NULL,
  `TITLE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of a_oa_leave
-- ----------------------------
