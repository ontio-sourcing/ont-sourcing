package com.ontology.sourcing.model.dao.ddo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "tbl_action_ontid_control")
public class ActionOntidControl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    private String ontid;

    @Column
    private String control;

    @Column
    private String txhash;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

}