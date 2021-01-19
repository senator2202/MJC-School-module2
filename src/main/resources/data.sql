-- --------------------------------------------------------
-- Хост:                         127.0.0.1
-- Версия сервера:               8.0.21 - MySQL Community Server - GPL
-- Операционная система:         Win64
-- HeidiSQL Версия:              11.0.0.5919
-- -------------------------------------------------------- /*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
-- Дамп данных таблицы gift.certificate_tag: ~15 rows (приблизительно) /*!40000 ALTER TABLE `certificate_tag` DISABLE KEYS */;
INSERT INTO `certificate_tag` (`gift_certificate_id`, `tag_id`)
VALUES (1, 8),
       (1, 1),
       (2, 1),
       (3, 2),
       (5, 2),
       (5, 1),
       (5, 8),
       (6, 3),
       (12, 7),
       (7, 6),
       (15, 3),
       (15, 10),
       (16, 2),
       (19, 13),
       (19, 14);
/*!40000 ALTER TABLE `certificate_tag` ENABLE KEYS */;
-- Дамп данных таблицы gift.gift_certificate: ~10 rows (приблизительно) /*!40000 ALTER TABLE `gift_certificate` DISABLE KEYS */;
INSERT INTO `gift_certificate` (`id`, `name`, `description`, `price`, `duration`
                               , `create_date`, `last_update_date`)
VALUES (1, 'Сауна Тритон', 'Сертификат на бесплатное посещение сауны Тритон на Маяковского, 16', 100.00, 60
       , '2020-12-16T14:48Z', '2020-12-16T14:49Z'),
       (2, 'Картинг "У Ашота"', '2 бесплатны круга по автодрому', 25.00, 45
       , '2020-12-16T14:49Z', '2020-12-16T14:50Z'),
       (3, 'Тату салон "Лисица"', 'Бесплатная татуировка 10x10 см', 125.00, 180
       , '2020-12-16T14:51Z', '2020-12-16T14:52:Z'),
       (5, 'SPA центр на Старовиленской, 15', 'Весь спектр SPA процедур', 125.00, 180
       , '2020-12-17T11:49Z', '2020-12-17T11:51Z'),
       (6, 'Programming courses ''Java Web development''', 'Become good programmer for short period', 400.00, 90
       , '2020-12-17T12:00Z', '2020-12-17T12:01Z'),
       (7, 'Театр имени Янки Купалы', 'Посещение любого спектакля', 60.00, 14
       , '2020-12-18T07:11Z', '2020-12-18T10:05Z'),
       (12, 'SilverScreen', 'Просмотр любого кинофильма', 15.00, 45
       , '2020-12-18T09:22Z', '2020-12-18T09:25Z'),
       (15, 'Курсы английского языка', 'Курсы английского в online школе SkyEng', 150.00, 100
       , '2020-12-18T10:22Z', '2020-12-18T10:37Z'),
       (16, 'Тату салон "Imagine Dragon"', 'Бесплатная татуировка 12х12, + дизайн', 250.00, 90
       , '2020-12-21T12:21Z', '2020-12-21T12:21Z'),
       (19, 'Онлайн курсы C#', 'Беслатный курс C# в школе программирования Litrex', 1350.00, 120
       , '2020-12-22T12:33Z', '2020-12-22T12:57Z');
/*!40000 ALTER TABLE `gift_certificate` ENABLE KEYS */;
-- Дамп данных таблицы gift.tag: ~9 rows (приблизительно) /*!40000 ALTER TABLE `tag` DISABLE KEYS */;
INSERT INTO `tag` (`id`, `name`)
VALUES (14, 'IT'),
       (13, 'Programming'),
       (1, 'Активность'),
       (10, 'Искусство'),
       (7, 'Кино'),
       (2, 'Красота'),
       (3, 'Образование'),
       (8, 'Отдых'),
       (6, 'Театр');
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
