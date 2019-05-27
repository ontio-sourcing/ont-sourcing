package com.ontology.sourcing.mapper.contract;

import com.ontology.sourcing.dao.contract.Contract;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ContractMapper {

    // ========== 增 ==========

    int insert(@Param("tableName") String tableName,
               @Param("record") Contract record);

    int insertSelective(@Param("tableName") String tableName,
                        @Param("record") Contract record);

    void insertBatch(@Param("tableName") String tableName,
                     @Param("contractList") List<Contract> contractList);


    // ========== 删 ==========


    // ========== 改 ==========


    int updateStatusByOntidAndHash(@Param("tableName") String tableName,
                                   @Param("ontid") String ontid,
                                   @Param("hash") String hash);

    int updateRevokeTx(@Param("tableName") String tableName,
                       @Param("txhash") String txhash,
                       @Param("revokeTx") String revokeTx,
                       @Param("updateTime") Date updateTime);


    // ========== 查 ==========

    List<Contract> selectByOntidAndTxHash(@Param("tableName") String tableName,
                                          @Param("ontid") String ontid,
                                          @Param("txhash") String txhash);

    List<Contract> selectByOntidAndHash(@Param("tableName") String tableName,
                                        @Param("ontid") String ontid,
                                        @Param("hash") String hash);

    List<Contract> selectByHash(@Param("tableName") String tableName,
                                @Param("hash") String hash);

    List<Contract> selectByOntidAndPage(@Param("tableName") String tableName,
                                        @Param("ontid") String ontid,
                                        @Param("start") int start,
                                        @Param("offset") int offset);

    List<Contract> selectByOntidAndPageAndType(@Param("tableName") String tableName,
                                               @Param("ontid") String ontid,
                                               @Param("start") int start,
                                               @Param("offset") int offset,
                                               @Param("type") String type);

    List<Contract> selectByPage(@Param("tableName") String tableName,
                                @Param("start") int start,
                                @Param("offset") int offset);


    // ========== 统计 ==========

    int count(@Param("tableName") String tableName,
              @Param("ontid") String ontid);

}