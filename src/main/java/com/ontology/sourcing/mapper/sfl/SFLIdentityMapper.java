package com.ontology.sourcing.mapper.sfl;

import com.ontology.sourcing.model.dao.sfl.SFLIdentity;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface SFLIdentityMapper extends Mapper<SFLIdentity> {

    // SFLIdentity findByCertNo(String certNo);
}