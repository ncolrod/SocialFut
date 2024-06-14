-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 14-06-2024 a las 10:48:03
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `db_socialfut_v1`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `football_match`
--

CREATE TABLE `football_match` (
  `match_id` int(11) NOT NULL,
  `date` datetime(6) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `price_per_person` double DEFAULT NULL,
  `result` varchar(255) DEFAULT NULL,
  `summary` varchar(255) DEFAULT NULL,
  `away_team_id` int(11) DEFAULT NULL,
  `creator_user_id` int(11) DEFAULT NULL,
  `home_team_id` int(11) DEFAULT NULL,
  `is_created` bit(1) NOT NULL,
  `is_finished` bit(1) NOT NULL,
  `check_away` varchar(255) DEFAULT NULL,
  `check_home` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `football_match`
--

INSERT INTO `football_match` (`match_id`, `date`, `location`, `price_per_person`, `result`, `summary`, `away_team_id`, `creator_user_id`, `home_team_id`, `is_created`, `is_finished`, `check_away`, `check_home`) VALUES
(51, '2024-06-02 20:06:00.000000', 'Hytasa', 2, '2-0', NULL, 2, 1, 1, b'1', b'1', NULL, NULL),
(54, '2024-06-07 11:40:00.000000', 'Polideportivo San Pablo interior', 2, '5-3', NULL, 2, 1, 1, b'1', b'1', NULL, NULL),
(56, '2024-06-08 15:10:00.000000', 'Polideportivo Hytasa Interior', 1, '5-0', NULL, 2, 1, 1, b'1', b'1', NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `goal`
--

CREATE TABLE `goal` (
  `goal_id` int(11) NOT NULL,
  `assistant_id` int(11) DEFAULT NULL,
  `match_id` int(11) DEFAULT NULL,
  `scorer_user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `team`
--

CREATE TABLE `team` (
  `team_id` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `join_code` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `stadium` varchar(255) DEFAULT NULL,
  `team_color` varchar(255) DEFAULT NULL,
  `is_available` bit(1) NOT NULL,
  `lost_matches` int(11) NOT NULL,
  `matches_played` int(11) NOT NULL,
  `matches_won` int(11) NOT NULL,
  `tied_matches` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `team`
--

INSERT INTO `team` (`team_id`, `description`, `join_code`, `location`, `name`, `stadium`, `team_color`, `is_available`, `lost_matches`, `matches_played`, `matches_won`, `tied_matches`) VALUES
(1, 'Heptacampeones de europa', 'sevilla123', 'Nervion', 'Sevilla FC', 'RSP', 'Rojo y blanco', b'0', 0, 5, 5, 0),
(2, NULL, 'betis123', 'Sevilla', 'Betis', 'Benito Villamarin', NULL, b'0', 5, 5, 0, 0),
(5, NULL, 'dioses123', 'Sevilla', 'Dioses', 'El Coliseum', NULL, b'0', 0, 0, 0, 0),
(8, NULL, 'og123', 'Fuengirola', 'OG', 'El masi', NULL, b'0', 0, 0, 0, 0),
(9, NULL, 'masi123', 'Fuengirola', 'La Masineta', 'EL MASIUM', NULL, b'0', 0, 0, 0, 0),
(10, NULL, 'HHDHDHD', 'PLUTON', 'loS AVENGERS', 'JSAJDJSADS', NULL, b'0', 0, 0, 0, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `position` varchar(255) DEFAULT NULL,
  `role` enum('USER','ADMIN') DEFAULT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `team_id` int(11) DEFAULT NULL,
  `assists` int(11) NOT NULL,
  `goals` int(11) NOT NULL,
  `matches_played` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `user`
--

INSERT INTO `user` (`id`, `email`, `firstname`, `lastname`, `location`, `password`, `position`, `role`, `telephone`, `team_id`, `assists`, `goals`, `matches_played`) VALUES
(1, 'nico@gmail.com', 'Nicolas', 'Collado', 'Sevilla', '$2a$10$nfy6TJ2xoVokbFNVkzsb4uF7vCPJb60UTFtJRlNnL4bASzHOqW5Eq', 'Mediocentro', 'ADMIN', '677010776', 1, 0, 5, 7),
(2, 'tony@gmail.com', 'Antonio', 'Vera', 'Sevilla', '$2a$10$XdapDYUhsYWfx5UoqSnthuJP/RDDcSE68QZzcv7L7FxTvyjba3FlC', 'Mediocentro', 'ADMIN', '655444556', 2, 0, 0, 7),
(3, 'valle@gmail.com', 'Davinson', 'Sanchez', 'Palomares', '$2a$10$V19YPWE2IjaKwVbfB39Gnen60.aLAnBlD8ihw5Q9z1DNYYlDpL6.u', 'Delantero', 'ADMIN', '6888999886', 2, 0, 0, 7),
(4, 'diego@gmail.com', 'Diego', 'Garcia', 'Sevilla', '$2a$10$w.SZEXT4g3uvwz/DkRFvHO0HWtSbbic8HOqBb3zIe7c1uUg2GajVO', 'Delantero', 'USER', '633444336', 1, 5, 0, 7),
(8, 'nyland@gmail.com', 'Orjan', 'Nyland', 'Sevilla', '$2a$10$9lctdKAMscgNM08JULBIjeCHVGz2au5WMox2WMBCV6EZLHQ2.QxwC', 'Portero', 'ADMIN', '689987654', 1, 0, 0, 0),
(9, 'navas@gmail.com', 'Jesus', 'Navas', 'Sevilla', '$2a$10$ouKPYa13G3OdqZcOkty/Puv0jCXKQxmWlN6jtz4WttxGKg2I3G6bG', 'Defensa', 'ADMIN', '654321123', 1, 0, 0, 0),
(10, 'banega@gmail.com', 'Ever', 'Banega', 'Sevilla', '$2a$10$ns6bVZR4fASZw0wJdYZarObN4odgqyOCFDcEdOlwwqLTsk/3poxHq', 'Mediocentro', 'USER', '675465564', 1, 0, 0, 0),
(11, 'juandedios@gmail.com', 'Juan', 'De Dios', 'Sevilla', '$2a$10$JDof9lFbAl8C28qgGKqwh.R4StRKq4VsnvndeI/lvC6wI8oiDOx1O', 'Delantero', 'ADMIN', '777888999', 5, 0, 0, 0),
(12, 'jalberto@gmail.com', 'Juan', 'Alberto', 'Fuengirola', '$2a$10$KGriYml1CVAu.1QY8AB9JeIjg3tzcPnn9n.W8ESlSMWC6hDgF1Lye', 'Delantero', 'ADMIN', '907878787', 8, 0, 0, 0),
(13, 'masi@gmail.com', 'Masi', 'Maribel', 'Fuengirola', '$2a$10$XZW.r9VzKEpAEBqjHZywPehZynHEB99oGRiZRmLhWhGjB6XBrTHbW', 'Delantero', 'ADMIN', '678876543', 9, 0, 0, 0),
(14, 'elboqueron@gmail.com', 'lilboq', 'boqueron', 'Fuengitola', '$2a$10$VCcmE.oeKtgEJttW.2FPYO6PvC26ddgmH5fBnRORgm5OHO6dbwYZS', 'Delantero', 'USER', '908765456', 9, 0, 0, 0),
(15, 'andres@gmail.com', 'Andres', 'Boqueron', 'Fuengirola', '$2a$10$OFYY0HNagoUZ5KLnAEmj8etXE7VQZALboPnbK4E6Jr4f16s3GO646', 'Delantero', 'USER', '789909876', 9, 0, 0, 0),
(16, 'terminator@gmail.com', 'Terminator', 'Lord', 'Narnia', '$2a$10$QjpZ9vIDOuvIDCeaBOp/6OZ1HEoB8zuKUmKdJUCoeIBoFbgANhJdq', 'Defensa', 'ADMIN', '999000999', 10, 0, 0, 0),
(17, '', '', '', '', '$2a$10$LTZtn45UVJdiqGJ9CMWn/eGIrd2PGfUWq5sOnFV6URXa35gxxc1vi', 'Delantero', 'USER', '', NULL, 0, 0, 0);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `football_match`
--
ALTER TABLE `football_match`
  ADD PRIMARY KEY (`match_id`),
  ADD KEY `FKsvp9i6sma7i7lsa90bop1hkut` (`away_team_id`),
  ADD KEY `FK65edoq9md9qrv438gfwkwuj02` (`creator_user_id`),
  ADD KEY `FKbo42ywtc4b860fa2gmlh338t5` (`home_team_id`);

--
-- Indices de la tabla `goal`
--
ALTER TABLE `goal`
  ADD PRIMARY KEY (`goal_id`),
  ADD KEY `FKcxsop2k64m3ujuqd26lw1oc5n` (`assistant_id`),
  ADD KEY `FKbcf2ldwc047p5ub4o70hpfnka` (`match_id`),
  ADD KEY `FK7bvqmbtjbs4bjweneyl214kpt` (`scorer_user_id`);

--
-- Indices de la tabla `team`
--
ALTER TABLE `team`
  ADD PRIMARY KEY (`team_id`),
  ADD UNIQUE KEY `UQ_name` (`name`);

--
-- Indices de la tabla `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UQ_email` (`email`),
  ADD KEY `FKbmqm8c8m2aw1vgrij7h0od0ok` (`team_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `football_match`
--
ALTER TABLE `football_match`
  MODIFY `match_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=61;

--
-- AUTO_INCREMENT de la tabla `goal`
--
ALTER TABLE `goal`
  MODIFY `goal_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `team`
--
ALTER TABLE `team`
  MODIFY `team_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `football_match`
--
ALTER TABLE `football_match`
  ADD CONSTRAINT `FK65edoq9md9qrv438gfwkwuj02` FOREIGN KEY (`creator_user_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `FKbo42ywtc4b860fa2gmlh338t5` FOREIGN KEY (`home_team_id`) REFERENCES `team` (`team_id`),
  ADD CONSTRAINT `FKsvp9i6sma7i7lsa90bop1hkut` FOREIGN KEY (`away_team_id`) REFERENCES `team` (`team_id`);

--
-- Filtros para la tabla `goal`
--
ALTER TABLE `goal`
  ADD CONSTRAINT `FK7bvqmbtjbs4bjweneyl214kpt` FOREIGN KEY (`scorer_user_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `FKbcf2ldwc047p5ub4o70hpfnka` FOREIGN KEY (`match_id`) REFERENCES `football_match` (`match_id`),
  ADD CONSTRAINT `FKcxsop2k64m3ujuqd26lw1oc5n` FOREIGN KEY (`assistant_id`) REFERENCES `user` (`id`);

--
-- Filtros para la tabla `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `FKbmqm8c8m2aw1vgrij7h0od0ok` FOREIGN KEY (`team_id`) REFERENCES `team` (`team_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
