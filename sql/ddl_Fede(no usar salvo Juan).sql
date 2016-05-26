CREATE DATABASE  IF NOT EXISTS `seminario` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `seminario`;
-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: localhost    Database: seminario
-- ------------------------------------------------------
-- Server version	5.7.12-log

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
-- Table structure for table `beneficio`
--

DROP TABLE IF EXISTS `beneficio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `beneficio` (
  `ID_BENEFICIO` int(11) NOT NULL AUTO_INCREMENT,
  `FECHA_CADUCA` date NOT NULL,
  `PRODUCTO` varchar(255) NOT NULL,
  `PUNTOS_NECESARIOS` int(11) NOT NULL,
  `ID_SPONSOR` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_BENEFICIO`),
  KEY `FK_BENEFICIO_ID_SPONSOR` (`ID_SPONSOR`),
  CONSTRAINT `FK_BENEFICIO_ID_SPONSOR` FOREIGN KEY (`ID_SPONSOR`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `calificacion`
--

DROP TABLE IF EXISTS `calificacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `calificacion` (
  `id_calificacion` int(11) NOT NULL AUTO_INCREMENT,
  `id_pasajero_viaje` int(11) DEFAULT NULL,
  `calificacion_para_conductor` int(11) DEFAULT NULL,
  `calificacion_para_pasajero` int(11) DEFAULT NULL,
  `participo` char(1) DEFAULT NULL,
  `id_movimiento_puntos` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_calificacion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cliente` (
  `id_usuario` int(11) NOT NULL,
  `puntos` int(11) DEFAULT NULL,
  `reputacion` int(11) DEFAULT NULL,
  `foto_registro` varchar(120) DEFAULT NULL,
  `foto` varchar(120) DEFAULT NULL,
  PRIMARY KEY (`id_usuario`),
  CONSTRAINT `cliente_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `comision`
--

DROP TABLE IF EXISTS `comision`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comision` (
  `ID_COMISION` int(11) NOT NULL AUTO_INCREMENT,
  `LIMITE_INFERIOR` float NOT NULL,
  `LIMITE_SUPERIOR` float NOT NULL,
  `PRECIO_COMISION` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_COMISION`),
  KEY `FK_COMISION_PRECIO_COMISION` (`PRECIO_COMISION`),
  CONSTRAINT `FK_COMISION_PRECIO_COMISION` FOREIGN KEY (`PRECIO_COMISION`) REFERENCES `precio_comision` (`ID_COMISION`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `comision_cobrada`
--

DROP TABLE IF EXISTS `comision_cobrada`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comision_cobrada` (
  `id_comision_cobrada` int(11) NOT NULL AUTO_INCREMENT,
  `monto` decimal(10,2) NOT NULL,
  `estado` char(1) DEFAULT NULL,
  `id_comision` int(11) DEFAULT NULL,
  `id_movimiento_saldo` int(11) DEFAULT NULL,
  `id_pasajero_viaje` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_comision_cobrada`)
) ENGINE=InnoDB AUTO_INCREMENT=7754 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cupon`
--

DROP TABLE IF EXISTS `cupon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cupon` (
  `ID_CUPON` int(11) NOT NULL AUTO_INCREMENT,
  `ESTADO` int(11) DEFAULT NULL,
  `FECHA_CADUCA` date NOT NULL,
  `ID_BENEFICIO` int(11) DEFAULT NULL,
  `ID_MOVIMIENTO_PUNTOS` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_CUPON`),
  KEY `FK_CUPON_ID_MOVIMIENTO_PUNTOS` (`ID_MOVIMIENTO_PUNTOS`),
  KEY `FK_CUPON_ID_BENEFICIO` (`ID_BENEFICIO`),
  CONSTRAINT `FK_CUPON_ID_BENEFICIO` FOREIGN KEY (`ID_BENEFICIO`) REFERENCES `beneficio` (`ID_BENEFICIO`),
  CONSTRAINT `FK_CUPON_ID_MOVIMIENTO_PUNTOS` FOREIGN KEY (`ID_MOVIMIENTO_PUNTOS`) REFERENCES `movimiento_puntos` (`ID_MOVIMIENTOS_PUNTOS`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `estado_viaje`
--

DROP TABLE IF EXISTS `estado_viaje`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `estado_viaje` (
  `id_estado_viaje` char(1) NOT NULL,
  `nombre_estado` varchar(30) NOT NULL,
  PRIMARY KEY (`id_estado_viaje`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `localidad`
--

DROP TABLE IF EXISTS `localidad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `localidad` (
  `id_localidad` int(11) NOT NULL,
  `nombre` varchar(200) DEFAULT NULL,
  `nombre_ascii` varchar(200) DEFAULT NULL,
  `nombres_alternativos` varchar(10000) DEFAULT NULL,
  `lat` float DEFAULT NULL,
  `lng` float DEFAULT NULL,
  `clasificacion` char(1) DEFAULT NULL,
  `clase_punto` varchar(10) DEFAULT NULL,
  `pais` char(2) DEFAULT NULL,
  `paises_alternativos` varchar(200) DEFAULT NULL,
  `admin1` varchar(20) DEFAULT NULL,
  `admin2` varchar(80) DEFAULT NULL,
  `admin3` varchar(20) DEFAULT NULL,
  `admin4` varchar(20) DEFAULT NULL,
  `poblacion` bigint(20) DEFAULT NULL,
  `elevacion` int(11) DEFAULT NULL,
  `dem` int(11) DEFAULT NULL,
  `zona_horaria` varchar(40) DEFAULT NULL,
  `fecha_modificacion` date DEFAULT NULL,
  PRIMARY KEY (`id_localidad`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `localidad_clasificacion`
--

DROP TABLE IF EXISTS `localidad_clasificacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `localidad_clasificacion` (
  `codigo` char(1) NOT NULL,
  `nombre_clase` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `localidad_viaje`
--

DROP TABLE IF EXISTS `localidad_viaje`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `localidad_viaje` (
  `id_viaje` int(11) NOT NULL,
  `id_localidad` int(11) NOT NULL,
  `cantidad_pasajeros` int(11) NOT NULL,
  `kms_a_localidad_siguiente` mediumtext NOT NULL,
  `ordinal` int(11) NOT NULL,
  PRIMARY KEY (`id_viaje`,`id_localidad`),
  UNIQUE KEY `id_viaje` (`id_viaje`,`id_localidad`),
  KEY `id_localidad` (`id_localidad`),
  CONSTRAINT `localidad_viaje_ibfk_1` FOREIGN KEY (`id_viaje`) REFERENCES `viaje` (`id_viaje`),
  CONSTRAINT `localidad_viaje_ibfk_2` FOREIGN KEY (`id_localidad`) REFERENCES `localidad` (`id_localidad`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `maneja`
--

DROP TABLE IF EXISTS `maneja`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `maneja` (
  `id_cliente` int(11) NOT NULL,
  `id_vehiculo` int(11) NOT NULL,
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date DEFAULT NULL,
  PRIMARY KEY (`id_cliente`,`id_vehiculo`,`fecha_inicio`),
  KEY `id_vehiculo` (`id_vehiculo`),
  CONSTRAINT `maneja_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id_usuario`),
  CONSTRAINT `maneja_ibfk_2` FOREIGN KEY (`id_vehiculo`) REFERENCES `vehiculo` (`id_vehiculo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `movimiento_puntos`
--

DROP TABLE IF EXISTS `movimiento_puntos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `movimiento_puntos` (
  `ID_MOVIMIENTOS_PUNTOS` int(11) NOT NULL AUTO_INCREMENT,
  `FECHA` date NOT NULL,
  `MONTO` int(11) NOT NULL,
  `ID_CLIENTE` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_MOVIMIENTOS_PUNTOS`),
  KEY `FK_movimiento_puntos_ID_CLIENTE` (`ID_CLIENTE`),
  CONSTRAINT `FK_movimiento_puntos_ID_CLIENTE` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `movimiento_saldo`
--

DROP TABLE IF EXISTS `movimiento_saldo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `movimiento_saldo` (
  `ID_MOVIMIENTO_SALDO` int(11) NOT NULL AUTO_INCREMENT,
  `FECHA` date NOT NULL,
  `MONTO` float NOT NULL,
  `ID_COMISION_COBRADA` int(11) DEFAULT NULL,
  `ID_PAGO` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_MOVIMIENTO_SALDO`),
  KEY `FK_MOVIMIENTO_SALDO_ID_PAGO` (`ID_PAGO`),
  KEY `FK_MOVIMIENTO_SALDO_ID_COMISION_COBRADA` (`ID_COMISION_COBRADA`),
  CONSTRAINT `FK_MOVIMIENTO_SALDO_ID_COMISION_COBRADA` FOREIGN KEY (`ID_COMISION_COBRADA`) REFERENCES `comision_cobrada` (`id_comision_cobrada`),
  CONSTRAINT `FK_MOVIMIENTO_SALDO_ID_PAGO` FOREIGN KEY (`ID_PAGO`) REFERENCES `pago` (`ID_PAGO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notificacion`
--

DROP TABLE IF EXISTS `notificacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notificacion` (
  `ID_NOTIFICACION` int(11) NOT NULL AUTO_INCREMENT,
  `FECHA` datetime NOT NULL,
  `TEXTO` varchar(250) NOT NULL,
  `ESTADO` char(1) NOT NULL,
  `ID_CLIENTE` int(11) NOT NULL,
  PRIMARY KEY (`ID_NOTIFICACION`),
  KEY `ID_CLIENTE` (`ID_CLIENTE`),
  CONSTRAINT `notificacion_ibfk_1` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `cliente` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=7757 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pago`
--

DROP TABLE IF EXISTS `pago`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pago` (
  `ID_PAGO` int(11) NOT NULL AUTO_INCREMENT,
  `FECHA` date NOT NULL,
  `MONTO` float NOT NULL,
  `ID_CLIENTE` int(11) DEFAULT NULL,
  `ID_MOVIMIENTO_SALDO` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_PAGO`),
  KEY `FK_PAGO_ID_CLIENTE` (`ID_CLIENTE`),
  KEY `FK_PAGO_ID_MOVIMIENTO_SALDO` (`ID_MOVIMIENTO_SALDO`),
  CONSTRAINT `FK_PAGO_ID_CLIENTE` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `FK_PAGO_ID_MOVIMIENTO_SALDO` FOREIGN KEY (`ID_MOVIMIENTO_SALDO`) REFERENCES `movimiento_saldo` (`ID_MOVIMIENTO_SALDO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pasajero_viaje`
--

DROP TABLE IF EXISTS `pasajero_viaje`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pasajero_viaje` (
  `id_pasajero_viaje` int(11) NOT NULL AUTO_INCREMENT,
  `id_viaje` int(11) NOT NULL,
  `id_cliente` int(11) NOT NULL,
  `kilometros` float DEFAULT NULL,
  `estado` char(1) NOT NULL,
  `id_calificacion` int(11) DEFAULT NULL,
  `id_comision_cobrada` int(11) NOT NULL,
  `id_localidad_subida` int(11) NOT NULL,
  `id_localidad_bajada` int(11) NOT NULL,
  PRIMARY KEY (`id_pasajero_viaje`),
  KEY `id_cliente` (`id_cliente`),
  KEY `id_comision_cobrada` (`id_comision_cobrada`),
  KEY `id_viaje` (`id_viaje`,`id_localidad_subida`),
  KEY `id_viaje_2` (`id_viaje`,`id_localidad_bajada`),
  CONSTRAINT `pasajero_viaje_ibfk_1` FOREIGN KEY (`id_viaje`) REFERENCES `viaje` (`id_viaje`),
  CONSTRAINT `pasajero_viaje_ibfk_2` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id_usuario`),
  CONSTRAINT `pasajero_viaje_ibfk_3` FOREIGN KEY (`id_comision_cobrada`) REFERENCES `comision_cobrada` (`id_comision_cobrada`),
  CONSTRAINT `pasajero_viaje_ibfk_4` FOREIGN KEY (`id_viaje`, `id_localidad_subida`) REFERENCES `localidad_viaje` (`id_viaje`, `id_localidad`),
  CONSTRAINT `pasajero_viaje_ibfk_5` FOREIGN KEY (`id_viaje`, `id_localidad_bajada`) REFERENCES `localidad_viaje` (`id_viaje`, `id_localidad`)
) ENGINE=InnoDB AUTO_INCREMENT=2003 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `permiso`
--

DROP TABLE IF EXISTS `permiso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permiso` (
  `id_permiso` int(11) NOT NULL AUTO_INCREMENT,
  `nombre_permiso` varchar(50) NOT NULL,
  `funcionalidad` varchar(200) DEFAULT NULL,
  `descripcion` text,
  `estado` char(1) NOT NULL,
  PRIMARY KEY (`id_permiso`),
  UNIQUE KEY `nombre_permiso` (`nombre_permiso`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `permiso_rol`
--

DROP TABLE IF EXISTS `permiso_rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permiso_rol` (
  `id_permiso` int(11) NOT NULL,
  `id_rol` int(11) NOT NULL,
  `fecha_modificacion` date NOT NULL,
  PRIMARY KEY (`id_permiso`,`id_rol`),
  KEY `id_rol` (`id_rol`),
  CONSTRAINT `permiso_rol_ibfk_1` FOREIGN KEY (`id_permiso`) REFERENCES `permiso` (`id_permiso`),
  CONSTRAINT `permiso_rol_ibfk_2` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id_rol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `persona`
--

DROP TABLE IF EXISTS `persona`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `persona` (
  `id_persona` int(11) NOT NULL AUTO_INCREMENT,
  `nombres` varchar(60) NOT NULL,
  `apellidos` varchar(60) NOT NULL,
  `tipo_doc` smallint(6) NOT NULL,
  `nro_doc` bigint(20) NOT NULL,
  `fecha_nacimiento` date NOT NULL,
  `sexo` char(1) NOT NULL,
  `domicilio` varchar(120) DEFAULT NULL,
  `telefono` varchar(16) DEFAULT NULL,
  `descripcion` text,
  `estado` char(1) NOT NULL,
  PRIMARY KEY (`id_persona`),
  UNIQUE KEY `tipo_doc` (`tipo_doc`,`nro_doc`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `precio_comision`
--

DROP TABLE IF EXISTS `precio_comision`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `precio_comision` (
  `ID_COMISION` int(11) NOT NULL AUTO_INCREMENT,
  `MONTO` float NOT NULL,
  `FECHA` date NOT NULL,
  PRIMARY KEY (`ID_COMISION`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rol`
--

DROP TABLE IF EXISTS `rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rol` (
  `id_rol` int(11) NOT NULL AUTO_INCREMENT,
  `nombre_rol` varchar(30) NOT NULL,
  `nombre_amigable` varchar(30) NOT NULL,
  `descripcion` text,
  `estado` char(1) NOT NULL,
  PRIMARY KEY (`id_rol`),
  UNIQUE KEY `nombre_rol` (`nombre_rol`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sancion`
--

DROP TABLE IF EXISTS `sancion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sancion` (
  `ID_SANCION` int(11) NOT NULL AUTO_INCREMENT,
  `ESTADO` int(11) NOT NULL,
  `FECHA_FIN` date NOT NULL,
  `FECHA_INICIO` date NOT NULL,
  `ID_CLIENTE` int(11) DEFAULT NULL,
  `ID_MOVIMIENTO_PUNTOS` int(11) DEFAULT NULL,
  `ID_TIPO_SANCION` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_SANCION`),
  KEY `FK_SANCION_ID_TIPO_SANCION` (`ID_TIPO_SANCION`),
  KEY `FK_SANCION_ID_MOVIMIENTO_PUNTOS` (`ID_MOVIMIENTO_PUNTOS`),
  KEY `FK_SANCION_ID_CLIENTE` (`ID_CLIENTE`),
  CONSTRAINT `FK_SANCION_ID_CLIENTE` FOREIGN KEY (`ID_CLIENTE`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `FK_SANCION_ID_MOVIMIENTO_PUNTOS` FOREIGN KEY (`ID_MOVIMIENTO_PUNTOS`) REFERENCES `movimiento_puntos` (`ID_MOVIMIENTOS_PUNTOS`),
  CONSTRAINT `FK_SANCION_ID_TIPO_SANCION` FOREIGN KEY (`ID_TIPO_SANCION`) REFERENCES `tipo_sancion` (`ID_TIPO_SANCION`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sequence`
--

DROP TABLE IF EXISTS `sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sequence` (
  `SEQ_NAME` varchar(50) NOT NULL,
  `SEQ_COUNT` decimal(38,0) DEFAULT NULL,
  PRIMARY KEY (`SEQ_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sponsor`
--

DROP TABLE IF EXISTS `sponsor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sponsor` (
  `ID_USUARIO` int(11) NOT NULL,
  `CUIT` int(11) NOT NULL,
  `RUBRO` varchar(120) NOT NULL,
  PRIMARY KEY (`ID_USUARIO`),
  CONSTRAINT `FK_sponsor_ID_USUARIO` FOREIGN KEY (`ID_USUARIO`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tipo_sancion`
--

DROP TABLE IF EXISTS `tipo_sancion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_sancion` (
  `ID_TIPO_SANCION` int(11) NOT NULL AUTO_INCREMENT,
  `DESCRIPCION` varchar(255) NOT NULL,
  `DIAS_SANCION` int(11) NOT NULL,
  PRIMARY KEY (`ID_TIPO_SANCION`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario` (
  `id_usuario` int(11) NOT NULL AUTO_INCREMENT,
  `id_persona` int(11) DEFAULT NULL,
  `nombre_usuario` varchar(30) NOT NULL,
  `password` varchar(32) NOT NULL,
  `email` varchar(40) NOT NULL,
  `descripcion` text,
  `estado` char(1) NOT NULL,
  `tipo` char(1) DEFAULT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `nombre_usuario` (`nombre_usuario`),
  UNIQUE KEY `email` (`email`),
  KEY `id_persona` (`id_persona`),
  CONSTRAINT `usuario_ibfk_1` FOREIGN KEY (`id_persona`) REFERENCES `persona` (`id_persona`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuario_rol`
--

DROP TABLE IF EXISTS `usuario_rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario_rol` (
  `id_usuario` int(11) NOT NULL,
  `id_rol` int(11) NOT NULL,
  `fecha_modificacion` date NOT NULL,
  PRIMARY KEY (`id_usuario`,`id_rol`),
  KEY `id_rol` (`id_rol`),
  CONSTRAINT `usuario_rol_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `usuario_rol_ibfk_2` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id_rol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vehiculo`
--

DROP TABLE IF EXISTS `vehiculo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vehiculo` (
  `id_vehiculo` int(11) NOT NULL AUTO_INCREMENT,
  `anio` int(11) NOT NULL,
  `marca` varchar(30) NOT NULL,
  `modelo` varchar(30) NOT NULL,
  `patente` varchar(15) NOT NULL,
  `verificado` char(1) NOT NULL,
  `estado` char(1) NOT NULL,
  `fecha_verificacion` date DEFAULT NULL,
  `color` varchar(20) DEFAULT NULL,
  `cantidad_asientos` int(11) NOT NULL,
  `aire_acondicionado` char(1) DEFAULT NULL,
  `seguro` char(1) NOT NULL,
  `foto` varchar(120) DEFAULT NULL,
  PRIMARY KEY (`id_vehiculo`),
  UNIQUE KEY `patente` (`patente`)
) ENGINE=InnoDB AUTO_INCREMENT=2821 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `viaje`
--

DROP TABLE IF EXISTS `viaje`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `viaje` (
  `id_viaje` int(11) NOT NULL AUTO_INCREMENT,
  `nombre_amigable` varchar(30) DEFAULT NULL,
  `asientos_disponibles` int(11) NOT NULL,
  `estado` char(1) NOT NULL,
  `fecha_inicio` datetime NOT NULL,
  `fecha_alta` datetime NOT NULL,
  `fecha_finalizacion` datetime DEFAULT NULL,
  `fecha_cancelacion` datetime DEFAULT NULL,
  `id_vehiculo` int(11) NOT NULL,
  `id_cliente` int(11) NOT NULL,
  `fecha_inicio_maneja` date NOT NULL,
  `viaje_complementario` int(11) DEFAULT NULL,
  `precio` float DEFAULT NULL,
  PRIMARY KEY (`id_viaje`),
  KEY `id_cliente` (`id_cliente`,`id_vehiculo`,`fecha_inicio_maneja`),
  KEY `viaje_complementario` (`viaje_complementario`),
  KEY `estado` (`estado`),
  CONSTRAINT `viaje_ibfk_1` FOREIGN KEY (`id_cliente`, `id_vehiculo`, `fecha_inicio_maneja`) REFERENCES `maneja` (`id_cliente`, `id_vehiculo`, `fecha_inicio`),
  CONSTRAINT `viaje_ibfk_2` FOREIGN KEY (`viaje_complementario`) REFERENCES `viaje` (`id_viaje`),
  CONSTRAINT `viaje_ibfk_3` FOREIGN KEY (`estado`) REFERENCES `estado_viaje` (`id_estado_viaje`)
) ENGINE=InnoDB AUTO_INCREMENT=2290 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-05-26 17:36:31
