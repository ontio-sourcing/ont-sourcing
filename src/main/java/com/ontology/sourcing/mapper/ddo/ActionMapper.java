package com.ontology.sourcing.mapper.ddo;

import com.ontology.sourcing.model.common.ddo.Action;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActionMapper {

    int insert(@Param("tableName") String tableName, @Param("record") Action record);

    int insertSelective(@Param("tableName") String tableName, @Param("record") Action record);

    Action selectByOntid(@Param("tableName") String tableName, @Param("ontid") String ontid);

    int updateByOntidSelective(@Param("tableName") String tableName, @Param("record") Action record);

    int updateByOntidWithBLOBs(@Param("tableName") String tableName, @Param("record") Action record);

    int updateByOntid(@Param("tableName") String tableName, @Param("record") Action record);

    int deleteByOntid(@Param("tableName") String tableName, @Param("ontid") String ontid);

    //
    int count(@Param("tableName") String tableName, @Param("ontid") String ontid);

    //
    List<Action> selectByPageNumSize(@Param("tableName") String tableName, @Param("ontid") String ontid, @Param("start") int start, @Param("offset") int offset);
}