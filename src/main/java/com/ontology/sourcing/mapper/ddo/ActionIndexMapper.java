package com.ontology.sourcing.mapper.ddo;

import com.ontology.sourcing.dao.ddo.ActionIndex;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionIndexMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ActionIndex record);

    int insertSelective(ActionIndex record);

    ActionIndex selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ActionIndex record);

    int updateByPrimaryKey(ActionIndex record);

    //
    int count();
    ActionIndex selectCurrent();
    int getTableSize(@Param("tableName") String tableName);
    void createNewTable(@Param("newTableName") String newTableName);

}