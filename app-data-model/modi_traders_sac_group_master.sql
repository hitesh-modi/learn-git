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
-- Table structure for table `sac_group_master`
--

DROP TABLE IF EXISTS `sac_group_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sac_group_master` (
  `group_id` varchar(50) NOT NULL,
  `group_desc` varchar(2000) NOT NULL,
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sac_group_master`
--

LOCK TABLES `sac_group_master` WRITE;
/*!40000 ALTER TABLE `sac_group_master` DISABLE KEYS */;
INSERT INTO `sac_group_master` VALUES ('99541','Construction services of buildings'),('99542','General construction services of civil engineering works'),('99543','Site preparation services'),('99544','Assembly and erection of prefabricated constructions'),('99545','Special trade construction services'),('99546','Installation services'),('99547','Building completion and finishing services'),('99611','Services in wholesale trade'),('99621','Services in retail trade'),('99631','Accommodation services'),('99632','Other accommodation services'),('99633','Food, edible preparations, alchoholic & non-alchocholic beverages serving services'),('99641','Local transport and sightseeing transportation services of passengers'),('99642','Long-distance transport services of passengers'),('99651','Land transport services of Goods'),('99652','Water transport services of goods'),('99653','Air and space transport services of goods'),('99660','Rental services of transport vehicles with or without operators'),('99671','Cargo handling services'),('99672','Storage and warehousing services'),('99673','Supporting services for railway transport'),('99674','Supporting services for road transport'),('99675','Supporting services for water transport (coastal, transoceanic and inland waterways)'),('99676','Supporting services for air or space transport'),('99679','Other supporting transport services'),('99681','Postal and courier services'),('99691','Electricity and gas distribution services'),('99692','Water distribution and other services'),('99711','Financial services (except investment banking, insurance services and pension services)'),('99712','Investment banking services '),('99713','Insurance and pension services (excluding reinsurance services)'),('99714','Reinsurance services'),('99715','Services auxiliary to financial services (other than to insurance and pensions)'),('99716','Services auxillary to insurance and pensions'),('99717','Services of holding financial assets'),('99721','Real estate services involving owned or leased property'),('99722','Real estate services on a fee/commission basis or contract basis'),('99731','Leasing or rental services concerning machinery and equipment with or without operator'),('99732','Leasing or rental services concerning other goods'),('99733','Licensing services for the right to use intellectual property and similar products'),('99811','Research and experimental development services in natural sciences and engineering.'),('99812','Research and experimental development services in social sciences and humanities.'),('99813','Interdisciplinary research services.'),('99814','Research and development originals'),('99821','Legal services'),('99822','Accounting, auditing and bookkeeping services'),('99823','Tax consultancy and preparation services'),('99824','Insolvency and receivership services'),('99831','Management consulting and management services; information technology services.'),('99832','Architectural services, urban and land planning and landscape architectural services'),('99833','Engineering services'),('99834','Scientific and other technical services'),('99835','Veterinary services'),('99836','Advertising services and provision of advertising space or time.'),('99837','Market research and public opinion polling services'),('99838','Photography & Videography and their processing services'),('99839','Other professional, technical and business services.'),('99841','Telephony and other telecommunications services'),('99842','Internet telecommunications services'),('99843','On-line content services'),('99844','News agency services'),('99845','Library and archive services'),('99846','Broadcasting, programming and programme distribution services'),('99851','Employment services including personnel search/referral service & labour supply service'),('99852','Investigation and security services'),('99853','Cleaning services'),('99854','Packaging services'),('99855','Travel arrangement, tour operator and related services'),('99859','Other support services'),('99861','Support services to agriculture, hunting, forestry and fishing'),('99862','Support services to mining'),('99863','Support services to electricity, gas and water distribution'),('99871','Maintenance and repair services of fabricated metal products, machinery and equipment'),('99872','Repair services of other goods'),('99873','Installation services (other than construction)'),('99881','Food, beverage and tobacco manufacturing services'),('99882','Textile, wearing apparel and leather manufacturing services'),('99883','Wood and paper manufacturing services'),('99884','Petroleum, chemical and pharmaceutical product manufacturing services'),('99885','Rubber, plastic and other non-metallic mineral product manufacturing service'),('99886','Basic metal manufacturing services'),('99887','Fabricated metal product, machinery and equipment manufacturing services'),('99888','Transport equipment manufacturing services'),('99889','Other manufacturing services'),('99891','Publishing, printing and reproduction services'),('99892','Moulding, pressing, stamping, extruding and similar plastic manufacturing services'),('99893','Casting, forging, stamping and similar metal manufacturing services'),('99894','Materials recovery (recycling) services, on a fee or contract basis'),('99911','Administrative services of the government'),('99912','Public administrative services provided to the community as a whole'),('99913','Administrative services related to compulsory social security schemes.'),('99921','Pre-primary education services'),('99922','Primary education services'),('99923','Secondary Education Services'),('99924','Higher education services'),('99925','Specialised education services'),('99929','Other education & training services and educational support services'),('99931','Human health services'),('99932','Residential care services for the elderly and disabled'),('99933','Other social services with accommodation'),('99934','Social services without accommodation for the elderly and disabled'),('99935','Other social services without accommodation'),('99941','Sewerage, sewage treatment and septic tank cleaning services'),('99942','Waste collection services'),('99943','Waste treatment and disposal services'),('99944','Remediation services'),('99945','Sanitation and similar services'),('99949','Others'),('99951','Services furnished by business, employers and professional organizations Services'),('99952','Services furnished by trade unions'),('99959','Services furnished by other membership organizations'),('99961','Audiovisual and related services'),('99962','Performing arts and other live entertainment event presentation and promotion services'),('99963','Services of performing and other artists'),('99964','Museum and preservation services'),('99965','Sports and recreational sports services'),('99966','Services of athletes and related support services'),('99969','Other amusement and recreational services'),('99971','Washing, cleaning and dyeing services'),('99972','Beauty and physical well-being services'),('99973','Funeral, cremation and undertaking services'),('99979','Other miscellaneous services'),('99980','Domestic services'),('99990','Services provided by extraterritorial organizations and bodies.');
/*!40000 ALTER TABLE `sac_group_master` ENABLE KEYS */;
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
