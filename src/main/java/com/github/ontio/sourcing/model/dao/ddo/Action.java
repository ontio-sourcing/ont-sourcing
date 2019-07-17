package com.github.ontio.sourcing.model.dao.ddo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "tbl_action")
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String ontid;

    @Column
    private String control;

    @Column
    private String txhash;

    @Column
    private Integer type;

    @Column
    private String detail;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "create_time")
    private Date updateTime;
}