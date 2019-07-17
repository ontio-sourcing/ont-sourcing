package com.github.ontio.sourcing.mapper;

import com.github.ontio.sourcing.model.dao.Event;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface EventMapper extends Mapper<Event> {

}