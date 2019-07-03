package com.ontology.sourcing.mapper.attestation;

import com.ontology.sourcing.model.dao.contract.ContractCompany;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface ContractCompanyMapper extends Mapper<ContractCompany> {

    // ContractCompany findByOntid(String ontid);
}