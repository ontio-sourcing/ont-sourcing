package com.ontology.sourcing.dao.sfl;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "tbl_sfl_notary")
public class SFLNotary {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    //
    @Column String certNo;  // 谁存的，根据证件号查
    //
    @Column(name = "filehash") String filehash;
    @Column(name = "txhash") String txhash;
    @Column String certUrl;
    //
    @Column Integer confirm;
    //
    @Column private Date createTime;
    @Column private Date updateTime;
}