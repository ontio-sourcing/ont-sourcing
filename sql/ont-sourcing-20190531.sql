drop table if exists `tbl_sensitive`;
CREATE TABLE `tbl_sensitive`
(
    `id`          INT          NOT NULL AUTO_INCREMENT,
    `word`        VARCHAR(128) NOT NULL,
    `create_time` DATETIME     NOT NULL,
    `update_time` DATETIME     NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  CHARSET = utf8
  COLLATE utf8_general_ci;


drop table if exists `tbl_sensitive_log`;
CREATE TABLE `tbl_sensitive_log`
(
    `id`          INT          NOT NULL AUTO_INCREMENT,
    `ontid`       VARCHAR(128) NOT NULL,
    `words`       TEXT         NOT NULL,
    `create_time` DATETIME     NOT NULL,
    `update_time` DATETIME     NULL,
    PRIMARY KEY (`id`),
    INDEX (`ontid`)
) ENGINE = InnoDB
  CHARSET = utf8
  COLLATE utf8_general_ci;