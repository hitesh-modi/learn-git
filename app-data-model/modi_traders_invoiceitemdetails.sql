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
-- Table structure for table `invoiceitemdetails`
--

DROP TABLE IF EXISTS `invoiceitemdetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoiceitemdetails` (
  `IID_ItemId` int(11) NOT NULL AUTO_INCREMENT,
  `IID_ItemType` varchar(20) NOT NULL,
  `IID_ServiceId` int(11) DEFAULT NULL,
  `IID_ServiceStartDate` date DEFAULT NULL,
  `IID_ServiceEndDate` date DEFAULT NULL,
  `IID_ProductId` int(11) DEFAULT NULL,
  `IID_ProductQuantity` int(11) DEFAULT NULL,
  `IID_InvoiceId` varchar(50) DEFAULT NULL,
  `IID_ItemPrice` decimal(10,2) DEFAULT NULL,
  `IID_ItemDiscount` decimal(10,2) DEFAULT NULL,
  `iid_taxableamount` decimal(10,0) DEFAULT NULL,
  `iid_igst` decimal(10,0) DEFAULT NULL,
  `iid_sgst` decimal(10,0) DEFAULT NULL,
  `iid_cgst` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (`IID_ItemId`),
  KEY `IID_ServiceId` (`IID_ServiceId`),
  KEY `IID_ProductId` (`IID_ProductId`),
  KEY `IID_InvoiceId` (`IID_InvoiceId`),
  CONSTRAINT `invoiceitemdetails_ibfk_1` FOREIGN KEY (`IID_ServiceId`) REFERENCES `servicedetails` (`SD_ServiceId`),
  CONSTRAINT `invoiceitemdetails_ibfk_2` FOREIGN KEY (`IID_ProductId`) REFERENCES `productdetails` (`PD_ProductId`),
  CONSTRAINT `invoiceitemdetails_ibfk_3` FOREIGN KEY (`IID_InvoiceId`) REFERENCES `invoicedetails` (`ID_InvoiceId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoiceitemdetails`
--

LOCK TABLES `invoiceitemdetails` WRITE;
/*!40000 ALTER TABLE `invoiceitemdetails` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoiceitemdetails` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-08-13  1:03:48
