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
-- Table structure for table `invoicedetails`
--

DROP TABLE IF EXISTS `invoicedetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoicedetails` (
  `ID_InvoiceId` varchar(50) NOT NULL,
  `ID_CustomerId` int(11) NOT NULL,
  `ID_InvoiceDate` date NOT NULL,
  `ID_InvoiceTotalAmount` decimal(10,2) NOT NULL,
  `ID_TaxAmount` decimal(10,2) NOT NULL,
  `ID_TaxId` int(11) DEFAULT NULL,
  `ID_InvoiceBalanceAmount` decimal(10,2) DEFAULT NULL,
  `ID_InvoicePaidAmount` decimal(10,2) DEFAULT NULL,
  `ID_CreationTimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ID_ModificationTimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ID_ConsigneeId` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_InvoiceId`),
  KEY `ID_CustomerId` (`ID_CustomerId`),
  KEY `ID_TaxId` (`ID_TaxId`),
  KEY `ID_ConsigneeId` (`ID_ConsigneeId`),
  CONSTRAINT `invoicedetails_ibfk_1` FOREIGN KEY (`ID_CustomerId`) REFERENCES `customer_details` (`CD_CUSTOMER_ID`),
  CONSTRAINT `invoicedetails_ibfk_2` FOREIGN KEY (`ID_TaxId`) REFERENCES `taxrates` (`TR_TaxId`),
  CONSTRAINT `invoicedetails_ibfk_3` FOREIGN KEY (`ID_ConsigneeId`) REFERENCES `consignee_details` (`CONSIGNEE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoicedetails`
--

LOCK TABLES `invoicedetails` WRITE;
/*!40000 ALTER TABLE `invoicedetails` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoicedetails` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-08-13  1:03:49
