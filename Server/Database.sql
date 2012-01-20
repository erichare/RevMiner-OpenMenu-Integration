-- phpMyAdmin SQL Dump
-- version 3.4.9
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jan 20, 2012 at 03:22 PM
-- Server version: 5.1.60
-- PHP Version: 5.2.17

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `dawgsfor_openmenu`
--

-- --------------------------------------------------------

--
-- Table structure for table `items`
--

DROP TABLE IF EXISTS `items`;
CREATE TABLE IF NOT EXISTS `items` (
  `iid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`iid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `items`
--

INSERT INTO `items` (`iid`, `name`, `description`, `price`, `updated`) VALUES
(1, 'HOUSE SALAD', 'Our house salad consists of fresh garden greens, tomatoes, and cucumbers. Served with your choice of either Mediterranean or Ranch dressing.', 3.95, '2012-01-20 21:58:04'),
(2, 'CAESAR SALAD', 'Fresh romaine lettuce, prepared with classic caesar dressing.', 4.50, '2012-01-20 22:04:08'),
(3, 'Classic Caesar Salad', 'Hearts of romaine, Reggiano parmesan, croutons, Spanish white anchovies', 0.00, '2012-01-20 22:04:08');

-- --------------------------------------------------------

--
-- Table structure for table `restaurants`
--

DROP TABLE IF EXISTS `restaurants`;
CREATE TABLE IF NOT EXISTS `restaurants` (
  `rid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `state` varchar(255) NOT NULL,
  `country` varchar(255) NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`rid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `restaurants`
--

INSERT INTO `restaurants` (`rid`, `name`, `address`, `city`, `state`, `country`, `updated`) VALUES
(1, 'Taste of India', '5517 Roosevelt Way NE', 'Seattle', 'WA', 'US', '2012-01-20 21:55:51'),
(2, 'Sand Point Grill', '5412 Sand Point Way NE', 'Seattle', 'WA', 'US', '2012-01-20 22:00:31');

-- --------------------------------------------------------

--
-- Table structure for table `restaurants_items`
--

DROP TABLE IF EXISTS `restaurants_items`;
CREATE TABLE IF NOT EXISTS `restaurants_items` (
  `rid` int(11) NOT NULL,
  `iid` int(11) NOT NULL,
  KEY `iid` (`iid`),
  KEY `rid` (`rid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `restaurants_items`
--

INSERT INTO `restaurants_items` (`rid`, `iid`) VALUES
(1, 1),
(1, 2),
(2, 3);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `restaurants_items`
--
ALTER TABLE `restaurants_items`
  ADD CONSTRAINT `restaurants_items_ibfk_2` FOREIGN KEY (`iid`) REFERENCES `items` (`iid`),
  ADD CONSTRAINT `restaurants_items_ibfk_1` FOREIGN KEY (`rid`) REFERENCES `restaurants` (`rid`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
