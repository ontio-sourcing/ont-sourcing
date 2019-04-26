package com.ontology.sourcing.controller;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import com.ontology.sourcing.dao.contract.Contract;
import com.ontology.sourcing.dao.contract.ContractCompany;
import com.ontology.sourcing.model.utils.Result;
import com.ontology.sourcing.service.ContractService;
import com.ontology.sourcing.service.oauth.OAuthService;
import com.ontology.sourcing.service.time.bctsp.TspService;
import com.ontology.sourcing.service.utils.SyncService;
import com.ontology.sourcing.service.utils.ValidateService;
import com.ontology.sourcing.utils.GlobalVariable;
import com.ontology.sourcing.utils.exp.ErrorCode;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;

@RestController
@RequestMapping("/api/v1/contract/")
public class ContractController {

    //
    private Logger logger = (Logger) LoggerFactory.getLogger(ContractController.class);
    private Gson gson = new Gson();

    //
    private TspService tspService;
    private SyncService syncService;
    private ValidateService validateService;
    //
    private OAuthService oauthService;
    private ContractService contractService;

    @Autowired
    public ContractController(TspService tspService, SyncService syncService, ValidateService validateService, OAuthService oauthService, ContractService contractService) {
        //
        this.tspService = tspService;
        this.syncService = syncService;
        this.validateService = validateService;
        //
        this.oauthService = oauthService;
        this.contractService = contractService;
    }

    //
    @PostConstruct
    public void postConstructor() {
        //
        logger.info("ContractController PostConstruct start ...");
    }


