package com.ontology.sourcing.mapper.contract;

import com.ontology.sourcing.dao.contract.ContractOntid;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
public interface ContractOntidMapper extends JpaRepository<ContractOntid, Integer> {

    ContractOntid findByOntid(String ontid);

    ContractOntid findFirstByOntidOrderByCreateTimeAsc(String ontid);

    @Modifying
    @Transactional
    @Query(value = "insert into tbl_contract_ontid (ontid,contract_index,create_time) values(?1,?2,?3)", nativeQuery = true)
    int saveIfIgnore(String ontid, int index, Date create_time);
}