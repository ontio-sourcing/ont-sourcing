package com.github.ontio.sourcing.model.dao.attestation;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "tbl_attestation")
public class Attestation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String ontid;

    @Column(name = "company_ontid")
    private String companyOntid;

    @Column
    private String detail;

    @Column
    private String type;

    @Column
    private Date timestamp;

    @Column(name = "timestamp_sign")
    private String timestampSign;

    @Column
    private String filehash;

    @Column
    private String txhash;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column
    private Integer status;  //

    @Column(name = "revoke_tx")
    private String revokeTx;

    @Transient
    private Integer height;  // 跨表
}
