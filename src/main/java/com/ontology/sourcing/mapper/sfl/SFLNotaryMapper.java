package com.ontology.sourcing.mapper.sfl;

import com.ontology.sourcing.model.dao.sfl.SFLNotary;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface SFLNotaryMapper extends Mapper<SFLNotary> {

    // SFLNotary findByTxhash(String txhash);
    //
    // @Query(value = "select * from tbl_sfl_notary where cert_no = :certNo order by create_time desc limit :start,:offset", nativeQuery = true)
    // List<SFLNotary> findByCertNoPageable(@Param("certNo") String certNo, @Param("start") Integer start, @Param("offset") Integer offset);
    //
    // SFLNotary findByFilehash(String filehash);
    //
    // long countByCertNo(String certNo);
}