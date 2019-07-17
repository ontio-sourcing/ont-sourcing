-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 172.17.0.1:3306
-- Generation Time: Jul 08, 2019 at 08:59 AM
-- Server version: 5.7.26-0ubuntu0.16.04.1
-- PHP Version: 7.2.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ont-sourcing-v2`
--

create database `ont-sourcing-v2`;
use `ont-sourcing-v2`;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_action`
--

CREATE TABLE `tbl_action` (
  `id` int(11) NOT NULL,
  `ontid` varchar(255) NOT NULL,
  `control` varchar(255) NOT NULL,
  `txhash` varchar(255) NOT NULL,
  `type` int(64) NOT NULL,
  `detail` text NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_action_ontid`
--

CREATE TABLE `tbl_action_ontid` (
  `id` int(11) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `ontid` varchar(255) DEFAULT NULL,
  `ddo` text,
  `keystore` text,
  `txhash` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_action_ontid_control`
--

CREATE TABLE `tbl_action_ontid_control` (
  `id` int(11) NOT NULL,
  `control` varchar(255) DEFAULT NULL,
  `ontid` varchar(255) DEFAULT NULL,
  `txhash` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_attestation`
--

CREATE TABLE `tbl_attestation` (
  `id` int(11) NOT NULL,
  `ontid` varchar(255) NOT NULL,
  `company_ontid` varchar(255) NOT NULL,
  `txhash` varchar(255) NOT NULL,
  `filehash` varchar(255) NOT NULL,
  `detail` text NOT NULL,
  `type` varchar(16) NOT NULL,
  `timestamp` datetime NOT NULL,
  `timestamp_sign` text NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '0-未删除；1-已删除；',
  `revoke_tx` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_attestation_company`
--

CREATE TABLE `tbl_attestation_company` (
  `id` int(11) NOT NULL,
  `ontid` varchar(128) NOT NULL,
  `prikey` varchar(128) NOT NULL COMMENT '私钥，必须和合约中的地址相对应',
  `code_addr` varchar(128) NOT NULL COMMENT '智能合约地址',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_event`
--

CREATE TABLE `tbl_event` (
  `id` int(11) NOT NULL,
  `txhash` varchar(255) NOT NULL,
  `event` text NOT NULL,
  `height` int(11) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_sfl_identity`
--

CREATE TABLE `tbl_sfl_identity` (
  `id` int(11) NOT NULL,
  `agent` varchar(255) DEFAULT NULL,
  `agent_id` varchar(255) DEFAULT NULL,
  `cert_name` varchar(255) DEFAULT NULL,
  `cert_no` varchar(255) DEFAULT NULL,
  `cert_type` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `legal_person` varchar(255) DEFAULT NULL,
  `legal_person_id` varchar(255) DEFAULT NULL,
  `mobile_no` varchar(255) DEFAULT NULL,
  `properties` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `user_type` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_sfl_notary`
--

CREATE TABLE `tbl_sfl_notary` (
  `id` int(11) NOT NULL,
  `cert_no` varchar(255) DEFAULT NULL,
  `cert_url` varchar(255) DEFAULT NULL,
  `confirm` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `filehash` varchar(255) DEFAULT NULL,
  `txhash` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbl_action`
--
ALTER TABLE `tbl_action`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `txhash` (`txhash`) USING BTREE,
  ADD KEY `control` (`control`),
  ADD KEY `ontid` (`ontid`);

--
-- Indexes for table `tbl_action_ontid`
--
ALTER TABLE `tbl_action_ontid`
  ADD PRIMARY KEY (`id`),
  ADD KEY `username` (`username`);

--
-- Indexes for table `tbl_action_ontid_control`
--
ALTER TABLE `tbl_action_ontid_control`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_attestation`
--
ALTER TABLE `tbl_attestation`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `txhash` (`txhash`) USING BTREE,
  ADD KEY `filehash` (`filehash`),
  ADD KEY `ontid` (`ontid`),
  ADD KEY `company_ontid` (`company_ontid`),
  ADD KEY `type` (`type`);

--
-- Indexes for table `tbl_attestation_company`
--
ALTER TABLE `tbl_attestation_company`
  ADD PRIMARY KEY (`id`),
  ADD KEY `ontid` (`ontid`);

--
-- Indexes for table `tbl_event`
--
ALTER TABLE `tbl_event`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `txhash` (`txhash`) USING BTREE;

--
-- Indexes for table `tbl_sfl_identity`
--
ALTER TABLE `tbl_sfl_identity`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_sfl_notary`
--
ALTER TABLE `tbl_sfl_notary`
  ADD PRIMARY KEY (`id`),
  ADD KEY `cert_no` (`cert_no`),
  ADD KEY `filehash` (`filehash`),
  ADD KEY `txhash` (`txhash`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tbl_action`
--
ALTER TABLE `tbl_action`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_action_ontid`
--
ALTER TABLE `tbl_action_ontid`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_action_ontid_control`
--
ALTER TABLE `tbl_action_ontid_control`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_attestation`
--
ALTER TABLE `tbl_attestation`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_attestation_company`
--
ALTER TABLE `tbl_attestation_company`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_event`
--
ALTER TABLE `tbl_event`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_sfl_identity`
--
ALTER TABLE `tbl_sfl_identity`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_sfl_notary`
--
ALTER TABLE `tbl_sfl_notary`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
