package com.github.ontio.sourcing.mapper.attestation;

import com.github.ontio.sourcing.model.dao.attestation.AttestationCompany;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface AttestationCompanyMapper extends Mapper<AttestationCompany> {
}