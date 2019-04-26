package com.ontology.sourcing.mapper.contract;

import com.ontology.sourcing.dao.contract.ContractOntid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractOntidMapper extends JpaRepository<ContractOntid, Integer> {

    ContractOntid findByOntid(String ontid);

    ContractOntid findFirstByOntidOrderByCreateTimeAsc(String ontid);
}