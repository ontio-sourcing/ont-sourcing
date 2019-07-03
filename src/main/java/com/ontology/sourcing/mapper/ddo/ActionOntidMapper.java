package com.ontology.sourcing.mapper.ddo;

import com.ontology.sourcing.model.dao.ddo.ActionOntid;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface ActionOntidMapper extends Mapper<ActionOntid> {

    // ActionOntid findByOntid(String ontid);

    // ActionOntid findByUsername(String username);
}