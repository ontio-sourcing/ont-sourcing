package com.github.ontio.sourcing.service.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PropertiesService {

    public final int TABLE_SIZE_LIMIT;
    public final int TABLE_SIZE_DETECT_INTERVAL;

    // 时间戳
    public final String[] TIMESTAMP_URL_LIST;

    @Autowired
    public PropertiesService(@Value("${com.ontology.sourcing.TABLE_SIZE_LIMIT}") int limit,
                             @Value("${com.ontology.sourcing.TABLE_SIZE_DETECT_INTERVAL}") int interval,
                             @Value("${com.ontology.sourcing.TIMESTAMP_URL_LIST}") String[] timestampUrlList,
                             @Value("${com.ontology.sourcing.PAYER_PRIVATE_KEY}") String payerPrivateKey,
                             @Value("${com.ontology.sourcing.WALLET_PATH}") String walletPath,
                             @Value("${com.ontology.sourcing.ONTOLOGY_URL_LIST}") String[] ontologyUrlList,
                             @Value("${com.ontology.sourcing.ONTID_PUBLIC_KEY}") String ontidPublicKey,
                             @Value("${com.ontology.sourcing.CONTRACT_CODE_ADDRESS}") String codeAddr,
                             @Value("${com.ontology.sourcing.SFL.ACCOUNTID}") String sflAccountId,
                             @Value("${com.ontology.sourcing.SFL.PRIKey}") String sflPriKey,
                             @Value("${com.ontology.sourcing.ONT_PASSWORD}") String ontPassword) {
        //
        this.TABLE_SIZE_LIMIT = limit;
        this.TABLE_SIZE_DETECT_INTERVAL = interval;
        //
        this.TIMESTAMP_URL_LIST = timestampUrlList;
        //
        this.payerPrivateKey = payerPrivateKey;
        this.walletPath = walletPath;
        this.ontologyUrlList = ontologyUrlList;
        this.ontidPublicKey = ontidPublicKey;
        //
        this.codeAddr = codeAddr;
        //
        this.sflAccountId = sflAccountId;
        this.sflPriKey = sflPriKey;
        //
        this.ontPassword = ontPassword;
    }

    // 付款的数字钱包
    public String payerPrivateKey;

    //
    public String walletPath;


    //
    public final String[] ontologyUrlList;


    /**
     * ontid后台的公钥,测试网的
     * ONT ID backend public key
     */
    public String ontidPublicKey;

    // 存证智能合约地址
    public String codeAddr;

    // 司法链
    public String sflAccountId;
    public String sflPriKey;

    // 项目方添加定制的合约和付款秘钥，需要这个密码进行验证
    public String ontPassword;
}