    @PostMapping("/put")
    public ResponseEntity<Result> putContract(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("putContract");

        //
        Set<String> required = new HashSet<>();
        required.add("access_token");
        required.add("user_ontid");  // 空表示自己上传；不空表示被别人上传
        required.add("filehash");
        required.add("detail");
        required.add("type");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            e.printStackTrace();
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String access_token = (String) obj.get("access_token");
        String user_ontid = (String) obj.get("user_ontid");
        String filehash = (String) obj.get("filehash");
        String detail = (String) obj.get("detail");
        String type = (String) obj.get("type");

        //
        String company_ontid = "";
        try {
            company_ontid = oauthService.getContentUser(access_token);
        } catch (Exception e) {
            e.printStackTrace();
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        if (StringUtils.isEmpty(user_ontid))
            user_ontid = company_ontid;

        //
        try {
            //
            Map<String, Object> map = tspService.getTimeStampMap(filehash);
            //
            long timestamp = (long) map.get("timestamp");
            String timestampSign = map.get("timestampSign").toString();
            //
            Contract contract = new Contract();
            contract.setOntid(user_ontid);
            contract.setCompanyOntid(company_ontid);
            contract.setFilehash(filehash);
            contract.setDetail(detail);
            contract.setType(type);
            contract.setTimestamp(new Date(timestamp * 1000L));
            contract.setTimestampSign(timestampSign);
            contract.setCreateTime(new Date());
            //
            Map<String, String> map2 = contractService.putContract(contract);
            String txhash = map2.get("txhash");
            contract.setTxhash(txhash);
            //
            contractService.saveToLocal(company_ontid, contract);
            // 链同步
            syncService.confirmTx(txhash);
            //
            rst.setResult(true);
            rst.setErrorAndDesc(ErrorCode.SUCCESSS);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }
    }

    @PostMapping("/put/batch")
    public ResponseEntity<Result> putContractBatch(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("putContractBatch");

        //
        Set<String> required = new HashSet<>();
        required.add("access_token");
        required.add("user_ontid");
        required.add("filelist");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            e.printStackTrace();
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String access_token = (String) obj.get("access_token");
        String user_ontid = (String) obj.get("user_ontid");

        //
        String company_ontid = "";
        try {
            company_ontid = oauthService.getContentUser(access_token);
        } catch (Exception e) {
            e.printStackTrace();
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        if (StringUtils.isEmpty(user_ontid))
            user_ontid = company_ontid;


        // TODO
        ArrayList<Map<String, Object>> filelist = (ArrayList<Map<String, Object>>) obj.get("filelist");

        //
        try {
            //
            List<Contract> contractList = new ArrayList<>();
            //
            for (Map<String, Object> item : filelist) {
                // TODO 稍微错开一点，0.1s
                Thread.sleep(100L);
                // TODO 参数检验
                String filehash = item.get("filehash").toString();
                String type = item.get("type").toString();
                ArrayList<Map<String, String>> detailList = (ArrayList<Map<String, String>>) item.get("detail");
                String detail = gson.toJson(detailList);
                System.out.println(detail);

                //
                Map<String, Object> map = tspService.getTimeStampMap(filehash);
                //
                long timestamp = (long) map.get("timestamp");
                String timestampSign = map.get("timestampSign").toString();
                //
                Contract contract = new Contract();
                contract.setCompanyOntid(company_ontid);
                contract.setOntid(user_ontid);
                contract.setFilehash(filehash);
                contract.setDetail(detail);
                contract.setType(type);
                contract.setTimestamp(new Date(timestamp * 1000L));
                contract.setTimestampSign(timestampSign);
                contract.setCreateTime(new Date());
                //
                Map<String, String> map2 = contractService.putContract(contract);
                String txhash = map2.get("txhash");
                contract.setTxhash(txhash);
                // 链同步
                syncService.confirmTx(txhash);
                //
                contractList.add(contract);
            }
            // mybatis batch insert
            contractService.saveToLocalBatch(company_ontid, contractList);
            //
            rst.setResult(true);
            rst.setErrorAndDesc(ErrorCode.SUCCESSS);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }
    }

    @PostMapping("/hash")
    public ResponseEntity<Result> selectByOntidAndHash(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("selectByOntidAndHash");

        //
        Set<String> required = new HashSet<>();
        required.add("hash");
        required.add("access_token");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            e.printStackTrace();
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String hash = (String) obj.get("hash");
        String accessToken = (String) obj.get("access_token");

        //
        String ontid = "";
        try {
            ontid = oauthService.getContentUser(accessToken);
        } catch (Exception e) {
            e.printStackTrace();
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        // ontid 也要作为条件，否则查到别人的了
        List<Contract> list = contractService.selectByOntidAndHash(ontid, hash);

        //
        rst.setResult(list);
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);
        return new ResponseEntity<>(rst, HttpStatus.OK);

    }

    @PostMapping("/history")
    public ResponseEntity<Result> getHistory(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("getHistory");

        //
        Set<String> required = new HashSet<>();
        required.add("access_token");
        required.add("pageNum");
        required.add("pageSize");
        if (obj.containsKey("type")) {
            required.add("type");
        }

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            e.printStackTrace();
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String accessToken = (String) obj.get("access_token");
        int pageNum = Integer.parseInt(obj.get("pageNum").toString());
        int pageSize = Integer.parseInt(obj.get("pageSize").toString());
        //
        String type = "";
        if (obj.containsKey("type")) {
            type = (String) obj.get("type");
        }

        //
        String ontid = "";
        try {
            ontid = oauthService.getContentUser(accessToken);
        } catch (Exception e) {
            e.printStackTrace();
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }
        //
        List<Contract> list = contractService.getHistoryByOntid(ontid, pageNum, pageSize, type);
        //
        rst.setResult(list);
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/count")
    public ResponseEntity<Result> count(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("count");

        //
        Set<String> required = new HashSet<>();
        required.add("access_token");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            e.printStackTrace();
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String accessToken = (String) obj.get("access_token");

        //
        String ontid = "";
        try {
            ontid = oauthService.getContentUser(accessToken);
        } catch (Exception e) {
            e.printStackTrace();
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }
        //
        Integer count = contractService.count(ontid);
        //
        rst.setResult(count);
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/explorer")
    public ResponseEntity<Result> getExplorer(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("getExplorer");

        //
        Set<String> required = new HashSet<>();
        required.add("pageNum");
        required.add("pageSize");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            e.printStackTrace();
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        int pageNum = Integer.parseInt(obj.get("pageNum").toString());
        int pageSize = Integer.parseInt(obj.get("pageSize").toString());

        //
        List<Contract> list = contractService.getExplorerHistory(GlobalVariable.CURRENT_CONTRACT_TABLE_NAME, pageNum, pageSize);
        //
        rst.setResult(list);
        //
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);

        //
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/explorer/hash")
    public ResponseEntity<Result> getExplorerHash(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("getExplorerHash");

        //
        Set<String> required = new HashSet<>();
        required.add("hash");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            e.printStackTrace();
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String hash = (String) obj.get("hash");

        // ontid 也要作为条件，否则查到别人的了
        List<Contract> list = contractService.selectByHash(hash);

        //
        rst.setResult(list);
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);
        return new ResponseEntity<>(rst, HttpStatus.OK);

    }


    @PostMapping("/company/add")
    public ResponseEntity<Result> addCompany(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("addCompany");

        //
        Set<String> required = new HashSet<>();
        required.add("ontid");
        required.add("prikey");
        required.add("code_addr");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            e.printStackTrace();
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String ontid = (String) obj.get("ontid");
        String prikey = (String) obj.get("prikey");
        String code_addr = (String) obj.get("code_addr");

        //
        ContractCompany existed = contractService.getCompany(ontid);
        if (existed != null) {
            rst.setErrorAndDesc(ErrorCode.ONTID_EXIST);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        ContractCompany company = new ContractCompany();
        company.setOntid(ontid);
        company.setPrikey(prikey);
        company.setCodeAddr(code_addr);
        company.setCreateTime(new Date());
        contractService.saveCompany(company);

        //
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);
        return new ResponseEntity<>(rst, HttpStatus.OK);

    }


    @PostMapping("/company/update")
    public ResponseEntity<Result> updateCompany(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("updateCompany");

        //
        Set<String> required = new HashSet<>();
        required.add("ontid");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            e.printStackTrace();
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String ontid = (String) obj.get("ontid");

        //
        String prikey = "";
        if (obj.containsKey("prikey")) {
            prikey = (String) obj.get("prikey");
        }
        String code_addr = "";
        if (obj.containsKey("code_addr")) {
            code_addr = (String) obj.get("code_addr");
        }

        //
        ContractCompany existed = contractService.getCompany(ontid);
        if (existed == null) {
            rst.setErrorAndDesc(ErrorCode.ONTID_NOT_EXIST);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        if (!StringUtils.isEmpty(prikey))
            existed.setPrikey(prikey);
        if (!StringUtils.isEmpty(code_addr))
            existed.setCodeAddr(code_addr);
        existed.setUpdateTime(new Date());

        //
        contractService.saveCompany(existed);

        //
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/company/get")
    public ResponseEntity<Result> getCompany(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("getCompany");

        //
        Set<String> required = new HashSet<>();
        required.add("ontid");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            e.printStackTrace();
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String ontid = (String) obj.get("ontid");

        //
        ContractCompany company = contractService.getCompany(ontid);

        //
        rst.setResult(company);
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }
}
