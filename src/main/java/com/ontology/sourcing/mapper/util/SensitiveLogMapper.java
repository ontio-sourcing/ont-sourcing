package com.ontology.sourcing.mapper.util;

import com.ontology.sourcing.dao.util.SensitiveLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensitiveLogMapper extends JpaRepository<SensitiveLog, Integer> {

}