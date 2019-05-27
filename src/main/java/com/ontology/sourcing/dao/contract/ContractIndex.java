package com.ontology.sourcing.dao.contract;

import com.ontology.sourcing.mapper.contract.ContractIndexMapper;
import lombok.Data;

import javax.persistence.*;

// @Component
@Data
@Entity
@Table(name = ContractIndexMapper.tableName)
public class ContractIndex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private Integer flag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}