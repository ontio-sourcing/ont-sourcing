package com.ontology.sourcing.mapper.sfl;

import com.ontology.sourcing.dao.sfl.SFLIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SFLIdentityMapper extends JpaRepository<SFLIdentity, Integer> {

    SFLIdentity findByCertNo(String certNo);
}