package com.ontology.sourcing.dao.sfl;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "tbl_sfl_identity")
public class SFLIdentity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    //
    @Column String userType;
    @Column String certType;
    @Column String certName;
    @Column String certNo;
    //
    @Column String mobileNo;
    //
    @Column String legalPerson;
    @Column String legalPersonId;
    @Column String agent;
    @Column String agentId;
    //
    @Column String properties;
    //
    @Column String token;
    //
    @Column private Date createTime;
    @Column private Date updateTime;
}