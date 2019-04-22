package com.ontology.sourcing.dao.ddo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "tbl_action_ontid_control")
public class ActionOntidControl {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String ontid;

    @Column
    private String control;

    @Column
    private String txhash;

    @Column
    private Date createTime;

    @Column
    private Date updateTime;

 }