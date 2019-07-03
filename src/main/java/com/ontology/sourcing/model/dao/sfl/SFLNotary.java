package com.ontology.sourcing.model.dao.sfl;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "tbl_sfl_notary")
public class SFLNotary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //
    @Column(name = "filehash")
    String filehash;

    @Column(name = "txhash")
    String txhash;

    //
    @Column(name = "cert_no")
    String certNo;  // 谁存的，根据证件号查

    @Column(name = "cert_url")
    String certUrl;

    //
    @Column
    Integer confirm;

    //
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}