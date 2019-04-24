package com.ontology.sourcing.mapper.contract;

import com.ontology.sourcing.dao.contract.ContractCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractCompanyMapper extends JpaRepository<ContractCompany, Integer> {

    ContractCompany findByOntid(String ontid);
}