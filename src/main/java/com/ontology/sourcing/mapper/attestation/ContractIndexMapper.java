package com.ontology.sourcing.mapper.attestation;

import com.ontology.sourcing.model.dao.contract.ContractIndex;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface ContractIndexMapper extends Mapper<ContractIndex> {

    // String tableName = "tbl_contract_index";
    //
    //
    // // ========== 增 ==========
    //
    //
    // // ========== 删 ==========
    //
    //
    // // ========== 改 ==========
    //
    //
    // // ========== 查 ==========
    //
    // @Query(value = "SELECT * from " + tableName + " where flag = '1'", nativeQuery = true)
    // ContractIndex selectCurrent();
    //
    //
    // // ========== 统计 ==========
    //
    //
    // // ========== 其它 ==========
    //
    // @Query(value = "SELECT round(((data_length + index_length) / 1024 / 1024 /1024), 2) `Size in GB` FROM information_schema.TABLES WHERE table_name = ?1", nativeQuery = true)
    // int getTableSize(String tableName);
    //
    // //
    // @Modifying
    // @Transactional
    // @Query(value = "CREATE TABLE ?1 ( `id` int(11) NOT NULL AUTO_INCREMENT, `ontid` varchar(255) NOT NULL, `company_ontid` varchar(255) NOT NULL, `txhash` varchar(255) NOT NULL, `filehash` varchar(255) NOT NULL, `detail` text NOT NULL, `type` varchar(16) NOT NULL, `timestamp` datetime NOT NULL, `timestamp_sign` text NOT NULL, `create_time` datetime NOT NULL, `update_time` datetime DEFAULT NULL, `status` int(11) NOT NULL DEFAULT '0' COMMENT '0-未删除；1-已删除；', `revoke_tx` varchar(255) DEFAULT NULL, PRIMARY KEY (`id`), UNIQUE KEY `txhash` (`txhash`) USING BTREE, KEY `filehash` (`filehash`), KEY `ontid` (`ontid`), KEY `company_ontid` (`company_ontid`), KEY `type` (`type`) ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8", nativeQuery = true)
    // void createNewTable(String newTableName);
}