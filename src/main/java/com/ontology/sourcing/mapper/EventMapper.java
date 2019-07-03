package com.ontology.sourcing.mapper;

import com.ontology.sourcing.model.dao.Event;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface EventMapper extends Mapper<Event> {

}