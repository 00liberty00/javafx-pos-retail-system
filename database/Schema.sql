-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: 193.233.103.206    Database: essa
-- ------------------------------------------------------
-- Server version	8.0.44-0ubuntu0.24.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `accounting`
--

DROP TABLE IF EXISTS `accounting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounting` (
  `id_accounting` int NOT NULL AUTO_INCREMENT,
  `firstSumGoods` decimal(9,2) DEFAULT NULL,
  `lastSumGoods` decimal(9,2) DEFAULT NULL,
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `freeMoney` decimal(9,2) DEFAULT NULL,
  `differenceFromSumGoods` decimal(9,2) DEFAULT NULL,
  PRIMARY KEY (`id_accounting`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `arrival`
--

DROP TABLE IF EXISTS `arrival`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `arrival` (
  `id_arrival` bigint NOT NULL AUTO_INCREMENT,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `sumInvoice` decimal(9,2) NOT NULL,
  `sumArrival` decimal(9,2) NOT NULL,
  `number_waybill` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `note` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `id_user` bigint NOT NULL,
  PRIMARY KEY (`id_arrival`),
  UNIQUE KEY `id_arrivalList_UNIQUE` (`id_arrival`),
  KEY `fk_arrival_userSwing1_idx` (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=10737 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `arrivaldish`
--

DROP TABLE IF EXISTS `arrivaldish`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `arrivaldish` (
  `id_arrivaldish` bigint NOT NULL AUTO_INCREMENT,
  `date` timestamp NULL DEFAULT NULL,
  `sumInvoice` decimal(9,2) DEFAULT NULL,
  `number_waybill` varchar(100) DEFAULT NULL,
  `note` varchar(500) DEFAULT NULL,
  `id_user` bigint DEFAULT NULL,
  PRIMARY KEY (`id_arrivaldish`),
  UNIQUE KEY `id_arrivaldish_UNIQUE` (`id_arrivaldish`),
  KEY `fk_arrdish_user_idx` (`id_user`),
  CONSTRAINT `fk_arrdish_user` FOREIGN KEY (`id_user`) REFERENCES `userswing` (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `arrivaldishlist`
--

DROP TABLE IF EXISTS `arrivaldishlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `arrivaldishlist` (
  `id_arrivaldishlist` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(10,3) DEFAULT NULL,
  `priceOpt` decimal(10,2) DEFAULT NULL,
  `id_dish` bigint DEFAULT NULL,
  `id_arrivaldish` bigint DEFAULT NULL,
  PRIMARY KEY (`id_arrivaldishlist`),
  UNIQUE KEY `id_arrivaldishlist_UNIQUE` (`id_arrivaldishlist`),
  KEY `fk_arrdishlist_arrdish_idx` (`id_arrivaldish`),
  KEY `fk_arrdishlist_dish_idx` (`id_dish`),
  CONSTRAINT `fk_arrdishlist_arrdish` FOREIGN KEY (`id_arrivaldish`) REFERENCES `arrivaldish` (`id_arrivaldish`),
  CONSTRAINT `fk_arrdishlist_dish` FOREIGN KEY (`id_dish`) REFERENCES `dish` (`id_dish`)
) ENGINE=InnoDB AUTO_INCREMENT=114 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `arrivallist`
--

DROP TABLE IF EXISTS `arrivallist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `arrivallist` (
  `id_arrivalList` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(7,3) NOT NULL,
  `price` decimal(7,2) DEFAULT NULL,
  `priceOpt` decimal(7,2) DEFAULT NULL,
  `id_goods` bigint NOT NULL,
  `id_arrival` bigint NOT NULL,
  PRIMARY KEY (`id_arrivalList`),
  KEY `fk_check1_goods1_idx` (`id_goods`),
  KEY `fk_arrivalList_arrival1_idx` (`id_arrival`),
  CONSTRAINT `fk_arrivalList_arrival1` FOREIGN KEY (`id_arrival`) REFERENCES `arrival` (`id_arrival`),
  CONSTRAINT `fk_check1_goods100` FOREIGN KEY (`id_goods`) REFERENCES `goods` (`id_goods`)
) ENGINE=InnoDB AUTO_INCREMENT=49291 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `barcode`
--

DROP TABLE IF EXISTS `barcode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `barcode` (
  `id_barcode` int NOT NULL AUTO_INCREMENT,
  `barcode` bigint DEFAULT NULL,
  `id_goods` bigint DEFAULT NULL,
  `id_dish` bigint DEFAULT NULL,
  PRIMARY KEY (`id_barcode`),
  UNIQUE KEY `id_barcode_UNIQUE` (`id_barcode`),
  KEY `fk_barcode_goods1_idx` (`id_goods`),
  KEY `fk_barcode_dish` (`id_dish`),
  CONSTRAINT `fk_barcode_dish` FOREIGN KEY (`id_dish`) REFERENCES `dish` (`id_dish`),
  CONSTRAINT `fk_barcode_goods1` FOREIGN KEY (`id_goods`) REFERENCES `goods` (`id_goods`)
) ENGINE=InnoDB AUTO_INCREMENT=1805 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `case_record`
--

DROP TABLE IF EXISTS `case_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `case_record` (
  `id_case` bigint NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `cashMustBe` decimal(9,2) DEFAULT NULL,
  `id_cashIn` int DEFAULT NULL,
  `id_cashOut` int DEFAULT NULL,
  `id_user` bigint DEFAULT NULL,
  `id_arrival` bigint DEFAULT NULL,
  `id_writeoff` bigint DEFAULT NULL,
  `id_arrivaldish` bigint DEFAULT NULL,
  PRIMARY KEY (`id_case`),
  KEY `fk_case_record_cash_in1_idx` (`id_cashIn`),
  KEY `fk_case_record_cash_out1_idx` (`id_cashOut`),
  KEY `fk_case_record_userSwing1_idx` (`id_user`),
  KEY `fk_case_record_arrival1_idx` (`id_arrival`),
  KEY `fk_case_record_writeoff1_idx` (`id_writeoff`),
  KEY `fk_case_record_arrivaldish1_idx` (`id_arrivaldish`)
) ENGINE=InnoDB AUTO_INCREMENT=45602 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cash_in`
--

DROP TABLE IF EXISTS `cash_in`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cash_in` (
  `id_cashIn` int NOT NULL AUTO_INCREMENT,
  `sum_cash` decimal(9,2) NOT NULL,
  `note` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  PRIMARY KEY (`id_cashIn`),
  UNIQUE KEY `id_cashIn_UNIQUE` (`id_cashIn`)
) ENGINE=InnoDB AUTO_INCREMENT=3116 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cash_out`
--

DROP TABLE IF EXISTS `cash_out`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cash_out` (
  `id_cashOut` int NOT NULL AUTO_INCREMENT,
  `sum_cash` decimal(9,2) NOT NULL,
  `note` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  PRIMARY KEY (`id_cashOut`),
  UNIQUE KEY `id_cashIn_UNIQUE` (`id_cashOut`)
) ENGINE=InnoDB AUTO_INCREMENT=15469 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `categorydish`
--

DROP TABLE IF EXISTS `categorydish`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorydish` (
  `id_categorydish` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(1000) DEFAULT NULL,
  `note` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id_categorydish`),
  UNIQUE KEY `id_categorydish_UNIQUE` (`id_categorydish`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `categoryfavorite`
--

DROP TABLE IF EXISTS `categoryfavorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoryfavorite` (
  `id_categoryFavorite` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `note` varchar(100) NOT NULL,
  PRIMARY KEY (`id_categoryFavorite`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `categorygoods`
--

DROP TABLE IF EXISTS `categorygoods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorygoods` (
  `id_categoryGoods` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `note` varchar(10000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id_categoryGoods`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `check`
--

DROP TABLE IF EXISTS `check`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `check` (
  `id_check` int NOT NULL AUTO_INCREMENT,
  `sum` decimal(9,2) DEFAULT NULL,
  `nal` decimal(9,2) DEFAULT NULL,
  `bnal` decimal(9,2) DEFAULT NULL,
  `money` decimal(9,2) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `bank` tinyint(1) DEFAULT NULL,
  `note` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `id_user` bigint NOT NULL,
  PRIMARY KEY (`id_check`),
  UNIQUE KEY `id_checkList_UNIQUE` (`id_check`),
  KEY `fk_checkList_userSwing1_idx` (`id_user`),
  CONSTRAINT `fk_checkList_userSwing1` FOREIGN KEY (`id_user`) REFERENCES `userswing` (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=107123 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `check_discount`
--

DROP TABLE IF EXISTS `check_discount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `check_discount` (
  `id_check` int NOT NULL,
  `id_discount` int NOT NULL,
  PRIMARY KEY (`id_check`),
  KEY `fk_check_discount_check1_idx` (`id_check`),
  KEY `fk_check_discount_discount1_idx` (`id_discount`),
  CONSTRAINT `fk_check_discount_check1` FOREIGN KEY (`id_check`) REFERENCES `check` (`id_check`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `checklist`
--

DROP TABLE IF EXISTS `checklist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `checklist` (
  `id_checkList` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(7,3) NOT NULL,
  `price` decimal(7,2) DEFAULT NULL,
  `profit` decimal(7,3) NOT NULL,
  `id_goods` bigint NOT NULL,
  `id_check` int NOT NULL,
  `new_price` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id_checkList`),
  KEY `fk_check1_goods1_idx` (`id_goods`),
  KEY `fk_check1_checkList1_idx` (`id_check`),
  CONSTRAINT `fk_check1_checkList1` FOREIGN KEY (`id_check`) REFERENCES `check` (`id_check`),
  CONSTRAINT `fk_check1_goods1` FOREIGN KEY (`id_goods`) REFERENCES `goods` (`id_goods`)
) ENGINE=InnoDB AUTO_INCREMENT=209553 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `checklist_newprice`
--

DROP TABLE IF EXISTS `checklist_newprice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `checklist_newprice` (
  `id_checkNewPrice` bigint NOT NULL AUTO_INCREMENT,
  `id_checkList` bigint NOT NULL,
  `new_price` decimal(7,3) NOT NULL,
  PRIMARY KEY (`id_checkNewPrice`),
  UNIQUE KEY `id_checkNewPrice_UNIQUE` (`id_checkNewPrice`),
  KEY `checkListNewPrice_idx` (`id_checkList`),
  CONSTRAINT `checkListNewPrice` FOREIGN KEY (`id_checkList`) REFERENCES `checklist` (`id_checkList`)
) ENGINE=InnoDB AUTO_INCREMENT=904 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `discount`
--

DROP TABLE IF EXISTS `discount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `discount` (
  `id_discount` int NOT NULL AUTO_INCREMENT,
  `numcard` varchar(50) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `lastname` varchar(50) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `date_register` datetime NOT NULL,
  `date_used` datetime NOT NULL,
  `percent` varchar(50) NOT NULL,
  `sum_checks` decimal(12,2) DEFAULT NULL,
  `classId` varchar(500) DEFAULT NULL,
  `objectId` varchar(500) DEFAULT NULL,
  `note` varchar(1000) DEFAULT NULL,
  `telegram_id_chat` bigint DEFAULT NULL,
  PRIMARY KEY (`id_discount`,`percent`)
) ENGINE=InnoDB AUTO_INCREMENT=450 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dish`
--

DROP TABLE IF EXISTS `dish`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dish` (
  `id_dish` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(1000) DEFAULT NULL,
  `code` varchar(50) DEFAULT NULL,
  `weight` decimal(10,3) DEFAULT NULL,
  `priceOpt` decimal(9,2) DEFAULT NULL,
  `id_categorydish` bigint DEFAULT NULL,
  UNIQUE KEY `id_dish_UNIQUE` (`id_dish`),
  KEY `id_dish_categorydish` (`id_categorydish`),
  CONSTRAINT `fk_dish_category` FOREIGN KEY (`id_categorydish`) REFERENCES `categorydish` (`id_categorydish`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `favorite`
--

DROP TABLE IF EXISTS `favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favorite` (
  `id_favorite` int NOT NULL AUTO_INCREMENT,
  `id_goods` bigint NOT NULL,
  `id_categoryFavorite` int NOT NULL,
  `buttonName` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id_favorite`),
  KEY `fk_favorite_goods1_idx` (`id_goods`),
  KEY `fk_favorite_categoryFavorite1_idx` (`id_categoryFavorite`)
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `goods`
--

DROP TABLE IF EXISTS `goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goods` (
  `id_goods` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `code` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `residue` decimal(7,3) DEFAULT NULL,
  `price` decimal(9,2) DEFAULT NULL,
  `id_categoryGoods` int DEFAULT NULL,
  `price_opt` decimal(9,2) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `marking` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id_goods`),
  UNIQUE KEY `id_goods_UNIQUE` (`id_goods`),
  KEY `fk_goods_categoryGoods1_idx` (`id_categoryGoods`),
  CONSTRAINT `fk_goods_categoryGoods1` FOREIGN KEY (`id_categoryGoods`) REFERENCES `categorygoods` (`id_categoryGoods`)
) ENGINE=InnoDB AUTO_INCREMENT=6396 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `goodsaccounting`
--

DROP TABLE IF EXISTS `goodsaccounting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goodsaccounting` (
  `id_goodsAccounting` bigint NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `checkin` tinyint(1) DEFAULT NULL,
  `residue_new` decimal(7,3) DEFAULT NULL,
  `residue_diff` decimal(7,3) DEFAULT NULL,
  `id_goods` bigint NOT NULL,
  PRIMARY KEY (`id_goodsAccounting`),
  UNIQUE KEY `id_goods_UNIQUE` (`id_goodsAccounting`),
  KEY `fk_goodsAccounting_goods1_idx` (`id_goods`),
  CONSTRAINT `fk_goodsAccounting_goods1` FOREIGN KEY (`id_goods`) REFERENCES `goods` (`id_goods`)
) ENGINE=InnoDB AUTO_INCREMENT=4834 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `goodsbackup`
--

DROP TABLE IF EXISTS `goodsbackup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goodsbackup` (
  `id_goods` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `code` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `residue` decimal(7,3) DEFAULT NULL,
  `price` decimal(9,2) DEFAULT NULL,
  `id_categoryGoods` int DEFAULT NULL,
  `price_opt` decimal(9,2) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `marking` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id_goods`),
  UNIQUE KEY `id_goods_UNIQUE` (`id_goods`),
  KEY `fk_goods_categoryGoods1_idx` (`id_categoryGoods`),
  CONSTRAINT `fk_goods_categoryGoods10` FOREIGN KEY (`id_categoryGoods`) REFERENCES `categorygoods` (`id_categoryGoods`)
) ENGINE=InnoDB AUTO_INCREMENT=5166 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id_order` int NOT NULL AUTO_INCREMENT,
  `payment` decimal(9,2) NOT NULL,
  `date` datetime NOT NULL,
  `note` varchar(1000) NOT NULL,
  `id_user` bigint NOT NULL,
  PRIMARY KEY (`id_order`),
  UNIQUE KEY `id_order_UNIQUE` (`id_order`),
  KEY `fk_orders_userSwing1_idx` (`id_user`),
  CONSTRAINT `fk_orders_userSwing1` FOREIGN KEY (`id_user`) REFERENCES `userswing` (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=4897 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reportsaccounting`
--

DROP TABLE IF EXISTS `reportsaccounting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reportsaccounting` (
  `id_reportsAccounting` bigint NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `residue_new` decimal(7,3) DEFAULT NULL,
  `residue_diff` decimal(7,3) DEFAULT NULL,
  `id_goods` bigint NOT NULL,
  PRIMARY KEY (`id_reportsAccounting`),
  UNIQUE KEY `id_goods_UNIQUE` (`id_reportsAccounting`),
  KEY `fk_reportsAccounting_goods1_idx` (`id_goods`),
  CONSTRAINT `fk_reportsAccounting_goods1` FOREIGN KEY (`id_goods`) REFERENCES `goods` (`id_goods`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `techmap`
--

DROP TABLE IF EXISTS `techmap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `techmap` (
  `id_techmap` bigint NOT NULL AUTO_INCREMENT,
  `gross` decimal(7,3) DEFAULT NULL,
  `id_dish` bigint DEFAULT NULL,
  `id_goods` bigint DEFAULT NULL,
  PRIMARY KEY (`id_techmap`),
  UNIQUE KEY `id_techmap_UNIQUE` (`id_techmap`),
  KEY `id_dish` (`id_dish`),
  KEY `id_goods` (`id_goods`),
  CONSTRAINT `fk_dish_techmap` FOREIGN KEY (`id_dish`) REFERENCES `dish` (`id_dish`),
  CONSTRAINT `fk_techamp_goods` FOREIGN KEY (`id_goods`) REFERENCES `goods` (`id_goods`)
) ENGINE=InnoDB AUTO_INCREMENT=120 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `total`
--

DROP TABLE IF EXISTS `total`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `total` (
  `id_total` int NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL,
  `spare` decimal(9,2) DEFAULT NULL,
  `sumGoods` decimal(9,2) NOT NULL,
  `profit` decimal(9,2) DEFAULT NULL,
  `proceeds` decimal(9,2) DEFAULT NULL,
  `nal` decimal(9,2) DEFAULT NULL,
  `bnal` decimal(9,2) DEFAULT NULL,
  `id_user` bigint NOT NULL,
  PRIMARY KEY (`id_total`),
  KEY `fk_total_new_userSwing1_idx` (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=2138 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trial`
--

DROP TABLE IF EXISTS `trial`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trial` (
  `id_trial` int NOT NULL AUTO_INCREMENT,
  `firstdate` datetime NOT NULL,
  `lastdate` datetime NOT NULL,
  `created` tinyint(1) NOT NULL,
  `license` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_trial`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_roles` (
  `idRole` int NOT NULL AUTO_INCREMENT,
  `authority` varchar(45) NOT NULL,
  PRIMARY KEY (`idRole`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `userswing`
--

DROP TABLE IF EXISTS `userswing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `userswing` (
  `id_user` bigint NOT NULL AUTO_INCREMENT,
  `login` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `last_login` datetime NOT NULL,
  `active` tinyint(1) DEFAULT NULL,
  `pass` varchar(50) NOT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `admin` int NOT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE KEY `id_user_UNIQUE` (`id_user`),
  KEY `fk_userSwing_user_roles1_idx` (`admin`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `writeoff`
--

DROP TABLE IF EXISTS `writeoff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `writeoff` (
  `id_writeoff` bigint NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL,
  `sum` decimal(9,2) DEFAULT NULL,
  `note` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `id_user` bigint NOT NULL,
  PRIMARY KEY (`id_writeoff`),
  UNIQUE KEY `id_writeoffList_UNIQUE` (`id_writeoff`),
  KEY `fk_writeoff_userSwing1_idx` (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=22143 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `writeofflist`
--

DROP TABLE IF EXISTS `writeofflist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `writeofflist` (
  `id_writeoffList` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(7,3) NOT NULL,
  `price` decimal(7,2) DEFAULT NULL,
  `id_goods` bigint NOT NULL,
  `id_writeoff` bigint NOT NULL,
  PRIMARY KEY (`id_writeoffList`),
  KEY `fk_check1_goods1_idx` (`id_goods`),
  KEY `fk_writeoffList_writeoff1_idx` (`id_writeoff`),
  CONSTRAINT `fk_check1_goods10` FOREIGN KEY (`id_goods`) REFERENCES `goods` (`id_goods`),
  CONSTRAINT `fk_writeoffList_writeoff1` FOREIGN KEY (`id_writeoff`) REFERENCES `writeoff` (`id_writeoff`)
) ENGINE=InnoDB AUTO_INCREMENT=39057 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-15 11:55:46
