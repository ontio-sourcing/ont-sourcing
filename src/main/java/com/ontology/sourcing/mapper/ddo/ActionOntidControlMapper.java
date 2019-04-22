package com.ontology.sourcing.mapper.ddo;

import com.ontology.sourcing.dao.ddo.ActionOntidControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionOntidControlMapper extends JpaRepository<ActionOntidControl, Integer> {

}