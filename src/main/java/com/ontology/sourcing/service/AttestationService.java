package com.ontology.sourcing.service;

import ch.qos.logback.classic.Logger;
import com.alibaba.fastjson.JSON;
import com.github.ontio.account.Account;
import com.github.ontio.common.Address;
import com.github.ontio.common.Helper;
import com.github.ontio.core.transaction.Transaction;
import com.github.ontio.network.exception.ConnectorException;
import com.github.ontio.sdk.exception.SDKException;
import com.github.ontio.smartcontract.neovm.abi.BuildParams;
import com.github.pagehelper.PageHelper;
import com.ontology.sourcing.mapper.EventMapper;
import com.ontology.sourcing.mapper.attestation.AttestationCompanyMapper;
import com.ontology.sourcing.mapper.attestation.AttestationMapper;
import com.ontology.sourcing.model.dao.Event;
import com.ontology.sourcing.model.dao.attestation.Attestation;
import com.ontology.sourcing.model.dao.attestation.AttestationCompany;
import com.ontology.sourcing.service.util.ChainService;
import com.ontology.sourcing.service.util.PropertiesService;
import com.ontology.sourcing.util.GlobalVariable;
import com.ontology.sourcing.util._hash.Sha256Util;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.*;

@Service
public class AttestationService {

    //
    private Logger logger = (Logger) LoggerFactory.getLogger(AttestationService.class);

    //
    private PropertiesService propertiesService;
    private ChainService chainService;
    //
    private AttestationMapper attestationMapper;
    private AttestationCompanyMapper attestationCompanyMapper;
    //
    private EventMapper eventMapper;

    // 公共payer和公共合约
    private String codeAddr;
    private Account payer;

    @Autowired
    public AttestationService(PropertiesService propertiesService,
                              ChainService chainService,
                              AttestationMapper attestationMapper,
                              EventMapper eventMapper,
                              AttestationCompanyMapper attestationCompanyMapper) {
        //
        this.propertiesService = propertiesService;
        this.chainService = chainService;

        //
        this.attestationMapper = attestationMapper;
        this.eventMapper = eventMapper;
        this.attestationCompanyMapper = attestationCompanyMapper;

        // 合约哈希/合约地址/contract codeAddr
        codeAddr = propertiesService.codeAddr;
        payer = GlobalVariable.getInstanceOfAccount(propertiesService.payerPrivateKey);
    }

    public Map<String, String> putContract(Attestation attestation) throws Exception {

        // 上链时，只把指定field的组合后的hash作为key
        String c_key = contractToDigestForKey(attestation);
        // 上链时，只把指定field的组合后的hash作为value
        String c_value = contractToDigestForValue(attestation);

        //
        return putContract(attestation, c_key, c_value);
    }

    public Map<String, String> putContract(Attestation attestation,
                                           String c_key,
                                           String c_value) throws Exception {

        // 先查询是不是项目方，有没有设置指定的payer地址和合约地址
        String c_ontid = attestation.getCompanyOntid();
        //
        Example example = new Example(AttestationCompany.class);
        example.createCriteria().andCondition("ontid=", c_ontid);
        AttestationCompany attestationCompany = attestationCompanyMapper.selectOneByExample(example);
        //
        if (attestationCompany != null) {
            payer = GlobalVariable.getInstanceOfAccount(attestationCompany.getPrikey());
            codeAddr = attestationCompany.getCodeAddr();
            // String s1 = payer.getAddressU160().toBase58();
        } else {
            // TODO
            throw new Exception("项目方地址列表中找不到该ontid..." + c_ontid);
        }
        // String s2 = payer.getAddressU160().toBase58();

        //
        logger.debug("c_key is {}", c_key);
        logger.debug("c_value is {}", c_value);

        //
        List paramList = new ArrayList<>();
        paramList.add("putRecord".getBytes());

        List args = new ArrayList();
        // key
        args.add(c_key);
        // value
        args.add(c_value);

        //
        paramList.add(args);
        byte[] params = BuildParams.createCodeParamsScript(paramList);

        //
        Map<String, String> map = invokeContract(Helper.reverse(codeAddr), null, params, payer, 76220L, GlobalVariable.DEFAULT_GAS_PRICE, false);

        //
        String txhash = map.get("txhash");
        String result = map.get("result");
        // System.out.println(result);  // true

        //
        return map;
    }

