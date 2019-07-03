package com.ontology.sourcing.mapper.attestation;

import com.ontology.sourcing.model.dao.contract.ContractOntid;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface ContractOntidMapper extends Mapper<ContractOntid> {

    // String tableName = "tbl_contract_ontid";
    //
    // ContractOntid findByOntid(String ontid);
    //
    // ContractOntid findFirstByOntidOrderByCreateTimeAsc(String ontid);
    //
    // @Modifying
    // @Transactional
    // @Query(value = "insert into " + tableName + " (ontid,contract_index,create_time) values(?1,?2,?3)", nativeQuery = true)
    // int saveIfIgnore(String ontid,
    //                  int index,
    //                  Date create_time);
}