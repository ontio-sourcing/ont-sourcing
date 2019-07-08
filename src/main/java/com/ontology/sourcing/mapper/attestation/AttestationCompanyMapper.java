package com.ontology.sourcing.mapper.attestation;

import com.ontology.sourcing.model.dao.attestation.AttestationCompany;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface AttestationCompanyMapper extends Mapper<AttestationCompany> {
}