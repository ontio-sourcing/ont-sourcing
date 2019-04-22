package com.ontology.sourcing.dao;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "tbl_event")
public class Event {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String txhash;

    @Column
    private String event;

    @Column
    private int height;

    @Column
    private Date createTime;

    @Column
    private Date updateTime;
}
