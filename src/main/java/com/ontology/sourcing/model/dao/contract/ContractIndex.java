package com.ontology.sourcing.model.dao.contract;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "tbl_contract_index")
public class ContractIndex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private Integer flag;
}