package com.ontology.sourcing.service;

import com.alibaba.fastjson.JSON;
import com.github.ontio.OntSdk;
import com.github.ontio.account.Account;
import com.github.ontio.common.Address;
import com.github.ontio.common.Helper;
import com.github.ontio.core.transaction.Transaction;
import com.github.ontio.sdk.exception.SDKException;
import com.github.ontio.smartcontract.neovm.abi.BuildParams;
import com.ontology.sourcing.dao.Event;
import com.ontology.sourcing.dao.contract.*;
import com.ontology.sourcing.mapper.EventMapper;
import com.ontology.sourcing.mapper.contract.*;
import com.ontology.sourcing.service.utils.PropertiesService;
import com.ontology.sourcing.utils.CryptoUtil;
import com.ontology.sourcing.utils.GlobalVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class ContractService {

    //
    private PropertiesService propertiesService;
    //
    private ContractMapper contractMapper;
    private ContractIndexMapper contractIndexMapper;
    private ContractOntidMapper contractOntidMapper;
    private EventMapper eventMapper;
    //
    private OntSdk ontSdk;
    private String codeAddr;

    @Autowired
    public ContractService(PropertiesService propertiesService,
                           ContractMapper contractMapper,
                           ContractIndexMapper contractIndexMapper,
                           ContractOntidMapper contractOntidMapper,
                           EventMapper eventMapper) {
        //
        this.propertiesService = propertiesService;
        //
        this.contractMapper = contractMapper;
        this.contractIndexMapper = contractIndexMapper;
        this.contractOntidMapper = contractOntidMapper;
        this.eventMapper = eventMapper;
        //
        ontSdk = GlobalVariable.getOntSdk(propertiesService.ontologyUrl, propertiesService.walletPath);
        // 合约哈希/合约地址/contract address
        codeAddr = Address.AddressFromVmCode(propertiesService.contractCode).toHexString(); // 6864a62235279e4c5c3fba004905f30e2157169a
    }

    public Map<String, String> putContract(Contract contract, Account payer) throws Exception {

        List paramList = new ArrayList<>();
        paramList.add("Put".getBytes());

        List args = new ArrayList();
        // key
        args.add(contractToDigestForKey(contract));  // 上链时，只把指定field的组合后的hash
        // value
        args.add(contractToDigestForValue(contract));  // 上链时，只把指定field的组合后的hash

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

    public String getContract(Contract contract, Account payer) throws Exception {

        List paramList = new ArrayList<>();
        paramList.add("Get".getBytes());

        List args = new ArrayList();
        args.add(contractToDigestForKey(contract));

        paramList.add(args);
        byte[] params = BuildParams.createCodeParamsScript(paramList);

        //
        Map<String, String> map = invokeContract(Helper.reverse(codeAddr), null, params, payer, ontSdk.DEFAULT_GAS_LIMIT, GlobalVariable.DEFAULT_GAS_PRICE, true);

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
    public Map<String, String> invokeContract(String codeAddr, String method, byte[] params, Account payerAcct, long gaslimit, long gasprice, boolean preExec) throws Exception {

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
        Transaction tx = ontSdk.vm().makeInvokeCodeTransaction(codeAddr, method, params, payerAcct.getAddressU160().toBase58(), gaslimit, gasprice);
        // System.out.println(tx);
        // com.github.ontio.core.payload.InvokeCode@fecc2faa

        //
        ontSdk.addSign(tx, payerAcct);

        //
        String rawdata = tx.toHexString();
        String txhash = tx.hash().toString();
        //
        map.put("txhash", txhash);
        map.put("rawdata", rawdata);  // TODO

        //
        if (preExec) {

            Object result = ontSdk.getConnect().sendRawTransactionPreExec(rawdata);
            map.put("result", result.toString());

        } else {

            boolean result = ontSdk.getConnect().sendRawTransaction(rawdata);
            map.put("result", Boolean.toString(result));
        }

        //
        return map;
    }

    // 发到链上的key
    public String contractToDigestForKey(Contract contract) {
        String k = contract.getOntid() + contract.getCompanyOntid() + contract.getFilehash() + contract.getTimestamp();
        return CryptoUtil.sha256(k);
    }

    // 发到链上的value
    public String contractToDigestForValue(Contract contract) {
        String v = contract.getOntid() + contract.getCompanyOntid() + contract.getDetail() + contract.getTimestamp() + contract.getTimestampSign();
        return CryptoUtil.sha256(v);
    }

    // 后期如果需要验证
    public boolean verifyContractOnBlockchain(Contract contract, Account payer) throws Exception {
        String valueLocal = contractToDigestForValue(contract);
        String valueOnBlockchain = getContract(contract, payer);
        return valueOnBlockchain.equals(valueLocal);
    }

    //
    public List<Contract> getHistoryByOntid(String ontid, int pageNum, int pageSize, String type) {
        //
        String tableName = getIndex(ontid).getName();
        //
        int start = (pageNum - 1) * pageSize;
        int offset = pageSize;
        //
        List<Contract> list;
        if (StringUtils.isEmpty(type)) {
            list = contractMapper.selectByOntidPageNumSize(tableName, ontid, start, offset);
        } else {
            list = contractMapper.selectByOntidPageNumSizeAndType(tableName, ontid, start, offset, type);
        }
        return addHeight(list);
    }

    //
    public List<Contract> getExplorerHistory(String tableName, int pageNum, int pageSize) {
        int start = (pageNum - 1) * pageSize;
        int offset = pageSize;
        List<Contract> list = contractMapper.selectByPageNumSize(tableName, start, offset);
        return addHeight(list);
    }

    //
    public List<Contract> selectByOntidAndHash(String ontid, String hash) {
        String tableName = getIndex(ontid).getName();
        List<Contract> list = contractMapper.selectByOntidAndHash(tableName, ontid, hash);
        return addHeight(list);
    }

    //
    public List<Contract> selectByHash(String hash) {

        // TODO 目前只支持从当前表查询
        List<Contract> list = contractMapper.selectByHash(GlobalVariable.CURRENT_CONTRACT_TABLE_NAME, hash);
        return addHeight(list);
    }

    //
    public Integer count(String ontid) {
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
            Integer height = e.getHeight();
            c.setHeight(height);
            newlist.add(c);
        }
        return newlist;
    }

    //
    public ContractOntid getRecord(String ontid) {
        //
        ContractOntid existed = contractOntidMapper.findByOntid(ontid);
        //
        if (existed != null) {
            return existed;
        }
        // 应该要写入该ontid对应的contract_index，而不是CURRENT_CONTRACT_TABLE，否则查询时就可能会跨表了
        ContractOntid record = new ContractOntid();
        record.setOntid(ontid);
        record.setCreateTime(new Date());
        record.setContractIndex(GlobalVariable.CURRENT_CONTRACT_TABLE_INDEX);
        contractOntidMapper.save(record);
        //
        return record;
    }

    private ContractIndex getIndex(String ontid) {
        ContractOntid record = getRecord(ontid);
        ContractIndex contractIndex = contractIndexMapper.selectByPrimaryKey(record.getContractIndex());
        return contractIndex;
    }

    // 写入数据库
    public void saveToLocal(String ontid, Contract contract) {
        contractMapper.insert(getIndex(ontid).getName(), contract);
    }

    // 写入数据库，batch insert
    public void saveToLocalBatch(String ontid, List<Contract> contractList) {
        contractMapper.insertBatch(getIndex(ontid).getName(), contractList);
    }
}
