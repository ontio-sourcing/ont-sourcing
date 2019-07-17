package com.github.ontio.sourcing.model.dao.attestation;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "tbl_attestation_company")
public class AttestationCompany {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String ontid;

    @Column
    private String prikey;

    @Column(name = "code_addr")
    private String codeAddr;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

}