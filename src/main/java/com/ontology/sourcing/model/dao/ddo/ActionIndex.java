package com.ontology.sourcing.model.dao.ddo;

import lombok.Data;

import javax.persistence.*;


@Data
@Table(name = "tbl_action_index")
public class ActionIndex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private Integer flag;
}