    public String getContract(Attestation attestation,
                              Account payer) throws Exception {

        List paramList = new ArrayList<>();
        paramList.add("getRecord".getBytes());

        List args = new ArrayList();
        args.add(contractToDigestForKey(attestation));

        paramList.add(args);
        byte[] params = BuildParams.createCodeParamsScript(paramList);

        // 先查询是不是项目方，有没有设置指定的payer地址和合约地址
        Example example = new Example(AttestationCompany.class);
        example.createCriteria().andCondition("ontid=", attestation.getCompanyOntid());
        AttestationCompany attestationCompany = attestationCompanyMapper.selectOneByExample(example);
        //
        if (attestationCompany != null) {
            payer = GlobalVariable.getInstanceOfAccount(attestationCompany.getPrikey());
            codeAddr = Address.AddressFromVmCode(attestationCompany.getCodeAddr()).toHexString();
        }

        //
        Map<String, String> map = invokeContract(Helper.reverse(codeAddr), null, params, payer, chainService.ontSdk.DEFAULT_GAS_LIMIT, GlobalVariable.DEFAULT_GAS_PRICE, true);

        //
        String txhash = map.get("txhash");
        String result = map.get("result");

        //
        String s1 = JSON.parseObject(result).getString("Result");
        byte[] s2 = Helper.hexToBytes(s1);
        String value = new String(s2);

        //
        return value;
    }

    //
    public Map<String, String> invokeContract(String codeAddr,
                                              String method,
                                              byte[] params,
                                              Account payerAcct,
                                              long gaslimit,
                                              long gasprice,
                                              boolean preExec) throws Exception {

        //
        if (payerAcct == null) {
            throw new SDKException("params should not be null");
        }
        if (gaslimit < 0 || gasprice < 0) {
            throw new SDKException("gaslimit or gasprice should not be less than 0");
        }

        //
        Map<String, String> map = new HashMap<String, String>();

        //
        Transaction tx = chainService.ontSdk.vm().makeInvokeCodeTransaction(codeAddr, method, params, payerAcct.getAddressU160().toBase58(), gaslimit, gasprice);
        // System.out.println(tx);
        // com.github.ontio.core.payload.InvokeCode@fecc2faa

        //
        chainService.ontSdk.addSign(tx, payerAcct);

        // String s = payerAcct.getAddressU160().toBase58();

        //
        String rawdata = tx.toHexString();
        String txhash = tx.hash().toString();
        //
        map.put("txhash", txhash);
        map.put("rawdata", rawdata);  // TODO

        //
        if (preExec) {

            for (int retry = 0; retry < 5; retry++) {
                //
                Object result = null;
                try {
                    result = chainService.ontSdk.getConnect().sendRawTransactionPreExec(rawdata);
                } catch (ConnectorException | IOException e) {
                    logger.error(e.getMessage());
                    //
                    chainService.switchOntSdk();
                    //
                    continue;
                }
                //
                map.put("result", result.toString());
                //
                break;
            }

        } else {

            for (int retry = 0; retry < 5; retry++) {
                //
                boolean result = false;
                try {
                    result = chainService.ontSdk.getConnect().sendRawTransaction(rawdata);
                } catch (ConnectorException | IOException e) {
                    logger.error(e.getMessage());
                    //
                    chainService.switchOntSdk();
                    //
                    continue;
                }
                //
                map.put("result", Boolean.toString(result));
                //
                break;
            }
        }

        //
        return map;
    }

    // 发到链上的key
    public String contractToDigestForKey(Attestation attestation) {
        String k = attestation.getOntid() + attestation.getCompanyOntid() + attestation.getFilehash() + attestation.getTimestamp();
        return Sha256Util.sha256(k);
    }

    // 发到链上的value
    public String contractToDigestForValue(Attestation attestation) {
        String v = attestation.getOntid() + attestation.getCompanyOntid() + attestation.getDetail() + attestation.getTimestamp() + attestation.getTimestampSign();
        return Sha256Util.sha256(v);
    }

    // 后期如果需要验证
    public boolean verifyContractOnBlockchain(Attestation attestation,
                                              Account payer) throws Exception {
        String valueLocal = contractToDigestForValue(attestation);
        String valueOnBlockchain = getContract(attestation, payer);
        return valueOnBlockchain.equals(valueLocal);
    }

    //
    public List<Attestation> getHistoryByOntid(String ontid,
                                               int pageNum,
                                               int pageSize,
                                               String type) throws Exception {
        //
        Example example = new Example(Attestation.class);
        //
        Example.Criteria c1 = example.createCriteria();
        c1.andCondition("dnaid=", ontid);
        c1.andCondition("status=", 0);
        if (!StringUtils.isEmpty(type)) {
            c1.andCondition("type=", type);
        }
        //
        example.setOrderByClause("id desc");
        example.and(c1);
        //
        PageHelper.startPage(pageNum, pageSize, false);
        List<Attestation> list = attestationMapper.selectByExample(example);

        //
        return addHeight(list);
    }

    //
    public List<Attestation> getExplorerHistory(int pageNum,
                                                int pageSize) {
        //
        PageHelper.startPage(pageNum, pageSize);
        //
        Example example = new Example(Attestation.class);
        example.createCriteria().andCondition("status=", 0);
        example.setOrderByClause("id desc");
        List<Attestation> list = attestationMapper.selectByExample(example);
        //
        return addHeight(list);
    }

