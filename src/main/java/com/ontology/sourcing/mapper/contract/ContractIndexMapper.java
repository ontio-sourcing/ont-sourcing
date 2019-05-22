package com.ontology.sourcing.mapper.contract;

import com.ontology.sourcing.dao.contract.ContractIndex;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractIndexMapper {

    // ========== 增 ==========

    int insert(ContractIndex record);

    // ========== 删 ==========


    // ========== 改 ==========

    //
    int updateByPrimaryKeySelective(ContractIndex record);

    int updateByPrimaryKey(ContractIndex record);

    // ========== 查 ==========

    //
    ContractIndex selectCurrent();

    ContractIndex selectByPrimaryKey(Integer id);

    // ========== 统计 ==========

    //
    int count();

    // ========== 其它 ==========

    //
    int getTableSize(@Param("tableName") String tableName);

    void createNewTable(@Param("newTableName") String newTableName);
}