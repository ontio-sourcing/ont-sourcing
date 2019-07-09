-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 172.17.0.1:3306
-- Generation Time: May 06, 2019 at 12:07 PM
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
-- Database: `ont-sourcing`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_action_index`
--
Drop Table If Exists `tbl_action_index`;
CREATE TABLE `tbl_action_index` (
  `id` int(11) NOT NULL,
  `name` varchar(64) NOT NULL,
  `flag` int(11) NOT NULL DEFAULT '0' COMMENT '当前操作表'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_action_ontid`
--
Drop Table If Exists `tbl_action_ontid`;
CREATE TABLE `tbl_action_ontid` (
  `id` int(11) NOT NULL,
  `action_index` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `ddo` text,
  `keystore` text,
  `ontid` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `txhash` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_action_ontid_control`
--
Drop Table If Exists `tbl_action_ontid_control`;
CREATE TABLE `tbl_action_ontid_control` (
  `id` int(11) NOT NULL,
  `control` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `ontid` varchar(255) DEFAULT NULL,
  `txhash` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_contract_company`
--
Drop Table If Exists `tbl_contract_company`;
CREATE TABLE `tbl_contract_company` (
  `id` int(11) NOT NULL,
  `ontid` varchar(128) NOT NULL,
  `prikey` varchar(128) NOT NULL COMMENT '私钥，必须和合约中的地址相对应',
  `code_addr` varchar(128) NOT NULL COMMENT '智能合约地址',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_contract_index`
--
Drop Table If Exists `tbl_contract_index`;
CREATE TABLE `tbl_contract_index` (
  `id` int(11) NOT NULL,
  `name` varchar(64) NOT NULL,
  `flag` int(11) NOT NULL DEFAULT '0' COMMENT '当前操作表'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_contract_ontid`
--
Drop Table If Exists `tbl_contract_ontid`;
CREATE TABLE `tbl_contract_ontid` (
  `id` int(11) NOT NULL,
  `ontid` varchar(255) DEFAULT NULL,
  `contract_index` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_event`
--
Drop Table If Exists `tbl_event`;
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
Drop Table If Exists `tbl_sfl_identity`;
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
Drop Table If Exists `tbl_sfl_notary`;
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
-- Indexes for table `tbl_action_index`
--
ALTER TABLE `tbl_action_index`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_action_ontid`
--
ALTER TABLE `tbl_action_ontid`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_action_ontid_control`
--
ALTER TABLE `tbl_action_ontid_control`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_contract_company`
--
ALTER TABLE `tbl_contract_company`
  ADD PRIMARY KEY (`id`),
  ADD KEY `ontid` (`ontid`);

--
-- Indexes for table `tbl_contract_index`
--
ALTER TABLE `tbl_contract_index`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_contract_ontid`
--
ALTER TABLE `tbl_contract_ontid`
  ADD PRIMARY KEY (`id`);

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
-- AUTO_INCREMENT for table `tbl_action_index`
--
ALTER TABLE `tbl_action_index`
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
-- AUTO_INCREMENT for table `tbl_contract_company`
--
ALTER TABLE `tbl_contract_company`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_contract_index`
--
ALTER TABLE `tbl_contract_index`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_contract_ontid`
--
ALTER TABLE `tbl_contract_ontid`
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