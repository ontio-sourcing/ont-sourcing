package com.ontology.sourcing.mapper.attestation;

import com.ontology.sourcing.model.common.attestation.Attestation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ContractMapper {

    // ========== 增 ==========

    int insert(@Param("tableName") String tableName,
               @Param("record") Attestation record);

    int insertSelective(@Param("tableName") String tableName,
                        @Param("record") Attestation record);

    void insertBatch(@Param("tableName") String tableName,
                     @Param("contractList") List<Attestation> attestationList);


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

    List<Attestation> selectByOntidAndTxHash(@Param("tableName") String tableName,
                                             @Param("ontid") String ontid,
                                             @Param("txhash") String txhash);

    List<Attestation> selectByOntidAndHash(@Param("tableName") String tableName,
                                           @Param("ontid") String ontid,
                                           @Param("hash") String hash);

    List<Attestation> selectByHash(@Param("tableName") String tableName,
                                   @Param("hash") String hash);

    List<Attestation> selectByOntidAndPage(@Param("tableName") String tableName,
                                           @Param("ontid") String ontid,
                                           @Param("start") int start,
                                           @Param("offset") int offset);

    List<Attestation> selectByOntidAndPageAndType(@Param("tableName") String tableName,
                                                  @Param("ontid") String ontid,
                                                  @Param("start") int start,
                                                  @Param("offset") int offset,
                                                  @Param("type") String type);

    List<Attestation> selectByPage(@Param("tableName") String tableName,
                                   @Param("start") int start,
                                   @Param("offset") int offset);


    // ========== 统计 ==========

    int count(@Param("tableName") String tableName,
              @Param("ontid") String ontid);

}