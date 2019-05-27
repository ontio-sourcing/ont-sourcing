package com.ontology.sourcing.dao.contract;

import com.ontology.sourcing.mapper.contract.ContractOntidMapper;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = ContractOntidMapper.tableName)
public class ContractOntid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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