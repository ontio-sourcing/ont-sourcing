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
import com.google.gson.Gson;
import com.ontology.sourcing.dao.Event;
import com.ontology.sourcing.dao.contract.*;
import com.ontology.sourcing.dao.util.Sensitive;
import com.ontology.sourcing.dao.util.SensitiveLog;
import com.ontology.sourcing.mapper.EventMapper;
import com.ontology.sourcing.mapper.contract.*;
import com.ontology.sourcing.mapper.util.SensitiveLogMapper;
import com.ontology.sourcing.mapper.util.SensitiveMapper;
import com.ontology.sourcing.service.util.ChainService;
import com.ontology.sourcing.service.util.PropertiesService;
import com.ontology.sourcing.util.GlobalVariable;
import com.ontology.sourcing.util._hash.Sha256Util;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Service
public class ContractService {

    //
    private Logger logger = (Logger) LoggerFactory.getLogger(ContractService.class);
    private Gson gson = new Gson();

    //
    private PropertiesService propertiesService;
    private ChainService chainService;
    //
    private ContractMapper contractMapper;
    private ContractIndexMapper contractIndexMapper;
    private ContractOntidMapper contractOntidMapper;
    private EventMapper eventMapper;
    private ContractCompanyMapper contractCompanyMapper;
    //
    private SensitiveMapper sensitiveMapper;
    private SensitiveLogMapper sensitiveLogMapper;

    // 公共payer和公共合约
    private String codeAddr;
    private Account payer;

    @Autowired
    public ContractService(PropertiesService propertiesService,
                           ChainService chainService,
                           ContractMapper contractMapper,
                           ContractIndexMapper contractIndexMapper,
                           ContractOntidMapper contractOntidMapper,
                           EventMapper eventMapper,
                           ContractCompanyMapper contractCompanyMapper,
                           SensitiveMapper sensitiveMapper,
                           SensitiveLogMapper sensitiveLogMapper) {
        //
        this.propertiesService = propertiesService;
        this.chainService = chainService;

        //
        this.contractMapper = contractMapper;
        this.contractIndexMapper = contractIndexMapper;
        this.contractOntidMapper = contractOntidMapper;
        this.eventMapper = eventMapper;
        this.contractCompanyMapper = contractCompanyMapper;
        //
        this.sensitiveMapper = sensitiveMapper;
        this.sensitiveLogMapper = sensitiveLogMapper;

        // 合约哈希/合约地址/contract codeAddr
        codeAddr = propertiesService.codeAddr;
        payer = GlobalVariable.getInstanceOfAccount(propertiesService.payerPrivateKey);
    }

    @PostConstruct
    public void whatever() {
        // 初始化敏感词
        GlobalVariable.sensitiveWords = getSensitives();
    }

    public Map<String, String> putContract(Contract contract) throws Exception {

        // 上链时，只把指定field的组合后的hash作为key
        String c_key = contractToDigestForKey(contract);
        // 上链时，只把指定field的组合后的hash作为value
        // String c_value = contractToDigestForValue(contract);
        String c_value = contract.getDetail();  // TODO 原文上链

        //
        return putContract(contract, c_key, c_value);
    }

