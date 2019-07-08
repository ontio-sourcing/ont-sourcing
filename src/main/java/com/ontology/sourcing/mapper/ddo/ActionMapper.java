package com.ontology.sourcing.mapper.ddo;

import com.ontology.sourcing.model.dao.ddo.Action;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface ActionMapper extends Mapper<Action> {
}