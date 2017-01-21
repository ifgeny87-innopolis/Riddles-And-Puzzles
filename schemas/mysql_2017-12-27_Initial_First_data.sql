-- --------------------------------------------------------
-- Хост:                         localhost
-- Версия сервера:               8.0.0-dmr-log - MySQL Community Server (GPL)
-- Операционная система:         Win64
-- HeidiSQL Версия:              9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Дамп структуры базы данных riddles
CREATE DATABASE IF NOT EXISTS `riddles` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `riddles`;

-- Дамп структуры для таблица riddles.answer
CREATE TABLE IF NOT EXISTS `answer` (
  `id_bin` binary(16) NOT NULL,
  `id` varchar(36) GENERATED ALWAYS AS (insert(insert(insert(insert(hex(`id_bin`),9,0,'-'),14,0,'-'),19,0,'-'),24,0,'-')) VIRTUAL,
  `user_id_bin` binary(16) NOT NULL,
  `user_id` varchar(36) GENERATED ALWAYS AS (insert(insert(insert(insert(hex(`user_id_bin`),9,0,'-'),14,0,'-'),19,0,'-'),24,0,'-')) VIRTUAL,
  `riddle_id_bin` binary(16) NOT NULL,
  `riddle_id` varchar(36) GENERATED ALWAYS AS (insert(insert(insert(insert(hex(`riddle_id_bin`),9,0,'-'),14,0,'-'),19,0,'-'),24,0,'-')) VIRTUAL,
  `answer` varchar(250) NOT NULL,
  `is_right` int(10) unsigned NOT NULL,
  `time_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_bin`),
  KEY `user_id` (`user_id`,`riddle_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Экспортируемые данные не выделены.
-- Дамп структуры для таблица riddles.riddle
CREATE TABLE IF NOT EXISTS `riddle` (
  `id_bin` binary(16) NOT NULL,
  `id` varchar(36) GENERATED ALWAYS AS (insert(insert(insert(insert(hex(`id_bin`),9,0,'-'),14,0,'-'),19,0,'-'),24,0,'-')) VIRTUAL,
  `user_id_bin` binary(16) NOT NULL,
  `user_id` varchar(36) GENERATED ALWAYS AS (insert(insert(insert(insert(hex(`user_id_bin`),9,0,'-'),14,0,'-'),19,0,'-'),24,0,'-')) VIRTUAL,
  `title` varchar(100) NOT NULL,
  `text` text NOT NULL,
  `image` varchar(250) DEFAULT NULL,
  `answer` varchar(250) NOT NULL,
  `answeredCount` int(10) unsigned NOT NULL DEFAULT '0',
  `attemptCount` int(10) unsigned NOT NULL DEFAULT '0',
  `time_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `time_update` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_bin`),
  KEY `id` (`id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Экспортируемые данные не выделены.
-- Дамп структуры для таблица riddles.user
CREATE TABLE IF NOT EXISTS `user` (
  `id_bin` binary(16) NOT NULL,
  `id` varchar(36) GENERATED ALWAYS AS (insert(insert(insert(insert(hex(`id_bin`),9,0,'-'),14,0,'-'),19,0,'-'),24,0,'-')) VIRTUAL,
  `name` varchar(50) NOT NULL,
  `birth` date DEFAULT NULL,
  `hash_password` varchar(32) DEFAULT NULL,
  `answeredCount` int(11) unsigned NOT NULL DEFAULT '0',
  `attemptCount` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_bin`),
  KEY `name` (`name`),
  KEY `id_text` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Экспортируемые данные не выделены.
-- Дамп структуры для триггер riddles.answer_bi
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `answer_bi` BEFORE INSERT ON `answer` FOR EACH ROW BEGIN
SET NEW.id_bin = unhex(replace(uuid(),'-',''));
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Дамп структуры для триггер riddles.riddle_bi
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `riddle_bi` BEFORE INSERT ON `riddle` FOR EACH ROW BEGIN
SET NEW.id_bin = unhex(replace(uuid(),'-',''));
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Дамп структуры для триггер riddles.user_bi
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `user_bi` BEFORE INSERT ON `user` FOR EACH ROW BEGIN
  SET NEW.id_bin = unhex(replace(uuid(),'-',''));
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
