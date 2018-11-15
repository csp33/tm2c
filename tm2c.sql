-- MySQL dump 10.13  Distrib 5.7.24, for Linux (x86_64)
--
-- Host: localhost    Database: tm2c
-- ------------------------------------------------------
-- Server version	5.7.24-0ubuntu0.18.04.1

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
-- Table structure for table `building`
--

CREATE DATABASE tm2c;
USE tm2c;

DROP TABLE IF EXISTS `building`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `building` (
  `building_code` varchar(4) NOT NULL,
  `location` varchar(255) NOT NULL,
  PRIMARY KEY (`building_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `building`
--

LOCK TABLES `building` WRITE;
/*!40000 ALTER TABLE `building` DISABLE KEYS */;
INSERT INTO `building` VALUES ('A','https://goo.gl/maps/1dFi1322mi22'),('AE','https://goo.gl/maps/8HZAKhuNVEJ2'),('CH','https://goo.gl/maps/hLi7cu1Mnb22'),('D','https://goo.gl/maps/AXAB8WwkEX32'),('DCS','https://goo.gl/maps/oL3YZcZHbiw'),('E','https://goo.gl/maps/uMZnykmDpQS2'),('EL','https://goo.gl/maps/ezGqPZazYd22'),('F','https://goo.gl/maps/Dr387bVRFTn'),('Fa','https://goo.gl/maps/L58EdYKena92'),('G','https://goo.gl/maps/dy8nMpcfckv'),('H','https://goo.gl/maps/vJP5VDvv1iT2'),('Hö','https://goo.gl/maps/cKg3Y4EQjws'),('I','https://goo.gl/maps/eq2uqHT3Sk82'),('J','https://goo.gl/maps/TvSTVbfhSNT2'),('K','https://goo.gl/maps/z3GsGwtqcb12'),('KO','https://goo.gl/maps/CvR3PcuwjwM2'),('L','https://goo.gl/maps/JBcQNwmp1mF2'),('MM','https://goo.gl/maps/qaz3McJvV612'),('MT','https://goo.gl/maps/RQos2iw5qQ12'),('Q','https://goo.gl/maps/uMZnykmDpQS2'),('R','https://goo.gl/maps/AdTWmLPR6pN2'),('St','https://goo.gl/maps/La7WNPiTz342'),('T','https://goo.gl/maps/hw9TFG8M6sL2'),('TR','https://goo.gl/maps/NLJmabV6tn62'),('U','https://goo.gl/maps/qJ2WBchpsy92'),('V1','https://goo.gl/maps/prD4diiwey22'),('V2','https://goo.gl/maps/eyPGBcktJR72'),('Z','https://goo.gl/maps/CD8xTMALmky');
/*!40000 ALTER TABLE `building` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `course` (
  `course_code` varchar(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`course_code`),
  UNIQUE KEY `course_code` (`course_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES ('BMM23','Trial'),('ETTHV52','Management'),('KVH4','Cisco'),('sa','try'),('VeTMABf3','Optical'),('VITMABf3','Databases'),('VITMAV52','Information and Network Security');
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `imparted`
--

DROP TABLE IF EXISTS `imparted`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `imparted` (
  `course_code` varchar(10) NOT NULL,
  `room_code` varchar(10) NOT NULL,
  `day` varchar(10) NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  PRIMARY KEY (`course_code`,`room_code`,`day`,`start_time`,`end_time`),
  KEY `ROOM_FK` (`room_code`),
  CONSTRAINT `COURSE_FK` FOREIGN KEY (`course_code`) REFERENCES `course` (`course_code`),
  CONSTRAINT `ROOM_FK` FOREIGN KEY (`room_code`) REFERENCES `room` (`room_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `imparted`
--

LOCK TABLES `imparted` WRITE;
/*!40000 ALTER TABLE `imparted` DISABLE KEYS */;
INSERT INTO `imparted` VALUES ('VITMABf3','E306cd','Thursday','13:15:00','15:00:00'),('VeTMABf3','G306cd','Monday','12:15:00','15:00:00'),('ETTHV52','GB246','MONDAY','10:15:00','14:00:00'),('KVH4','HB246','THURSDAY','16:15:00','20:00:00'),('VITMAV52','IB146','Wednesday','12:15:00','14:00:00'),('BMM23','IB147','MONDAY','12:00:00','13:00:00'),('sa','IB147','TUESDAY','22:00:00','23:00:00');
/*!40000 ALTER TABLE `imparted` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `is_registered`
--

DROP TABLE IF EXISTS `is_registered`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `is_registered` (
  `username` varchar(255) NOT NULL,
  `course_code` varchar(10) NOT NULL,
  PRIMARY KEY (`username`,`course_code`),
  KEY `FK2` (`course_code`),
  CONSTRAINT `FK1` FOREIGN KEY (`username`) REFERENCES `student` (`username`),
  CONSTRAINT `FK2` FOREIGN KEY (`course_code`) REFERENCES `course` (`course_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `is_registered`
--

LOCK TABLES `is_registered` WRITE;
/*!40000 ALTER TABLE `is_registered` DISABLE KEYS */;
INSERT INTO `is_registered` VALUES ('Juan','BMM23'),('Juan','sa');
/*!40000 ALTER TABLE `is_registered` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `located`
--

DROP TABLE IF EXISTS `located`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `located` (
  `room_code` varchar(255) NOT NULL,
  `building_code` varchar(10) NOT NULL,
  PRIMARY KEY (`room_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `located`
--

LOCK TABLES `located` WRITE;
/*!40000 ALTER TABLE `located` DISABLE KEYS */;
INSERT INTO `located` VALUES ('E306cd','E'),('G306cd','G'),('GB246','G'),('HB246','H'),('IB146','I'),('IB147','I');
/*!40000 ALTER TABLE `located` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `room` (
  `room_code` varchar(10) NOT NULL,
  PRIMARY KEY (`room_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES ('E306cd'),('G306cd'),('GB246'),('HB246'),('IB146'),('IB147');
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `student` (
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  PRIMARY KEY (`username`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES ('Juan','dce0b27ba675df41e9cc07af80ec59c475810824','Juan','juan'),('pepe','dce0b27ba675df41e9cc07af80ec59c475810824','pepe','mail');
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-11-15 19:14:17
