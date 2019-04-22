package com.ontology.sourcing.mapper;

import com.ontology.sourcing.dao.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventMapper extends JpaRepository<Event, Integer> {

    // 根据规则自定义的
    Event findByTxhash(String txhash);

}