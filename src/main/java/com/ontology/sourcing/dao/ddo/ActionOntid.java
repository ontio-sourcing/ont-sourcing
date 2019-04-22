package com.ontology.sourcing.dao.ddo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "tbl_action_ontid")
public class ActionOntid {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String ontid;

    @Column
    private String txhash;

    @Column
    private String keystore;

    @Column
    private String ddo;

    @Column
    private Date createTime;

    @Column
    private Date updateTime;

    @Column
    private Integer actionIndex;

}