-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jan 04, 2025 at 04:55 PM
-- Server version: 8.0.30
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_hotel`
--

-- --------------------------------------------------------

--
-- Table structure for table `booking`
--

CREATE TABLE `booking` (
  `id_booking` int NOT NULL,
  `nik` int NOT NULL,
  `nama` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `no_hp` varchar(15) NOT NULL,
  `id_room` int DEFAULT NULL,
  `check_in` date DEFAULT NULL,
  `check_out` date DEFAULT NULL,
  `total_price` varchar(50) DEFAULT NULL,
  `status_booking` enum('available','booked') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `booking`
--

INSERT INTO `booking` (`id_booking`, `nik`, `nama`, `email`, `no_hp`, `id_room`, `check_in`, `check_out`, `total_price`, `status_booking`) VALUES
(1, 7868333, 'Onic', 'onic@gmail.com', '08756725782', 2, '2024-12-31', '2025-01-01', '1290000.0', 'booked'),
(2, 897987, 'Heru', 'heru@gamil.com', '075355367', 1, '2025-01-01', '2025-01-24', '2.3E7', 'booked'),
(3, 9356536, 'Najla', 'najla@gmail.com', '085653633', 6, '2024-12-31', '2025-01-08', '3.9999992E7', 'booked'),
(4, 4736733, 'Farah', 'farah@gmail.com', '0845378783', 3, '2024-12-31', '2025-01-04', '400000.0', 'booked'),
(5, 12345, 'syfnaza', 'syfnaza@gmail.com', '087653462133', 1, '2025-01-03', '2025-01-04', '1000000.0', 'booked'),
(6, 89064, 'hasna', 'hasna@gmail.com', '87654321906', 5, '2025-01-08', '2025-01-10', '1800000.0', 'booked'),
(7, 1233, 'hana', 'hana@gmail,com', '12345678', 2, '2025-01-03', '2025-01-04', '1290000.0', 'booked');

-- --------------------------------------------------------

--
-- Table structure for table `login`
--

CREATE TABLE `login` (
  `id` int NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `login`
--

INSERT INTO `login` (`id`, `username`, `password`) VALUES
(1, 'dara', 'dara123'),
(20, 'admin', 'admin123'),
(21, 'syfnaza', '1'),
(23, 'hana ', '123'),
(24, 'ara', '1234');

-- --------------------------------------------------------

--
-- Table structure for table `rooms`
--

CREATE TABLE `rooms` (
  `id_room` int NOT NULL,
  `no_room` varchar(20) NOT NULL,
  `type_room` varchar(50) NOT NULL,
  `fasilitas` varchar(200) NOT NULL,
  `price` varchar(50) NOT NULL,
  `status` enum('available','booked','maintenance') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `rooms`
--

INSERT INTO `rooms` (`id_room`, `no_room`, `type_room`, `fasilitas`, `price`, `status`) VALUES
(1, 'B12', 'Deluxe Room', 'Tv', '1000000', 'available'),
(2, 'V12', 'Double Room', 'Lengkap', '1290000', 'maintenance'),
(3, 'A13', 'Standard Room', 'AC', '100000', 'available'),
(4, 'H17', 'Twin Room', 'Wifi', '3000000', 'maintenance'),
(5, 'G67', 'Suite Room', 'Kolam', '900000', 'booked'),
(6, 'U56', 'Single Room', 'Gym', '4999999', 'available'),
(7, 'C34', 'Family Room', 'Sarapan pagi', '2000000', 'available');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `booking`
--
ALTER TABLE `booking`
  ADD PRIMARY KEY (`id_booking`),
  ADD KEY `id_room` (`id_room`);

--
-- Indexes for table `login`
--
ALTER TABLE `login`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `rooms`
--
ALTER TABLE `rooms`
  ADD PRIMARY KEY (`id_room`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `booking`
--
ALTER TABLE `booking`
  MODIFY `id_booking` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `login`
--
ALTER TABLE `login`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `booking`
--
ALTER TABLE `booking`
  ADD CONSTRAINT `booking_ibfk_1` FOREIGN KEY (`id_room`) REFERENCES `rooms` (`id_room`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
