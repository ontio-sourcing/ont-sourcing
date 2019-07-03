package com.ontology.sourcing.model.common.attestation;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
public class Attestation {

    private String ontid;

    // private String companyOntid = "";
    private String companyOntid;

    private String detail;

    private String type;

    private Date timestamp;

    private String timestampSign;

    private String filehash;

    private String txhash;

    private Date createTime;

    private Date updateTime;

    private Integer status;  //

    private Integer height;  // 跨表

    private String revokeTx;
}