package com.ontology.sourcing.mapper.sfl;

import com.ontology.sourcing.dao.sfl.SFLNotary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SFLNotaryMapper extends JpaRepository<SFLNotary, Integer> {

    SFLNotary findByTxhash(String txhash);

    @Query(value = "select * from tbl_sfl_notary where cert_no = :certNo order by create_time desc limit :start,:offset", nativeQuery = true)
    List<SFLNotary> findByCertNoPageable(@Param("certNo") String certNo, @Param("start") Integer start, @Param("offset") Integer offset);

    SFLNotary findByFilehash(String filehash);

    long countByCertNo(String certNo);
}