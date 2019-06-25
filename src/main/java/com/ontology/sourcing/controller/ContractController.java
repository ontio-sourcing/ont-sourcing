package com.ontology.sourcing.controller;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import com.ontology.sourcing.dao.contract.Contract;
import com.ontology.sourcing.dao.contract.ContractCompany;
import com.ontology.sourcing.model.contract.input.InputWrapper;
import com.ontology.sourcing.model.util.Result;
import com.ontology.sourcing.service.ContractService;
import com.ontology.sourcing.service.oauth.JWTService;
import com.ontology.sourcing.service.ontid_server.OntidServerService;
import com.ontology.sourcing.service.time.bctsp.TspService;
import com.ontology.sourcing.service.util.SyncService;
import com.ontology.sourcing.service.util.ValidateService;
import com.ontology.sourcing.util.GlobalVariable;
import com.ontology.sourcing.util.exp.ErrorCode;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;

@RestController
@RequestMapping("/api/v1/")
@Configuration
public class ContractController {

    //
    private Logger logger = (Logger) LoggerFactory.getLogger(ContractController.class);

    //
    private Gson gson = new Gson();

    //
    private TspService tspService;
    private SyncService syncService;
    private ValidateService validateService;

    //
    private JWTService jwtService;
    private ContractService contractService;
    private OntidServerService ontidServerService;

    //
    private final KafkaTemplate<String, Object> kafkaTemplate;

    //
    private final String ontSourcingTopicPut;

    @Autowired
    public ContractController(TspService tspService,
                              SyncService syncService,
                              ValidateService validateService,
                              JWTService jwtService,
                              ContractService contractService,
                              OntidServerService ontidServerService,
                              KafkaTemplate<String, Object> kafkaTemplate,
                              @Value("${com.ontology.sourcing.kafka.topic.put}") String ontSourcingTopicPut) {
        //
        this.tspService = tspService;
        this.syncService = syncService;
        this.validateService = validateService;
        //
        this.jwtService = jwtService;
        this.contractService = contractService;
        this.ontidServerService = ontidServerService;
        //
        this.kafkaTemplate = kafkaTemplate;
        //
        this.ontSourcingTopicPut = ontSourcingTopicPut;
    }

    //
    @PostConstruct
    public void postConstructor() {
        //
        logger.info("ContractController PostConstruct start ...");
    }

