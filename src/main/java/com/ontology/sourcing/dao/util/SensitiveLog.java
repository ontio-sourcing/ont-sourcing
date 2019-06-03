package com.ontology.sourcing.dao.util;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "tbl_sensitive_log")
public class SensitiveLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String ontid;

    @Column
    private String words;

    @Column
    private String content;

    @Column
    private Date createTime;

    @Column
    private Date updateTime;

}