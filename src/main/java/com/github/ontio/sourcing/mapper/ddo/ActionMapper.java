package com.github.ontio.sourcing.mapper.ddo;

import com.github.ontio.sourcing.model.dao.ddo.Action;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface ActionMapper extends Mapper<Action> {
}