    @PostMapping("/token/check")
    public ResponseEntity<Result> checkToken(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("checkToken");

        //
        Set<String> required = new HashSet<>();
        required.add("access_token");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String access_token = (String) obj.get("access_token");
        String ontid = "";
        try {
            ontid = jwtService.getContentUser(access_token);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        ContractCompany cc = contractService.getCompany(ontid);
        if (cc == null) {
            rst.setResult("2c");
        } else {
            rst.setResult("2b");
        }

        //
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    /*
    @KafkaListener(topics = {GlobalVariable.myTopic1})
    public void kafkaListener01(ConsumerRecord<String, String> record, Acknowledgment ack) {
        //
        Contract contract = gson.fromJson(record.value(), Contract.class);

        //
        String filehash = contract.getFilehash();
        String company_ontid = contract.getCompanyOntid();

        //
        logger.debug("start processing filehash {} from mq ...", filehash);

        try {
            //
            Map<String, Object> map = tspService.getTimeStampMap(filehash);
            //
            long timestamp = (long) map.get("timestamp");
            String timestampSign = map.get("timestampSign").toString();
            //
            contract.setTimestamp(new Date(timestamp * 1000L));
            contract.setTimestampSign(timestampSign);
            //
            Map<String, String> map2 = contractService.putAttestation(contract);
            String txhash = map2.get("txhash");
            contract.setTxhash(txhash);
            //
            contractService.saveToLocal(company_ontid, contract); // TODO 批量入库
            // 链同步
            syncService.confirmTx(txhash);
            //
            logger.debug("finish processing filehash {}.", filehash);
        } catch (Exception e) {
            // logger.error(e.getMessage());
            logger.error(e.getMessage());
        }

        //
        ack.acknowledge();
    }
    */

    @KafkaListener(topics = "#{'${com.ontology.sourcing.kafka.topic.put}'}")
    public void kafkaListener01(List<String> list,
                                Acknowledgment ack) {

        //
        logger.info("start fetching {} from mq ...", list.size());
        if (list.size() <= 0)
            return;

        //
        List<Contract> c_list = new ArrayList<>();
        List<Contract> other_list = new ArrayList<>();

        //
        String first = list.get(0);
        Contract first_contract = gson.fromJson(first, Contract.class);
        String first_ontid = first_contract.getCompanyOntid();


        //
        for (String message : list) {

            //
            Contract contract = gson.fromJson(message, Contract.class);

            //
            String filehash = contract.getFilehash();
            String company_ontid = contract.getCompanyOntid();

            //
            logger.debug("start processing filehash {} from mq ...", filehash);

            //
            try {
                //
                Map<String, Object> map = tspService.getTimeStampMap(filehash);
                //
                long timestamp = (long) map.get("timestamp");
                String timestampSign = map.get("timestampSign").toString();
                //
                contract.setTimestamp(new Date(timestamp * 1000L));
                contract.setTimestampSign(timestampSign);
                //
                Map<String, String> map2 = contractService.putContract(contract);
                String txhash = map2.get("txhash");
                contract.setTxhash(txhash);
                //
                if (contract.getCompanyOntid().equals(first_ontid)) {
                    c_list.add(contract);
                } else {
                    other_list.add(contract);
                }
                // 链同步
                syncService.confirmTx(txhash);
            } catch (Exception e) {
                // logger.error(e.getMessage());
                logger.error(e.getMessage());
                // TODO
                return;
            }

            //
            logger.debug("finish processing contract {},{}.", company_ontid, filehash);
        }

        //
        try {
            contractService.saveToLocalBatch(first_ontid, c_list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return;
        }

        //
        for (Contract c : other_list) {
            try {
                contractService.saveToLocal(c.getCompanyOntid(), c);
            } catch (Exception e) {
                logger.error(e.getMessage());
                return;
            }
        }

        //
        ack.acknowledge();
    }


    @PostMapping("/attestation/put")
    public ResponseEntity<Result> putAttestation(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("putAttestation");

        //
        Set<String> required = new HashSet<>();
        required.add("access_token");
        required.add("user_ontid");  // 空表示自己上传；不空表示被别人上传
        required.add("filehash");
        required.add("type");
        required.add("metadata");
        required.add("context");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        InputWrapper iw = gson.fromJson(gson.toJson(obj), InputWrapper.class);

        //
        String access_token = iw.getAccessToken();
        String user_ontid = iw.getUserOntid();
        String filehash = iw.getFilehash();
        String type = iw.getType();

        //
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        jsonObject.put("metadata", iw.getMetadata());
        jsonObject.put("context", iw.getContext());
        jsonObject.put("signature", iw.getSignature());
        String detail = jsonObject.toJSONString();

        //
        boolean async = false;
        if (obj.containsKey("async")) {
            async = (boolean) obj.get("async");
        }

        //
        String company_ontid = "";
        try {
            company_ontid = jwtService.getContentUser(access_token);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        // 查看项目有没有添加定制信息
        try {
            contractService.checkCompany(company_ontid);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        if (StringUtils.isEmpty(user_ontid))
            user_ontid = company_ontid;

        //
        try {
            //
            Contract contract = new Contract();
            contract.setOntid(user_ontid);
            contract.setCompanyOntid(company_ontid);
            contract.setFilehash(filehash);
            contract.setDetail(detail);
            contract.setType(type);
            contract.setCreateTime(new Date());
            //
            if (async) {
                //
                kafkaTemplate.send(ontSourcingTopicPut, gson.toJson(contract, Contract.class));
                //
                rst.setResult(true);
                rst.setErrorAndDesc(ErrorCode.SUCCESSS);
                return new ResponseEntity<>(rst, HttpStatus.OK);

            } else {
                //
                Map<String, Object> map = tspService.getTimeStampMap(filehash);
                //
                long timestamp = (long) map.get("timestamp");
                String timestampSign = map.get("timestampSign").toString();
                //
                contract.setTimestamp(new Date(timestamp * 1000L));
                contract.setTimestampSign(timestampSign);
                //
                Map<String, String> map2 = contractService.putContract(contract);
                String txhash = map2.get("txhash");
                contract.setTxhash(txhash);
                //
                contractService.saveToLocal(company_ontid, contract);
                // 链同步
                syncService.confirmTx(txhash);
                //
                Map<String, String> m = new HashMap<>();
                m.put("txhash", txhash);
                //
                rst.setResult(m);
                rst.setErrorAndDesc(ErrorCode.SUCCESSS);
                return new ResponseEntity<>(rst, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }
    }

    @PostMapping("/attestations/put")
    public ResponseEntity<Result> putAttestations(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("putAttestations");

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
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String access_token = (String) obj.get("access_token");
        String user_ontid = (String) obj.get("user_ontid");

        //
        boolean async = false;
        if (obj.containsKey("async")) {
            async = (boolean) obj.get("async");
        }

        //
        String company_ontid = "";
        try {
            company_ontid = jwtService.getContentUser(access_token);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        // 查看项目有没有添加定制信息
        try {
            contractService.checkCompany(company_ontid);
        } catch (Exception e) {
            logger.error(e.getMessage());
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
            List<Map<String, String>> txhashList = new ArrayList<>();
            //
            for (Map<String, Object> item : filelist) {

                //
                InputWrapper iw = gson.fromJson(gson.toJson(item), InputWrapper.class);

                //
                String filehash = iw.getFilehash();
                String type = iw.getType();
                //
                com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
                jsonObject.put("metadata", iw.getMetadata());
                jsonObject.put("context", iw.getContext());
                jsonObject.put("signature", iw.getSignature());
                String detail = jsonObject.toJSONString();

                //
                // ArrayList<Object> detailList = (ArrayList<Object>) item.get("detail");
                // String detail = gson.toJson(detailList);
                // System.out.println(detail);

                //
                Contract contract = new Contract();
                contract.setCompanyOntid(company_ontid);
                contract.setOntid(user_ontid);
                contract.setFilehash(filehash);
                contract.setDetail(detail);
                contract.setType(type);
                contract.setCreateTime(new Date());

                //
                if (async) {
                    //
                    kafkaTemplate.send(ontSourcingTopicPut, gson.toJson(contract, Contract.class));

                } else {
                    try {

                        //
                        Map<String, Object> map = tspService.getTimeStampMap(filehash);
                        //
                        long timestamp = (long) map.get("timestamp");
                        String timestampSign = map.get("timestampSign").toString();
                        //
                        contract.setTimestamp(new Date(timestamp * 1000L));
                        contract.setTimestampSign(timestampSign);
                        //
                        Map<String, String> map2 = contractService.putContract(contract);
                        String txhash = map2.get("txhash");
                        contract.setTxhash(txhash);
                        // 链同步
                        syncService.confirmTx(txhash);
                        //
                        contractList.add(contract);
                        //
                        Map<String, String> m = new HashMap<>();
                        m.put("txhash", txhash);
                        txhashList.add(m);

                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        rst.setErrorAndDesc(e);
                        return new ResponseEntity<>(rst, HttpStatus.OK);
                    }
                }
            }

            if (async) {

                //
                rst.setResult(true);
                rst.setErrorAndDesc(ErrorCode.SUCCESSS);
                return new ResponseEntity<>(rst, HttpStatus.OK);

            } else {
                // mybatis batch insert
                // 单条长度大概 4KB，30条，一个sql语句size大概为120KB
                // TODO 检查 max_allowed_packet
                contractService.saveToLocalBatch(company_ontid, contractList);
                //
                rst.setResult(txhashList);
                rst.setErrorAndDesc(ErrorCode.SUCCESSS);
                return new ResponseEntity<>(rst, HttpStatus.OK);
            }


        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }
    }

    // 点晴定制
    @PostMapping("/attestations/put/custom")
    public ResponseEntity<Result> putAttestationsCustom(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("putAttestationsCustom");

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
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String access_token = (String) obj.get("access_token");
        String user_ontid = (String) obj.get("user_ontid");

        //
        String company_ontid = "";
        try {
            company_ontid = jwtService.getContentUser(access_token);
        } catch (Exception e) {
            logger.error(e.getMessage());
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
                //
                String filehash = item.get("filehash").toString();
                String type = item.get("type").toString();
                ArrayList<Map<String, Object>> detailList = (ArrayList<Map<String, Object>>) item.get("detail");
                String detail = gson.toJson(detailList);
                // System.out.println(detail);

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
            // 单条长度大概 4KB，30条，一个sql语句size大概为120KB
            // TODO 检查 max_allowed_packet
            contractService.saveToLocalBatch(company_ontid, contractList);
            //
            rst.setResult(true);
            rst.setErrorAndDesc(ErrorCode.SUCCESSS);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }
    }

    @PostMapping("/contract/hash")
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
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String hash = (String) obj.get("hash");
        String accessToken = (String) obj.get("access_token");

        //
        String ontid = "";
        List<Contract> list = new ArrayList<>();

        //
        try {
            ontid = jwtService.getContentUser(accessToken);

            // ontid 也要作为条件，否则查到别人的了
            list = contractService.selectByOntidAndHash(ontid, hash);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        rst.setResult(list);
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);
        return new ResponseEntity<>(rst, HttpStatus.OK);

    }

    @PostMapping("/contract/hash/delete")
    public ResponseEntity<Result> deleteByOntidAndHash(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("deleteByOntidAndHash");

        //
        Set<String> required = new HashSet<>();
        required.add("hash");
        required.add("access_token");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String txhash = (String) obj.get("hash");
        String accessToken = (String) obj.get("access_token");

        //
        String ontid = "";
        try {
            ontid = jwtService.getContentUser(accessToken);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        // ontid 也要作为条件
        try {
            //
            List<Contract> exists = contractService.selectByOntidAndTxHash(ontid, txhash);
            if (exists.size() != 0) {

                //
                int rt = contractService.updateStatusByOntidAndHash(ontid, txhash);
                System.out.println(rt);

                //
                Contract exist = exists.get(0);
                String c_key = "revoke" + contractService.contractToDigestForKey(exist);
                String c_value = "";
                Map<String, String> map2 = contractService.putContract(exist, c_key, c_value);
                String revokeTx = map2.get("txhash");

                //
                contractService.updateRevokeTx(ontid, txhash, revokeTx);

                // todo 保证一定从链上取到
                syncService.confirmTx(revokeTx);

            } else {
                //
                rst.setResult(true);
                rst.setErrorAndDesc(ErrorCode.TXHASH_NOT_EXIST);
                return new ResponseEntity<>(rst, HttpStatus.OK);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        rst.setResult(true);
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);
        return new ResponseEntity<>(rst, HttpStatus.OK);

    }

    @PostMapping("/contract/history")
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
            logger.error(e.getMessage());
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
        List<Contract> list = new ArrayList<>();
        try {
            //
            ontid = jwtService.getContentUser(accessToken);
            //
            list = contractService.getHistoryByOntid(ontid, pageNum, pageSize, type);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        rst.setResult(list);
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/contract/count")
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
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String accessToken = (String) obj.get("access_token");

        //
        String ontid = "";
        Integer count;
        try {
            ontid = jwtService.getContentUser(accessToken);
            count = contractService.count(ontid);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        rst.setResult(count);
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/contract/explorer")
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
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        int pageNum = Integer.parseInt(obj.get("pageNum").toString());
        int pageSize = Integer.parseInt(obj.get("pageSize").toString());

        //
        List<Contract> list = new ArrayList<>();
        if (!StringUtils.isEmpty(GlobalVariable.CURRENT_CONTRACT_TABLE_NAME)) {
            list = contractService.getExplorerHistory(GlobalVariable.CURRENT_CONTRACT_TABLE_NAME, pageNum, pageSize);
        }

        //
        rst.setResult(list);
        //
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);

        //
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/contract/explorer/hash")
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
            logger.error(e.getMessage());
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


    @PostMapping("/contract/company/add")
    public ResponseEntity<Result> addCompany(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("addCompany");

        //
        Set<String> required = new HashSet<>();
        required.add("ontid");
        required.add("prikey");
        required.add("code_addr");
        //
        required.add("ont_password");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
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


    @PostMapping("/contract/company/update")
    public ResponseEntity<Result> updateCompany(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("updateCompany");

        //
        Set<String> required = new HashSet<>();
        required.add("ontid");
        //
        required.add("ont_password");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
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

    @PostMapping("/contract/company/get")
    public ResponseEntity<Result> getCompany(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("getCompany");

        //
        Set<String> required = new HashSet<>();
        required.add("ontid");
        //
        required.add("ont_password");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
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

    @PostMapping("/ontid/create")
    public ResponseEntity<Result> createOntid(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("createOntid");

        //
        Set<String> required = new HashSet<>();
        required.add("user_phone");
        // required.add("password");

        //
        try {
            validateService.validateParamsKeys(obj, required);
            validateService.validateParamsValues(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        String user_phone = (String) obj.get("user_phone");
        // String password = (String) obj.get("password");

        //
        try {
            // String ontid = ontidServerService.registerPhoneWithoutCode(user_phone, password);
            String ontid = ontidServerService.registerPhoneWithoutCode(user_phone);
            //
            Map<String, String> m = new HashMap<>();
            m.put("user_ontid", ontid);
            //
            rst.setResult(m);
            rst.setErrorAndDesc(ErrorCode.SUCCESSS);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }
    }
}
