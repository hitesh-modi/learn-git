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
-- Table structure for table `chapter_section_map`
--

DROP TABLE IF EXISTS `chapter_section_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chapter_section_map` (
  `section_id` varchar(5) NOT NULL,
  `chapter_id` varchar(10) NOT NULL,
  PRIMARY KEY (`chapter_id`),
  KEY `group_id` (`chapter_id`),
  KEY `chapter_section_map_ibfk_1` (`section_id`),
  CONSTRAINT `chapter_section_map_ibfk_1` FOREIGN KEY (`section_id`) REFERENCES `section_master` (`section_id`),
  CONSTRAINT `chapter_section_map_ibfk_2` FOREIGN KEY (`chapter_id`) REFERENCES `hsnchapter_master` (`hsnchapter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chapter_section_map`
--

LOCK TABLES `chapter_section_map` WRITE;
/*!40000 ALTER TABLE `chapter_section_map` DISABLE KEYS */;
INSERT INTO `chapter_section_map` VALUES ('sec1','ch01'),('sec1','ch02'),('sec1','ch03'),('sec1','ch04'),('sec1','ch05'),('sec10','ch47'),('sec10','ch48'),('sec10','ch49'),('sec11','ch50'),('sec11','ch51'),('sec11','ch52'),('sec11','ch53'),('sec11','ch54'),('sec11','ch55'),('sec11','ch56'),('sec11','ch57'),('sec11','ch58'),('sec11','ch59'),('sec11','ch60'),('sec11','ch61'),('sec11','ch62'),('sec11','ch63'),('sec12','ch64'),('sec12','ch65'),('sec12','ch66'),('sec12','ch67'),('sec13','ch68'),('sec13','ch69'),('sec13','ch70'),('sec14','ch71'),('sec15','ch72'),('sec15','ch73'),('sec15','ch74'),('sec15','ch75'),('sec15','ch76'),('sec15','ch78'),('sec15','ch79'),('sec15','ch80'),('sec15','ch81'),('sec15','ch82'),('sec15','ch83'),('sec16','ch84'),('sec16','ch85'),('sec17','ch86'),('sec17','ch87'),('sec17','ch88'),('sec17','ch89'),('sec18','ch90'),('sec18','ch91'),('sec18','ch92'),('sec19','ch93'),('sec2','ch06'),('sec2','ch07'),('sec2','ch08'),('sec2','ch09'),('sec2','ch10'),('sec2','ch11'),('sec2','ch12'),('sec2','ch13'),('sec2','ch14'),('sec20','ch94'),('sec20','ch95'),('sec20','ch96'),('sec3','ch15'),('sec4','ch16'),('sec4','ch17'),('sec4','ch18'),('sec4','ch19'),('sec4','ch20'),('sec4','ch21'),('sec4','ch22'),('sec4','ch23'),('sec4','ch24'),('sec5','ch25'),('sec5','ch26'),('sec5','ch27'),('sec6','ch28'),('sec6','ch29'),('sec6','ch30'),('sec6','ch31'),('sec6','ch32'),('sec6','ch33'),('sec6','ch34'),('sec6','ch35'),('sec6','ch36'),('sec6','ch37'),('sec6','ch38'),('sec7','ch39'),('sec7','ch40'),('sec8','ch41'),('sec8','ch42'),('sec8','ch43'),('sec9','ch44'),('sec9','ch45'),('sec9','ch46');
/*!40000 ALTER TABLE `chapter_section_map` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-08-13  1:03:46
