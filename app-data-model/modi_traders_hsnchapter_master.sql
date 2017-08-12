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
-- Table structure for table `hsnchapter_master`
--

DROP TABLE IF EXISTS `hsnchapter_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hsnchapter_master` (
  `hsnchapter_id` varchar(10) NOT NULL,
  `hsnchapter_desc` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`hsnchapter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hsnchapter_master`
--

LOCK TABLES `hsnchapter_master` WRITE;
/*!40000 ALTER TABLE `hsnchapter_master` DISABLE KEYS */;
INSERT INTO `hsnchapter_master` VALUES ('ch01','Live animals'),('ch02','Meat and edible meat offal'),('ch03','Fish and crustaceans, molluscs and other aquatic invertebrates'),('ch04','Dairy produce; birds\' eggs; natural honey; edible products of animal origin, not elsewhere specified or included'),('ch05','Products of animal origin, not elsewhere specified or included'),('ch06','Live trees and other plants; bulbs, roots and the like; cut flowers and ornamental foliage'),('ch07','Edible vegetables and certain roots and tubers'),('ch08','Edible fruit and nuts; peel of citrus fruit or melons'),('ch09','Coffee, tea, matÉ and spices'),('ch10','Cereals'),('ch11','Products of the milling industry; malt; starches; inulin; wheat gluten'),('ch12','Oil seeds and oleaginous fruits; miscellaneous grains, seeds and fruit; industrial or medicinal plants; straw and fodder'),('ch13','Lac; gums, resins and other vegetable saps and extracts'),('ch14','Vegetable plaiting materials; vegetable products not elsewhere specified or included'),('ch15','Animal or vegetable fats and oils and their cleavage products; prepared edible fats; animal or vegetable waxes'),('ch16','Preparations of meat, of fish or of crustaceans, molluscs or other aquatic invertebrates'),('ch17','Sugars and sugar confectionery'),('ch18','Cocoa and cocoa preparations'),('ch19','Preparations of cereals, flour, starch or milk; pastrycooks\' products'),('ch20','Preparations of vegetables, fruit, nuts or other parts of plants'),('ch21','Miscellaneous edible preparations'),('ch22','Beverages, spirits and vinegar'),('ch23','Residues and waste from the food industries; prepared animal fodder'),('ch24','Tobacco and manufactured tobacco substitutes'),('ch25','Salt; sulphur; earths and stone; plastering materials, lime and cement'),('ch26','Ores, slag and ash'),('ch27','Mineral fuels, mineral oils and products of their distillation; bituminous substances; mineral waxes'),('ch28','Inorganic chemicals; organic or inorganic compounds of precious metals, of rare-earth metals, of radioactive elements or of isotopes'),('ch29','Organic chemicals'),('ch30','Pharmaceutical products'),('ch31','Fertilisers'),('ch32','Tanning or dyeing extracts; tannins and their derivatives; dyes, pigments and other colouring matter; paints and varnishes; putty and other mastics; inks'),('ch33','Essential oils and resinoids; perfumery, cosmetic or toilet preparations'),('ch34','Soap, organic surface-active agents, washing preparations, lubricating preparations, artificial waxes, prepared waxes, polishing or scouring preparations, candles and similar articles, modelling pastes, \'dental waxes\' and dental preparations with a basis'),('ch35','Albuminoidal substances; modified starches; glues; enzymes'),('ch36','Explosives; pyrotechnic products; matches; pyrophoric alloys; certain combustible preparations'),('ch37','Photographic or cinematographic goods'),('ch38','Miscellaneous chemical products'),('ch39','Plastics and articles thereof'),('ch40','Rubber and articles thereof'),('ch41','Raw hides and skins (other than furskins) and leather'),('ch42','Articles of leather; saddlery and harness; travel goods, handbags and similar containers; articles of animal gut (other than silkworm gut)'),('ch43','Furskins and artificial fur; manufactures thereof'),('ch44','Wood and articles of wood; wood charcoal'),('ch45','Cork and articles of cork'),('ch46','Manufactures of straw, of esparto or of other plaiting materials; basketware and wickerwork'),('ch47','Pulp of wood or of other fibrous cellulosic material; recovered (waste and scrap) paper or paperboard'),('ch48','Paper and paperboard; articles of paper pulp, of paper or of paperboard'),('ch49','Printed books, newspapers, pictures and other products of the printing industry; manuscripts, typescripts and plans'),('ch50','Silk'),('ch51','Wool, fine or coarse animal hair; horsehair yarn and woven fabric'),('ch52','Cotton'),('ch53','Other vegetable textile fibres; paper yarn and woven fabrics of paper yarn'),('ch54','Man-made filaments; strip and the like of man-made textile materials'),('ch55','Man-made staple fibres'),('ch56','Wadding, felt and nonwovens; special yarns; twine, cordage, ropes and cables and articles thereof'),('ch57','Carpets and other textile floor coverings'),('ch58','Special woven fabrics; tufted textile fabrics; lace; tapestries; trimmings; embroidery'),('ch59','Impregnated, coated, covered or laminated textile fabrics; textile articles of a kind suitable for industrial use'),('ch60','Knitted or crocheted fabrics'),('ch61','Articles of apparel and clothing accessories, knitted or crocheted'),('ch62','Articles of apparel and clothing accessories, not knitted or crocheted'),('ch63','Other made-up textile articles; sets; worn clothing and worn textile articles; rags'),('ch64','Footwear, gaiters and the like; parts of such articles'),('ch65','Headgear and parts thereof'),('ch66','Umbrellas, sun umbrellas, walking sticks, seat-sticks, whips, riding-crops and parts thereof'),('ch67','Prepared feathers and down and articles made of feathers or of down; artificial flowers; articles of human hair'),('ch68','Articles of stone, plaster, cement, asbestos, mica or similar materials'),('ch69','Ceramic products'),('ch70','Glass and glassware'),('ch71','Natural or cultured pearls, precious or semi-precious stones, precious metals, metals clad with precious metal, and articles thereof; imitation jewellery; coin'),('ch72','Iron and steel'),('ch73','Articles of iron or steel'),('ch74','Copper and articles thereof'),('ch75','Nickel and articles thereof'),('ch76','Aluminium and articles thereof'),('ch78','Lead and articles thereof'),('ch79','Zinc and articles thereof'),('ch80','Tin and articles thereof'),('ch81','Other base metals; cermets; articles thereof'),('ch82','Tools, implements, cutlery, spoons and forks, of base metal; parts thereof of base metal'),('ch83','Miscellaneous articles of base metal'),('ch84','Nuclear reactors, boilers, machinery and mechanical appliances; parts thereof'),('ch85','Electrical machinery and equipment and parts thereof; sound recorders and reproducers, television image and sound recorders and reproducers, and parts and accessories of such articles'),('ch86','Railway or tramway locomotives, rolling stock and parts thereof; railway or tramway track fixtures and fittings and parts thereof; mechanical (including electromechanical) traffic signalling equipment of all kinds'),('ch87','Vehicles other than railway or tramway rolling stock, and parts and accessories thereof'),('ch88','Aircraft, spacecraft, and parts thereof'),('ch89','Ships, boats and floating structures'),('ch90','Optical, photographic, cinematographic, measuring, checking, precision, medical or surgical instruments and apparatus; parts and accessories thereof'),('ch91','Clocks and watches and parts thereof'),('ch92','Musical instruments; parts and accessories of such articles'),('ch93','Arms and ammunition; parts and accessories thereof'),('ch94','Furniture; bedding, mattresses, mattress supports, cushions and similar stuffed furnishings; lamps and lighting fittings, not elsewhere specified or included; illuminated signs, illuminated nameplates and the like; prefabricated buildings'),('ch95','Toys, games and sports requisites; parts and accessories thereof'),('ch96','Miscellaneous manufactured articles');
/*!40000 ALTER TABLE `hsnchapter_master` ENABLE KEYS */;
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
