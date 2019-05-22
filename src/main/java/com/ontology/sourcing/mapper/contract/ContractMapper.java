package com.ontology.sourcing.mapper.contract;

import com.ontology.sourcing.dao.contract.Contract;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ContractMapper {

    int insert(@Param("tableName") String tableName, @Param("record") Contract record);

    int insertSelective(@Param("tableName") String tableName, @Param("record") Contract record);

    //
    void insertBatch(@Param("tableName") String tableName, @Param("contractList") List<Contract> contractList);

    //
    List<Contract> selectByOntidAndTxHash(@Param("tableName") String tableName, @Param("ontid") String ontid, @Param("txhash") String txhash);

    List<Contract> selectByOntidAndHash(@Param("tableName") String tableName, @Param("ontid") String ontid, @Param("hash") String hash);

    //
    List<Contract> deleteByOntidAndHash(@Param("tableName") String tableName, @Param("ontid") String ontid, @Param("hash") String hash);

    //
    List<Contract> selectByHash(@Param("tableName") String tableName, @Param("hash") String hash);

    //
    int count(@Param("tableName") String tableName, @Param("ontid") String ontid);

    List<Contract> selectByOntidPageNumSize(@Param("tableName") String tableName, @Param("ontid") String ontid, @Param("start") int start, @Param("offset") int offset);

    List<Contract> selectByOntidPageNumSizeAndType(@Param("tableName") String tableName,
                                                   @Param("ontid") String ontid,
                                                   @Param("start") int start,
                                                   @Param("offset") int offset,
                                                   @Param("type") String type);

    //
    List<Contract> selectByPageNumSize(@Param("tableName") String tableName, @Param("start") int start, @Param("offset") int offset);

    //
    int updateRevokeTx(@Param("tableName") String tableName, @Param("txhash") String txhash, @Param("revokeTx") String revokeTx, @Param("updateTime") Date updateTime);
}