package com.ontology.sourcing.mapper.ddo;

import com.ontology.sourcing.dao.ddo.ActionOntid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionOntidMapper extends JpaRepository<ActionOntid, Integer> {

    ActionOntid findByOntid(String ontid);
}