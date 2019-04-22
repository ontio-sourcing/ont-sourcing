package com.ontology.sourcing.dao.contract;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "tbl_contract_ontid")
public class ContractOntid {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String ontid;

    @Column
    private Date createTime;

    @Column
    private Date updateTime;

    @Column
    private Integer contractIndex;

}