package com.github.ontio.sourcing.model.dao.sfl;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "tbl_sfl_identity")
public class SFLIdentity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //
    @Column(name = "user_type")
    String userType;
    @Column(name = "cert_type")
    String certType;
    @Column(name = "cert_name")
    String certName;
    @Column(name = "cert_no")
    String certNo;

    //
    @Column(name = "mobile_no")
    String mobileNo;

    //
    @Column(name = "legal_person")
    String legalPerson;
    @Column(name = "legal_person_id")
    String legalPersonId;

    //
    @Column
    String agent;
    @Column(name = "agent_id")
    String agentId;

    //
    @Column
    String properties;

    //
    @Column
    String token;

    //
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}