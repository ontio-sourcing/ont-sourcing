package com.ontology.sourcing.model.common.ddo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
public class Action {

    private String ontid;

    private String control;

    private String txhash;

    private Integer type;

    private Date createTime;

    private Date updateTime;

    private String detail;
}