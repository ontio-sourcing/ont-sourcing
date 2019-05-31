package com.ontology.sourcing.dao.util;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "tbl_sensitive")
public class Sensitive {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String word;

    @Column
    private Date createTime;

    @Column
    private Date updateTime;

}