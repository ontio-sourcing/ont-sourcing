package com.ontology.sourcing.service;

import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.ontology.sourcing.exception.exp.ErrorCode;
import com.ontology.sourcing.exception.exp.ONTSourcingException;
import com.ontology.sourcing.mapper.sfl.SFLIdentityMapper;
import com.ontology.sourcing.mapper.sfl.SFLNotaryMapper;
import com.ontology.sourcing.model.dao.sfl.SFLIdentity;
import com.ontology.sourcing.model.dao.sfl.SFLNotary;
import com.ontology.sourcing.model.dto.sfl.SFLResponse;
import com.ontology.sourcing.service.util.PropertiesService;
import com.ontology.sourcing.service.util.SyncService;
import com.ontology.sourcing.util.HttpUtil;
import com.ontology.sourcing.util.sfl.CertUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class SFLService {

    //
    private Gson gson = new Gson();

    //
    private PropertiesService propertiesService;

    //
    SFLIdentityMapper sflIdentityMapper;
    SFLNotaryMapper sflNotaryMapper;
    //
    SyncService syncService;

    // TODO
    Integer BizId = 1;
    String subBizId = "TEXT";

    //
    @Autowired
    public SFLService(PropertiesService propertiesService,
                      SyncService syncService,
                      SFLIdentityMapper sflIdentityMapper,
                      SFLNotaryMapper sflNotaryMapper) {
        //
        this.propertiesService = propertiesService;
        this.syncService = syncService;
        //
        this.sflIdentityMapper = sflIdentityMapper;
        this.sflNotaryMapper = sflNotaryMapper;
    }

    //
    public String getToken(SFLIdentity sflIdentity) throws Exception {

        //
        Example example = new Example(SFLIdentity.class);
        example.createCriteria().andCondition("cert_no=", sflIdentity.getCertNo());
        SFLIdentity existed = sflIdentityMapper.selectOneByExample(example);
        //
        if (existed != null) {  // TODO 以后token可能会失效
            return existed.getToken();
        }
        //
        String timestamp = String.valueOf(System.currentTimeMillis());

        String signedData = CertUtil.sign(propertiesService.sflAccountId + BizId + timestamp, propertiesService.sflPriKey);

        JSONObject obj = new JSONObject();
        obj.put("accountId", propertiesService.sflAccountId);
        obj.put("bizId", BizId);

        JSONObject iden = new JSONObject();
        iden.put("userType", sflIdentity.getUserType());
        iden.put("certType", sflIdentity.getCertType());
        iden.put("certName", sflIdentity.getCertName());
        iden.put("certNo", sflIdentity.getCertNo());
        iden.put("mobileNo", sflIdentity.getMobileNo());
        iden.put("legalPerson", sflIdentity.getLegalPerson());
        iden.put("legalPersonId", sflIdentity.getLegalPersonId());
        iden.put("agent", sflIdentity.getAgent());
        iden.put("agentId", sflIdentity.getAgentId());
        iden.put("properties", sflIdentity.getProperties());

        obj.put("customer", iden);

        obj.put("timestamp", timestamp);
        obj.put("signedData", signedData);
        String jsonString = obj.toString();

        HttpUtil.HttpInfo httpInfo = HttpUtil.doPost("https://check.netcourt.gov.cn/api/blockChain/notaryToken", HttpUtil.JSON, obj);

        System.out.println(httpInfo.responseBody);

        SFLResponse response = gson.fromJson(httpInfo.responseBody, SFLResponse.class);

        //
        if (!response.getSuccess()) {
            // throw new Exception(response.getErrMessage());
            throw new ONTSourcingException(response.getErrMessage(), ErrorCode.SFL_ERROR);
        }

        //
        String token = response.getResponseData();

        //
        sflIdentity.setToken(token);
        sflIdentity.setCreateTime(new Date());
        sflIdentityMapper.insertSelective(sflIdentity);

        //
        return token;
    }

    //
    public void put(String filehash,
                    String token,
                    String certNo) throws Exception {

        String timestamp = String.valueOf(System.currentTimeMillis());

        JSONObject notaryMeta = new JSONObject();
        notaryMeta.put("token", token);
        String phase = "";  // TODO
        notaryMeta.put("phase", phase);
        notaryMeta.put("accountId", propertiesService.sflAccountId);

        JSONObject obj = new JSONObject();
        obj.put("meta", notaryMeta);
        obj.put("notaryContent", filehash);
        obj.put("timestamp", timestamp);

        String signedData = CertUtil.sign(propertiesService.sflAccountId + phase + timestamp, propertiesService.sflPriKey);
        obj.put("signedData", signedData);

        //
        HttpUtil.HttpInfo httpInfo = HttpUtil.doPost("https://check.netcourt.gov.cn/api/blockChain/notaryCertUrl", HttpUtil.JSON, obj);

        //
        String certUrl = httpInfo.responseHeaders.get("certUrl");

        //
        SFLResponse response = gson.fromJson(httpInfo.responseBody, SFLResponse.class);
        String txhash = response.getResponseData();

        //
        SFLNotary notary = new SFLNotary();
        notary.setTxhash(txhash);
        notary.setFilehash(filehash);
        notary.setCertNo(certNo);
        notary.setCertUrl(certUrl);
        notary.setCreateTime(new Date());
        sflNotaryMapper.insertSelective(notary);

        //
        syncService.confirmTxSFL(txhash, filehash);
    }

    public List<SFLNotary> getHistoryByCertNo(String certNo,
                                               int pageNum,
                                               int pageSize) {
        //
        PageHelper.startPage(pageNum, pageSize);
        //
        Example example = new Example(SFLNotary.class);
        example.createCriteria().andCondition("cert_no=", certNo);
        example.setOrderByClause("id desc");
        List<SFLNotary> list = sflNotaryMapper.selectByExample(example);
        //
        return list;
    }
}
