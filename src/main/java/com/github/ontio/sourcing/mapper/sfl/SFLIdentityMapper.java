package com.github.ontio.sourcing.mapper.sfl;

import com.github.ontio.sourcing.model.dao.sfl.SFLIdentity;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface SFLIdentityMapper extends Mapper<SFLIdentity> {

    // SFLIdentity findByCertNo(String certNo);
}