    //
    public List<Attestation> selectByOntidAndTxHash(String ontid,
                                                    String txhash) throws Exception {
        //
        Example example = new Example(Attestation.class);
        //
        Example.Criteria criteria = example.createCriteria();
        //
        Example.Criteria c1 = example.createCriteria();
        c1.andCondition("dnaid=", ontid);
        c1.andCondition("txhash=", txhash);
        c1.andCondition("status=", 0);
        //
        example.and(c1);
        //
        List<Attestation> list = attestationMapper.selectByExample(example);
        //
        return addHeight(list);
    }

    //
    public List<Attestation> selectByOntidAndHash(String ontid,
                                                  String hash) throws Exception {
        //
        Example example = new Example(Attestation.class);
        //
        Example.Criteria c1 = example.createCriteria();
        c1.andCondition("dnaid=", ontid);
        c1.andCondition("status=", 0);
        //
        Example.Criteria c2 = example.createCriteria();
        c2.orEqualTo("txhash", hash).orEqualTo("filehash", hash);
        //
        example.and(c1);
        example.and(c2);
        //
        List<Attestation> list = attestationMapper.selectByExample(example);
        //
        return addHeight(list);
    }

    //
    public int updateStatusByOntidAndHash(String ontid,
                                          String hash) throws Exception {
        //
        Example example = new Example(Attestation.class);
        //
        Example.Criteria c1 = example.createCriteria();
        c1.andCondition("dnaid=", ontid);
        //
        Example.Criteria c2 = example.createCriteria();
        c2.orEqualTo("txhash", hash).orEqualTo("filehash", hash);
        //
        example.and(c1);
        example.and(c2);
        //
        Attestation c = new Attestation();
        c.setStatus(1);
        return attestationMapper.updateByExampleSelective(c, example);
    }

    //
    public List<Attestation> selectByHash(String hash) {

        //
        Example example = new Example(Attestation.class);
        //
        Example.Criteria c1 = example.createCriteria();
        c1.andCondition("txhash=", hash);
        //
        Example.Criteria c2 = example.createCriteria();
        c2.andCondition("filehash=", hash);
        //
        example.and(c1);
        example.or(c2);
        //
        List<Attestation> list = attestationMapper.selectByExample(example);
        //
        return addHeight(list);
    }

    //
    public void updateRevokeTx(String ontid,
                               String txhash,
                               String revokeTx) throws Exception {
        //
        Example example = new Example(Attestation.class);
        //
        Example.Criteria c1 = example.createCriteria();
        c1.andCondition("txhash=", txhash);
        //
        example.and(c1);
        //
        Attestation c = new Attestation();
        c.setRevokeTx(revokeTx);
        attestationMapper.updateByExampleSelective(c, example);
    }


    //
    public Integer count(String ontid) throws Exception {
        //
        Example example = new Example(Attestation.class);
        //
        Example.Criteria c1 = example.createCriteria();
        c1.andCondition("dnaid=", ontid);
        c1.andCondition("status=", 0);
        //
        example.and(c1);
        //
        return attestationMapper.selectCountByExample(example);
    }

    // 跨表添加height信息
    private List<Attestation> addHeight(List<Attestation> list) {
        //
        if (list == null || list.size() == 0)
            return list;
        //
        List<Attestation> newlist = new ArrayList<>();
        for (Attestation c : list) {
            //
            Example example = new Example(Event.class);
            //
            Example.Criteria c1 = example.createCriteria();
            c1.andCondition("txhash=", c.getTxhash());
            //
            example.and(c1);
            //
            Event e = eventMapper.selectOneByExample(example);
            //
            if (e != null) {
                Integer height = e.getHeight();
                c.setHeight(height);
            } else {
                c.setHeight(0);  // TODO
            }
            newlist.add(c);
        }
        return newlist;
    }

    // 写入数据库
    public void saveToLocal(Attestation attestation) throws Exception {
        attestationMapper.insertSelective(attestation);
    }

    // 写入数据库，batch insert
    public void saveToLocalBatch(List<Attestation> attestationList) throws Exception {
        attestationMapper.insertBatch(attestationList);
    }

    //
    public void saveCompany(AttestationCompany company) {
        attestationCompanyMapper.insertSelective(company);
    }

    //
    public AttestationCompany getCompany(String ontid) {
        Example example = new Example(AttestationCompany.class);
        example.createCriteria().andCondition("ontid=", ontid);
        AttestationCompany company = attestationCompanyMapper.selectOneByExample(example);
        return company;
    }

    // 先查询是不是项目方，有没有设置指定的payer地址和合约地址
    public void checkCompany(String company_ontid) throws Exception {
        AttestationCompany attestationCompany = getCompany(company_ontid);
        if (attestationCompany == null) {
            // TODO
            throw new Exception("项目方地址列表中找不到该ontid..." + company_ontid);
        }
    }
}
