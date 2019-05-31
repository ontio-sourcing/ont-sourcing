package com.ontology.sourcing.mapper.util;

import com.ontology.sourcing.dao.util.Sensitive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensitiveMapper extends JpaRepository<Sensitive, Integer> {

}