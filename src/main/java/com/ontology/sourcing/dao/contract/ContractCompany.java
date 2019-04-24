package com.ontology.sourcing.dao.contract;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "tbl_contract_company")
public class ContractCompany {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String ontid;

    @Column
    private String prikey;

    @Column
    private String codeAddr;

    @Column
    private Date createTime;

    @Column
    private Date updateTime;

}