    public Map<String, String> putContract(Contract contract,
                                           String c_key,
                                           String c_value) throws Exception {

        // 先查询是不是项目方，有没有设置指定的payer地址和合约地址
        String c_ontid = contract.getCompanyOntid();
        ContractCompany contractCompany = contractCompanyMapper.findByOntid(c_ontid);
        if (contractCompany != null) {
            payer = GlobalVariable.getInstanceOfAccount(contractCompany.getPrikey());
            codeAddr = contractCompany.getCodeAddr();
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

    public String getContract(Contract contract,
                              Account payer) throws Exception {

        List paramList = new ArrayList<>();
        paramList.add("getRecord".getBytes());

        List args = new ArrayList();
        args.add(contractToDigestForKey(contract));

        paramList.add(args);
        byte[] params = BuildParams.createCodeParamsScript(paramList);

        // 先查询是不是项目方，有没有设置指定的payer地址和合约地址
        ContractCompany contractCompany = contractCompanyMapper.findByOntid(contract.getCompanyOntid());
        if (contractCompany != null) {
            payer = GlobalVariable.getInstanceOfAccount(contractCompany.getPrikey());
            codeAddr = Address.AddressFromVmCode(contractCompany.getCodeAddr()).toHexString();
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
    public String contractToDigestForKey(Contract contract) {
        String k = contract.getOntid() + contract.getCompanyOntid() + contract.getFilehash() + contract.getTimestamp();
        return Sha256Util.sha256(k);
    }

    // 发到链上的value
    public String contractToDigestForValue(Contract contract) {
        String v = contract.getOntid() + contract.getCompanyOntid() + contract.getDetail() + contract.getTimestamp() + contract.getTimestampSign();
        return Sha256Util.sha256(v);
    }

    // 后期如果需要验证
    public boolean verifyContractOnBlockchain(Contract contract,
                                              Account payer) throws Exception {
        String valueLocal = contractToDigestForValue(contract);
        String valueOnBlockchain = getContract(contract, payer);
        return valueOnBlockchain.equals(valueLocal);
    }

    //
    public List<Contract> getHistoryByOntid(String ontid,
                                            int pageNum,
                                            int pageSize,
                                            String type) throws Exception {
        //
        String tableName = getIndex(ontid).getName();
        //
        int start = (pageNum - 1) * pageSize;
        int offset = pageSize;
        //
        List<Contract> list;
        if (StringUtils.isEmpty(type)) {
            list = contractMapper.selectByOntidAndPage(tableName, ontid, start, offset);
        } else {
            list = contractMapper.selectByOntidAndPageAndType(tableName, ontid, start, offset, type);
        }
        return addHeight(list);
    }

    //
    public List<Contract> getExplorerHistory(String tableName,
                                             int pageNum,
                                             int pageSize) {
        int start = (pageNum - 1) * pageSize;
        int offset = pageSize;
        List<Contract> list = contractMapper.selectByPage(tableName, start, offset);
        return addHeight(list);
    }

    //
    public List<Contract> selectByOntidAndTxHash(String ontid,
                                                 String txhash) throws Exception {
        String tableName = getIndex(ontid).getName();
        List<Contract> list = contractMapper.selectByOntidAndTxHash(tableName, ontid, txhash);
        return addHeight(list);
    }

    //
    public List<Contract> selectByOntidAndHash(String ontid,
                                               String hash) throws Exception {
        String tableName = getIndex(ontid).getName();
        List<Contract> list = contractMapper.selectByOntidAndHash(tableName, ontid, hash);
        return addHeight(list);
    }

    //
    public int updateStatusByOntidAndHash(String ontid,
                                          String hash) throws Exception {
        String tableName = getIndex(ontid).getName();
        return contractMapper.updateStatusByOntidAndHash(tableName, ontid, hash);
    }

    //
    public List<Contract> selectByHash(String hash) {

        // TODO 目前只支持从当前表查询
        List<Contract> list = contractMapper.selectByHash(GlobalVariable.CURRENT_CONTRACT_TABLE_NAME, hash);
        return addHeight(list);
    }

    //
    public int updateRevokeTx(String ontid,
                              String txhash,
                              String revokeTx) throws Exception {
        String tableName = getIndex(ontid).getName();
        return contractMapper.updateRevokeTx(tableName, txhash, revokeTx, new Date());
    }


    //
    public Integer count(String ontid) throws Exception {
        String tableName = getIndex(ontid).getName();
        return contractMapper.count(tableName, ontid);
    }

    // 跨表添加height信息
    private List<Contract> addHeight(List<Contract> list) {
        //
        if (list == null || list.size() == 0)
            return list;
        //
        List<Contract> newlist = new ArrayList<>();
        for (Contract c : list) {
            Event e = eventMapper.findByTxhash(c.getTxhash());
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

    // TODO @Transactional  ??
    private ContractOntid getRecord(String ontid) {

        //
        ContractOntid existed = contractOntidMapper.findFirstByOntidOrderByCreateTimeAsc(ontid);

        //
        if (existed != null) {
            return existed;
        }
        //
        ContractOntid record = new ContractOntid();
        record.setOntid(ontid);
        record.setCreateTime(new Date());
        record.setContractIndex(GlobalVariable.CURRENT_CONTRACT_TABLE_INDEX);

        // TODO 这里存在一个很大bug，若同一时间一个ontid发来多个请求，在数据库记录任何一个之前，都会返回null，于是后面都会创建并写入数据库，同一个ontid就会有多条记录
        //        ContractOntid existed = contractOntidMapper.findByOntid(ontid);
        // contractOntidMapper.save(record);
        contractOntidMapper.saveIfIgnore(ontid, record.getContractIndex(), record.getCreateTime());
        //
        return record;
    }

    // private ContractIndex getIndex(String ontid) throws Exception {
    //     ContractOntid record = getRecord(ontid);
    //     // ContractIndex contractIndex = contractIndexMapper.findById(record.getContractIndex());
    //
    //     Optional<ContractIndex> ci_opt = contractIndexMapper.findById(record.getContractIndex());
    //     if (ci_opt.isPresent()) {
    //         ContractIndex ci = ci_opt.get();
    //         return ci;
    //     } else {
    //         throw new Exception("can not find index for ontid:" + ontid);
    //     }
    // }
    private ContractIndex getIndex(String ontid) {
        ContractOntid record = getRecord(ontid);
        ContractIndex contractIndex = contractIndexMapper.selectByPrimaryKey(record.getContractIndex());
        return contractIndex;
    }

    // 写入数据库
    public void saveToLocal(String ontid,
                            Contract contract) throws Exception {
        contractMapper.insert(getIndex(ontid).getName(), contract);
    }

    // 写入数据库，batch insert
    public void saveToLocalBatch(String ontid,
                                 List<Contract> contractList) throws Exception {
        contractMapper.insertBatch(getIndex(ontid).getName(), contractList);
    }

    //
    public void saveCompany(ContractCompany company) {
        contractCompanyMapper.save(company);
    }

    //
    public ContractCompany getCompany(String ontid) {
        ContractCompany company = contractCompanyMapper.findByOntid(ontid);
        return company;
    }

    // 先查询是不是项目方，有没有设置指定的payer地址和合约地址
    public void checkCompany(String company_ontid) throws Exception {
        ContractCompany contractCompany = getCompany(company_ontid);
        if (contractCompany == null) {
            // TODO
            throw new Exception("项目方地址列表中找不到该ontid..." + company_ontid);
        }
    }

    //
    public int addSensitive(String word) {
        //
        Sensitive s = new Sensitive();
        s.setWord(word);
        s.setCreateTime(new Date());
        s = sensitiveMapper.save(s);
        //
        GlobalVariable.sensitiveWords.add(word);
        //
        return s.getId();
    }

    public List<String> getSensitives() {
        List<String> l = new ArrayList<>();
        List<Sensitive> ls = sensitiveMapper.findAll();
        for (Sensitive s : ls) {
            l.add(s.getWord());
        }
        return l;
    }

    public int addSensitiveLog(String ontid,
                               List<String> words) {
        //
        SensitiveLog sl = new SensitiveLog();
        sl.setOntid(ontid);
        sl.setWords(gson.toJson(words));
        sl.setCreateTime(new Date());
        sl = sensitiveLogMapper.save(sl);
        //
        return sl.getId();
    }

    public List<SensitiveLog> getSensitiveLog(String ontid) {
        // return sensitiveLogMapper.findAllByOntid(ontid);
        SensitiveLog slog = new SensitiveLog();
        slog.setOntid(ontid);
        Example<SensitiveLog> employeeExample = Example.of(slog);
        //calling QueryByExampleExecutor#findAll(Example)
        Iterable<SensitiveLog> employees = sensitiveLogMapper.findAll(employeeExample);
        //
        List<SensitiveLog> sl = new ArrayList<>();
        for (SensitiveLog e : employees) {
            // System.out.println(e);
            sl.add(e);
        }
        //
        return sl;
    }

    public void hasSensitives(String ontid,
                              String contextStr) throws Exception {
        // 分词，英文
        List<String> wlist = new ArrayList<>();
        StringTokenizer multiTokenizer = new StringTokenizer(contextStr, ":：//,，.。-()（）[]{}、\"");
        while (multiTokenizer.hasMoreTokens()) {
            wlist.add(multiTokenizer.nextToken());
        }
        //
        List<String> slist = new ArrayList<>();
        //
        for (String w : GlobalVariable.sensitiveWords) {
            //
            if (wlist.indexOf(w) != -1) {
                slist.add(w);
            }
        }
        // 中文
        if (isChinese(contextStr)){
            //
            for (String w : GlobalVariable.sensitiveWords) {
                //
                if (contextStr.contains(w)) {
                    slist.add(w);
                }
            }
        }

        //
        if (slist.size() != 0) {
            //
            SensitiveLog slog = new SensitiveLog();
            slog.setOntid(ontid);
            slog.setWords(gson.toJson(slist));
            slog.setCreateTime(new Date());
            sensitiveLogMapper.save(slog);
            //
            throw new Exception("含有敏感词:" + slist);
        }

    }



    // 判断一个字符是否是中文
    public static boolean isChinese(char c) {
        return c >= 0x4E00 &&  c <= 0x9FA5;// 根据字节码判断
    }
    // 判断一个字符串是否含有中文
    public static boolean isChinese(String str) {
        if (str == null) return false;
        for (char c : str.toCharArray()) {
            if (isChinese(c)) return true;// 有一个中文字符就返回
        }
        return false;
    }
}
