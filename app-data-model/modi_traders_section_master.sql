CREATE DATABASE  IF NOT EXISTS `modi_traders` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `modi_traders`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: modi_traders
-- ------------------------------------------------------
-- Server version	5.7.18-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `section_master`
--

DROP TABLE IF EXISTS `section_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `section_master` (
  `section_id` varchar(5) NOT NULL,
  `section_desc` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`section_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `section_master`
--

LOCK TABLES `section_master` WRITE;
/*!40000 ALTER TABLE `section_master` DISABLE KEYS */;
INSERT INTO `section_master` VALUES ('sec1','Section I (Chapters 1 to 5) covers live animals and animal products'),('sec10','Section X (Chapters 47 to 49) covers pulp of wood, paper, paperboard, and printed products'),('sec11','Section XI (Chapters 50 to 63) covers textiles and textile articles'),('sec12','Section XII (Chapters 64 to 67) covers footwear, headgear, umbrellas, walking sticks, prepared feathers, artificial flowers, and articles of human hair'),('sec13','Section XIII (Chapters 68 to 70) covers articles made of minerals, stone, plaster, cement, etc., and ceramic and glass products'),('sec14','Section XIV (Chapter 71) covers precious metals and stones'),('sec15','Section XV (Chapters 72 to 83) covers base metals and articles thereof (Note: Chapter 77 is reserve for future use)'),('sec16','Section XVI (Chapters 84 to 85) covers machinery and mechanical appliances, electrical equipment, sound recorders and reproducers, television image and sound recorders and reproducers, and parts and accessories of such articles'),('sec17','Section XVII (Chapters 86 to 89) covers vehicles, aircraft, vessels, and associated transport equipment'),('sec18','Section XVIII (Chapters 90 to 92) covers optical, photographic, cinematographic, and musical apparatus and equipment; measuring, medical, surgical, and other instruments; and clocks and watches'),('sec19','Section XIX (Chapter 93) covers arms and ammunitions'),('sec2','Section II (Chapters 6 to 14) covers vegetable products'),('sec20','Section XX (Chapters 94 to 96) covers miscellaneous manufactured articles'),('sec3','Section III (Chapter 15) covers animal or vegetable fats and oils'),('sec4','Section IV (Chapters 16 to 24) covers beverages, spirits, vinegar, and tobacco'),('sec5','Section V (Chapters 25 to 27) covers mineral products'),('sec6','Section VI (Chapters 28 to 38) covers chemical and para-chemical products'),('sec7','Section VII (Chapters 39 to 40) covers plastics and rubber, and articles thereof'),('sec8','Section VIII (Chapters 41 to 43) covers certain animal hides and skins'),('sec9','Section IX (Chapters 44 to 46) covers wood, cork, manufactures of straw, and articles thereof');
/*!40000 ALTER TABLE `section_master` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-08-13  1:03:45
