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
-- Table structure for table `sac_heading_master`
--

DROP TABLE IF EXISTS `sac_heading_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sac_heading_master` (
  `heading_id` varchar(50) NOT NULL,
  `heading_desc` varchar(1000) NOT NULL,
  PRIMARY KEY (`heading_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sac_heading_master`
--

LOCK TABLES `sac_heading_master` WRITE;
/*!40000 ALTER TABLE `sac_heading_master` DISABLE KEYS */;
INSERT INTO `sac_heading_master` VALUES ('9954','Construction services'),('9961','Services in wholesale trade'),('9962','Services in retail trade'),('9963','Accommodation, Food and beverage services'),('9964','Passenger transport services'),('9965','Goods Transport Services'),('9966','Rental services of transport vehicles with or without operators'),('9967','Supporting services in transport'),('9968','Postal and courier services'),('9969','Electricity, gas, water and other distribution services'),('9971','Financial and related services'),('9972','Real estate services'),('9973','Leasing or rental services with or without operator'),('9981','Research and development services'),('9982','Legal and accounting services'),('9983','Other professional, technical and business services'),('9984','Telecommunications, broadcasting and information supply services'),('9985','Support services'),('9986','Support services to agriculture, hunting, forestry, fishing, mining and utilities.'),('9987','Maintenance, repair and installation (except construction) services'),('9988','Manufacturing services on physical inputs (goods) owned by others'),('9989','Other manufacturing services; publishing, printing and reproduction services; materials recovery services'),('9991','Public administration and other services provided to the community as a whole; compulsory social security services'),('9992','Education services'),('9993','Human health and social care services'),('9994','Sewage and waste collection, treatment and disposal and other environmental protection services'),('9995','Services of membership organizations'),('9996','Recreational, cultural and sporting services'),('9997','Other services'),('9998','Domestic services'),('9999','Services provided by extraterritorial organizations and bodies.');
/*!40000 ALTER TABLE `sac_heading_master` ENABLE KEYS */;
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
