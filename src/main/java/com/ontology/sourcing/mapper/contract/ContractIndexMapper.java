package com.ontology.sourcing.mapper.contract;

import com.ontology.sourcing.dao.contract.ContractIndex;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractIndexMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ContractIndex record);

    int insertSelective(ContractIndex record);

    ContractIndex selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ContractIndex record);

    int updateByPrimaryKey(ContractIndex record);

    //
    int count();
    ContractIndex selectCurrent();
    int getTableSize(@Param("tableName") String tableName);
    void createNewTable(@Param("newTableName") String newTableName);
}