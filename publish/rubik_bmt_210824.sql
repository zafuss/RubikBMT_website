-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Aug 21, 2024 at 09:13 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `rubik_bmt_backup190824`
--

-- --------------------------------------------------------

--
-- Table structure for table `candidate`
--

CREATE TABLE `candidate` (
  `id` varchar(255) NOT NULL,
  `date_of_birth` date DEFAULT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `gmail` varchar(255) DEFAULT NULL,
  `is_confirmed` bit(1) NOT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `phone_number` varchar(10) DEFAULT NULL,
  `registration_time` datetime(6) DEFAULT NULL,
  `competition_id` varchar(255) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `time_confirmed` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `candidate_events`
--

CREATE TABLE `candidate_events` (
  `candidate_id` varchar(255) NOT NULL,
  `events_id` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `competition`
--

CREATE TABLE `competition` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `competition`
--

INSERT INTO `competition` (`id`, `name`) VALUES
('1', 'BackToSchool');

-- --------------------------------------------------------

--
-- Table structure for table `competition_events`
--

CREATE TABLE `competition_events` (
  `competition_id` varchar(255) NOT NULL,
  `events_id` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `competition_events`
--

INSERT INTO `competition_events` (`competition_id`, `events_id`) VALUES
('1', '1'),
('1', '2'),
('1', '3');

-- --------------------------------------------------------

--
-- Table structure for table `event`
--

CREATE TABLE `event` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `event`
--

INSERT INTO `event` (`id`, `name`) VALUES
('1', '3x3'),
('2', '2x2'),
('3', '3x3 One Hand');

-- --------------------------------------------------------

--
-- Table structure for table `learning_type`
--

CREATE TABLE `learning_type` (
  `id` varchar(255) NOT NULL,
  `learning_type` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `learning_type`
--

INSERT INTO `learning_type` (`id`, `learning_type`) VALUES
('ATHOME', 'Học tại nhà'),
('ONLINE', 'Học trực tuyến');

-- --------------------------------------------------------

--
-- Table structure for table `mentor`
--

CREATE TABLE `mentor` (
  `id` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mentor`
--

INSERT INTO `mentor` (`id`, `description`, `first_name`, `last_name`) VALUES
('lhp', NULL, 'Phong', 'Lê Hà'),
('nhtp', NULL, 'Phú', 'Nguyễn Hoàng Thiên');

-- --------------------------------------------------------

--
-- Table structure for table `mentor_students`
--

CREATE TABLE `mentor_students` (
  `mentor_id` varchar(255) NOT NULL,
  `students_id` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

CREATE TABLE `role` (
  `id` varchar(255) NOT NULL,
  `role_name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `role`
--

INSERT INTO `role` (`id`, `role_name`) VALUES
('ADMIN', 'Admin'),
('CLIENT', 'Client'),
('MODIFIER', 'Modifier'),
('SUPER_ADMIN', 'SuperAdmin');

-- --------------------------------------------------------

--
-- Table structure for table `registerStudent`
--

CREATE TABLE `registerStudent` (
  `id` varchar(255) NOT NULL,
  `date_of_birth` date NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `is_confirmed` bit(1) NOT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `learning_type` varchar(255) DEFAULT NULL,
  `parent_name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(10) DEFAULT NULL,
  `mentor_id` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `learning_type_id` varchar(255) DEFAULT NULL,
  `confirmation_date` datetime(6) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `registration_date` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `registerStudent`
--

INSERT INTO `registerStudent` (`id`, `date_of_birth`, `email`, `first_name`, `image_url`, `is_confirmed`, `last_name`, `learning_type`, `parent_name`, `phone_number`, `mentor_id`, `full_name`, `learning_type_id`, `confirmation_date`, `note`, `registration_date`) VALUES
('9cf5f773-047d-4545-87c8-b45a5f362370', '2011-08-25', 'suhoangnguyen368@gmail.com', 'Nguyễn Sư', NULL, b'0', 'Hoàng', NULL, 'Hoàng Nguyễn Sư', '0385286085', 'nhtp', 'Nguyễn Sư Hoàng', 'ONLINE', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` varchar(255) NOT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  `count_fail` int(11) NOT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `lock_expired` datetime(6) DEFAULT NULL,
  `password_hash` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `provider` varchar(50) DEFAULT NULL,
  `reset_password_token` varchar(255) DEFAULT NULL,
  `reset_password_token_expired` datetime(6) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `avatar_url`, `count_fail`, `create_date`, `email`, `enabled`, `full_name`, `lock_expired`, `password_hash`, `phone_number`, `provider`, `reset_password_token`, `reset_password_token_expired`, `user_name`, `first_name`, `last_name`) VALUES
('14cba104-4537-4050-9c6f-0f51ead08a6d', NULL, 0, NULL, 'admin@rubikbmt.vn', b'0', NULL, NULL, '$2a$10$b4eZUDexgOYSoBDjDIH64udAWJBoYxFN8S4Fwp3qetgWf9SkIsxuu', NULL, NULL, NULL, NULL, 'admin', NULL, NULL),
('ce4c1055-eb43-4872-8b85-7d22e4cbf6b3', NULL, 0, NULL, 'duc.modifier@rubikbmt.vn', b'0', NULL, NULL, '$2a$10$vVrDa.1PSMiiAF78jMovg.8wveY.NOnRNEF7dCn7AzMWAVN8GsrRa', NULL, NULL, NULL, NULL, 'ducmodifier', 'Đức', 'Nguyễn Xuân'),
('d7fe2c21-8ac9-4a3c-a919-b3b9b2cb16da', NULL, 0, NULL, 'superadmin@rubikbmt.vn', b'0', NULL, NULL, '$2a$10$ap9tubgy6TaFyqCdrlvNj.H7bkJUFU4lGnhlTv.cfL70/hZiaEE3q', NULL, NULL, NULL, NULL, 'superadmin', 'superadmin', 'superadmin');

-- --------------------------------------------------------

--
-- Table structure for table `user_role`
--

CREATE TABLE `user_role` (
  `user_id` varchar(255) NOT NULL,
  `role_id` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_role`
--

INSERT INTO `user_role` (`user_id`, `role_id`) VALUES
('14cba104-4537-4050-9c6f-0f51ead08a6d', 'ADMIN'),
('ce4c1055-eb43-4872-8b85-7d22e4cbf6b3', 'MODIFIER'),
('d7fe2c21-8ac9-4a3c-a919-b3b9b2cb16da', 'SUPER_ADMIN');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `candidate`
--
ALTER TABLE `candidate`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKpsewvogvakidtrd7sjdb2lu8x` (`competition_id`);

--
-- Indexes for table `candidate_events`
--
ALTER TABLE `candidate_events`
  ADD KEY `FKhewbomfu7807dcnebd19kl3hc` (`events_id`),
  ADD KEY `FK2aht2tsi8y5xq10ebpu9xkl4k` (`candidate_id`);

--
-- Indexes for table `competition`
--
ALTER TABLE `competition`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `competition_events`
--
ALTER TABLE `competition_events`
  ADD UNIQUE KEY `UKtbdg9t2ctwjswjoyh0rv43980` (`events_id`),
  ADD KEY `FK3qyvkbnxgm102fms9ydry8e2e` (`competition_id`);

--
-- Indexes for table `event`
--
ALTER TABLE `event`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `learning_type`
--
ALTER TABLE `learning_type`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK7etbmjx9yx8w7fdptcdacm4k8` (`learning_type`);

--
-- Indexes for table `mentor`
--
ALTER TABLE `mentor`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `mentor_students`
--
ALTER TABLE `mentor_students`
  ADD UNIQUE KEY `UKds5jsg1d3tucu94ewff5hetf4` (`students_id`),
  ADD KEY `FKslof18mc1j4o1d17fv9olj9x9` (`mentor_id`);

--
-- Indexes for table `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKiubw515ff0ugtm28p8g3myt0h` (`role_name`);

--
-- Indexes for table `registerStudent`
--
ALTER TABLE `registerStudent`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKfmk16k9whxemgkowg3mt24m3w` (`mentor_id`),
  ADD KEY `FK7ibptto9xf19k2xkpratmvb7e` (`learning_type_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKlqjrcobrh9jc8wpcar64q1bfh` (`user_name`);

--
-- Indexes for table `user_role`
--
ALTER TABLE `user_role`
  ADD PRIMARY KEY (`user_id`,`role_id`),
  ADD KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `candidate`
--
ALTER TABLE `candidate`
  ADD CONSTRAINT `FKpsewvogvakidtrd7sjdb2lu8x` FOREIGN KEY (`competition_id`) REFERENCES `competition` (`id`);

--
-- Constraints for table `candidate_events`
--
ALTER TABLE `candidate_events`
  ADD CONSTRAINT `FK2aht2tsi8y5xq10ebpu9xkl4k` FOREIGN KEY (`candidate_id`) REFERENCES `candidate` (`id`),
  ADD CONSTRAINT `FKhewbomfu7807dcnebd19kl3hc` FOREIGN KEY (`events_id`) REFERENCES `event` (`id`);

--
-- Constraints for table `competition_events`
--
ALTER TABLE `competition_events`
  ADD CONSTRAINT `FK3qyvkbnxgm102fms9ydry8e2e` FOREIGN KEY (`competition_id`) REFERENCES `competition` (`id`),
  ADD CONSTRAINT `FKh9cvq7bhi2u2om16mr2gjr4ru` FOREIGN KEY (`events_id`) REFERENCES `event` (`id`);

--
-- Constraints for table `mentor_students`
--
ALTER TABLE `mentor_students`
  ADD CONSTRAINT `FKmax7g1xkr4osp6wk6ati4ihs4` FOREIGN KEY (`students_id`) REFERENCES `registerStudent` (`id`),
  ADD CONSTRAINT `FKslof18mc1j4o1d17fv9olj9x9` FOREIGN KEY (`mentor_id`) REFERENCES `mentor` (`id`);

--
-- Constraints for table `registerStudent`
--
ALTER TABLE `registerStudent`
  ADD CONSTRAINT `FK7ibptto9xf19k2xkpratmvb7e` FOREIGN KEY (`learning_type_id`) REFERENCES `learning_type` (`id`),
  ADD CONSTRAINT `FKfmk16k9whxemgkowg3mt24m3w` FOREIGN KEY (`mentor_id`) REFERENCES `mentor` (`id`);

--
-- Constraints for table `user_role`
--
ALTER TABLE `user_role`
  ADD CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
