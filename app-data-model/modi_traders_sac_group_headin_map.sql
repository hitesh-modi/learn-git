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
-- Table structure for table `sac_group_headin_map`
--

DROP TABLE IF EXISTS `sac_group_headin_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sac_group_headin_map` (
  `heading_id` varchar(50) NOT NULL,
  `group_id` varchar(50) NOT NULL,
  PRIMARY KEY (`group_id`),
  KEY `heading_id` (`heading_id`),
  CONSTRAINT `sac_group_headin_map_ibfk_1` FOREIGN KEY (`heading_id`) REFERENCES `sac_heading_master` (`heading_id`),
  CONSTRAINT `sac_group_headin_map_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `sac_group_master` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sac_group_headin_map`
--

LOCK TABLES `sac_group_headin_map` WRITE;
/*!40000 ALTER TABLE `sac_group_headin_map` DISABLE KEYS */;
INSERT INTO `sac_group_headin_map` VALUES ('9954','99541'),('9954','99542'),('9954','99543'),('9954','99544'),('9954','99545'),('9954','99546'),('9954','99547'),('9961','99611'),('9962','99621'),('9963','99631'),('9963','99632'),('9963','99633'),('9964','99641'),('9964','99642'),('9965','99651'),('9965','99652'),('9965','99653'),('9966','99660'),('9967','99671'),('9967','99672'),('9967','99673'),('9967','99674'),('9967','99675'),('9967','99676'),('9967','99679'),('9968','99681'),('9969','99691'),('9969','99692'),('9971','99711'),('9971','99712'),('9971','99713'),('9971','99714'),('9971','99715'),('9971','99716'),('9971','99717'),('9972','99721'),('9972','99722'),('9973','99731'),('9973','99732'),('9973','99733'),('9981','99811'),('9981','99812'),('9981','99813'),('9981','99814'),('9982','99821'),('9982','99822'),('9982','99823'),('9982','99824'),('9983','99831'),('9983','99832'),('9983','99833'),('9983','99834'),('9983','99835'),('9983','99836'),('9983','99837'),('9983','99838'),('9983','99839'),('9984','99841'),('9984','99842'),('9984','99843'),('9984','99844'),('9984','99845'),('9984','99846'),('9985','99851'),('9985','99852'),('9985','99853'),('9985','99854'),('9985','99855'),('9985','99859'),('9986','99861'),('9986','99862'),('9986','99863'),('9987','99871'),('9987','99872'),('9987','99873'),('9988','99881'),('9988','99882'),('9988','99883'),('9988','99885'),('9988','99886'),('9988','99887'),('9988','99888'),('9988','99889'),('9989','99891'),('9989','99892'),('9989','99893'),('9989','99894'),('9991','99911'),('9991','99912'),('9991','99913'),('9992','99921'),('9992','99922'),('9992','99923'),('9992','99924'),('9992','99925'),('9992','99929'),('9993','99931'),('9993','99932'),('9993','99933'),('9993','99934'),('9993','99935'),('9994','99941'),('9994','99942'),('9994','99943'),('9994','99944'),('9994','99945'),('9994','99949'),('9995','99951'),('9995','99952'),('9995','99959'),('9996','99961'),('9996','99962'),('9996','99963'),('9996','99964'),('9996','99965'),('9996','99966'),('9996','99969'),('9997','99971'),('9997','99972'),('9997','99973'),('9997','99979'),('9998','99980'),('9999','99990');
/*!40000 ALTER TABLE `sac_group_headin_map` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-08-13  1:03:44
