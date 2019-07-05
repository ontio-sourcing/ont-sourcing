package com.ontology.sourcing.controller;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import com.ontology.sourcing.exception.ErrorInfo;
import com.ontology.sourcing.model.common.attestation.Attestation;
import com.ontology.sourcing.model.dao.contract.ContractCompany;
import com.ontology.sourcing.model.dto.ResponseBean;
import com.ontology.sourcing.model.dto.attestation.input.InputWrapper;
import com.ontology.sourcing.service.ContractService;
import com.ontology.sourcing.service.oauth.JWTService;
import com.ontology.sourcing.service.ontid_server.OntidServerService;
import com.ontology.sourcing.service.time.bctsp.TspService;
import com.ontology.sourcing.service.util.SyncService;
import com.ontology.sourcing.service.util.ValidateService;
import com.ontology.sourcing.util.GlobalVariable;
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
public class AttestationController {

    //
    private Logger logger = (Logger) LoggerFactory.getLogger(AttestationController.class);

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
    public AttestationController(TspService tspService,
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
    public ResponseEntity<ResponseBean> checkToken(@RequestBody LinkedHashMap<String, Object> obj) throws Exception {

        //
        ResponseBean rst = new ResponseBean();

        //
        Set<String> required = new HashSet<>();
        required.add("access_token");

        //
        validateService.validateParamsKeys(obj, required);
        validateService.validateParamsValues(obj);

        //
        String access_token = (String) obj.get("access_token");
        String ontid = jwtService.getContentUser(access_token);

        //
        ContractCompany cc = contractService.getCompany(ontid);
        if (cc == null) {
            rst.setResult("2c");
        } else {
            rst.setResult("2b");
        }
        rst.setCode(ErrorInfo.SUCCESS.code());
        rst.setMsg(ErrorInfo.SUCCESS.desc());

        //
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
        List<Attestation> c_list = new ArrayList<>();
        List<Attestation> other_list = new ArrayList<>();

        //
        String first = list.get(0);
        Attestation first_attestation = gson.fromJson(first, Attestation.class);
        String first_ontid = first_attestation.getCompanyOntid();


        //
        for (String message : list) {

            //
            Attestation attestation = gson.fromJson(message, Attestation.class);

            //
            String filehash = attestation.getFilehash();
            String company_ontid = attestation.getCompanyOntid();

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
                attestation.setTimestamp(new Date(timestamp * 1000L));
                attestation.setTimestampSign(timestampSign);
                //
                Map<String, String> map2 = contractService.putContract(attestation);
                String txhash = map2.get("txhash");
                attestation.setTxhash(txhash);
                //
                if (attestation.getCompanyOntid().equals(first_ontid)) {
                    c_list.add(attestation);
                } else {
                    other_list.add(attestation);
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
        for (Attestation c : other_list) {
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
    public ResponseEntity<ResponseBean> putAttestation(@RequestBody LinkedHashMap<String, Object> obj) throws Exception {

        //
        ResponseBean rst = new ResponseBean();
        //
        Set<String> required = new HashSet<>();
        required.add("access_token");
        required.add("user_ontid");  // 空表示自己上传；不空表示被别人上传
        required.add("filehash");
        required.add("type");
        required.add("metadata");
        required.add("context");

        //
        validateService.validateParamsKeys(obj, required);
        validateService.validateParamsValues(obj);

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
        String company_ontid = jwtService.getContentUser(access_token);

        // 查看项目有没有添加定制信息
        contractService.checkCompany(company_ontid);

        //
        if (StringUtils.isEmpty(user_ontid))
            user_ontid = company_ontid;

        //
        Attestation attestation = new Attestation();
        attestation.setOntid(user_ontid);
        attestation.setCompanyOntid(company_ontid);
        attestation.setFilehash(filehash);
        attestation.setDetail(detail);
        attestation.setType(type);
        attestation.setCreateTime(new Date());

        //
        if (async) {

            //
            kafkaTemplate.send(ontSourcingTopicPut, gson.toJson(attestation, Attestation.class));
            //
            rst.setResult(true);
            rst.setCode(ErrorInfo.SUCCESS.code());
            rst.setMsg(ErrorInfo.SUCCESS.desc());

            //
            return new ResponseEntity<>(rst, HttpStatus.OK);

        } else {

            //
            Map<String, Object> map = tspService.getTimeStampMap(filehash);
            //
            long timestamp = (long) map.get("timestamp");
            String timestampSign = map.get("timestampSign").toString();
            //
            attestation.setTimestamp(new Date(timestamp * 1000L));
            attestation.setTimestampSign(timestampSign);
            //
            Map<String, String> map2 = contractService.putContract(attestation);
            String txhash = map2.get("txhash");
            attestation.setTxhash(txhash);
            //
            contractService.saveToLocal(company_ontid, attestation);
            // 链同步
            syncService.confirmTx(txhash);
            //
            Map<String, String> m = new HashMap<>();
            m.put("txhash", txhash);
            //
            rst.setResult(m);
            rst.setCode(ErrorInfo.SUCCESS.code());
            rst.setMsg(ErrorInfo.SUCCESS.desc());
            //
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }
    }

    @PostMapping("/attestations/put")
    public ResponseEntity<ResponseBean> putAttestations(@RequestBody LinkedHashMap<String, Object> obj) throws Exception {

        //
        ResponseBean rst = new ResponseBean();

        //
        Set<String> required = new HashSet<>();
        required.add("access_token");
        required.add("user_ontid");
        required.add("filelist");

        //
        validateService.validateParamsKeys(obj, required);
        validateService.validateParamsValues(obj);

        //
        String access_token = (String) obj.get("access_token");
        String user_ontid = (String) obj.get("user_ontid");

        //
        boolean async = false;
        if (obj.containsKey("async")) {
            async = (boolean) obj.get("async");
        }

        //
        String company_ontid = jwtService.getContentUser(access_token);


        // 查看项目有没有添加定制信息
        contractService.checkCompany(company_ontid);

        //
        if (StringUtils.isEmpty(user_ontid)) {
            user_ontid = company_ontid;
        }

        // TODO
        ArrayList<Map<String, Object>> filelist = (ArrayList<Map<String, Object>>) obj.get("filelist");

        //
        List<Attestation> attestationList = new ArrayList<>();
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
            Attestation attestation = new Attestation();
            attestation.setCompanyOntid(company_ontid);
            attestation.setOntid(user_ontid);
            attestation.setFilehash(filehash);
            attestation.setDetail(detail);
            attestation.setType(type);
            attestation.setCreateTime(new Date());

            //
            if (async) {
                //
                kafkaTemplate.send(ontSourcingTopicPut, gson.toJson(attestation, Attestation.class));
            } else {
                //
                Map<String, Object> map = tspService.getTimeStampMap(filehash);
                //
                long timestamp = (long) map.get("timestamp");
                String timestampSign = map.get("timestampSign").toString();
                //
                attestation.setTimestamp(new Date(timestamp * 1000L));
                attestation.setTimestampSign(timestampSign);
                //
                Map<String, String> map2 = contractService.putContract(attestation);
                String txhash = map2.get("txhash");
                attestation.setTxhash(txhash);
                // 链同步
                syncService.confirmTx(txhash);
                //
                attestationList.add(attestation);
                //
                Map<String, String> m = new HashMap<>();
                m.put("txhash", txhash);
                txhashList.add(m);
            }
        }

        //
        if (async) {
            //
            rst.setResult(true);
            rst.setCode(ErrorInfo.SUCCESS.code());
            rst.setMsg(ErrorInfo.SUCCESS.desc());
            //
            return new ResponseEntity<>(rst, HttpStatus.OK);
        } else {
            // mybatis batch insert
            // 单条长度大概 4KB，30条，一个sql语句size大概为120KB
            // TODO 检查 max_allowed_packet
            contractService.saveToLocalBatch(company_ontid, attestationList);
            //
            rst.setResult(txhashList);
            rst.setCode(ErrorInfo.SUCCESS.code());
            rst.setMsg(ErrorInfo.SUCCESS.desc());
            //
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }
    }

    @PostMapping("/attestation/hash")
    public ResponseEntity<ResponseBean> selectByOntidAndHash(@RequestBody LinkedHashMap<String, Object> obj) throws Exception {

        //
        ResponseBean rst = new ResponseBean();

        //
        Set<String> required = new HashSet<>();
        required.add("hash");
        required.add("access_token");

        //
        validateService.validateParamsKeys(obj, required);
        validateService.validateParamsValues(obj);

        //
        String hash = (String) obj.get("hash");
        String accessToken = (String) obj.get("access_token");

        //
        String ontid = jwtService.getContentUser(accessToken);

        // ontid 也要作为条件，否则查到别人的了
        List<Attestation> list = contractService.selectByOntidAndHash(ontid, hash);

        //
        rst.setResult(list);
        rst.setCode(ErrorInfo.SUCCESS.code());
        rst.setMsg(ErrorInfo.SUCCESS.desc());

        //
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/attestation/hash/delete")
    public ResponseEntity<ResponseBean> deleteByOntidAndHash(@RequestBody LinkedHashMap<String, Object> obj) throws Exception {

        //
        ResponseBean rst = new ResponseBean();
        //
        Set<String> required = new HashSet<>();
        required.add("hash");
        required.add("access_token");

        //
        validateService.validateParamsKeys(obj, required);
        validateService.validateParamsValues(obj);

        //
        String txhash = (String) obj.get("hash");
        String accessToken = (String) obj.get("access_token");

        //
        String ontid = jwtService.getContentUser(accessToken);

        // ontid 也要作为条件
        List<Attestation> exists = contractService.selectByOntidAndTxHash(ontid, txhash);
        if (exists.size() != 0) {

            //
            int rt = contractService.updateStatusByOntidAndHash(ontid, txhash);
            System.out.println(rt);

            //
            Attestation exist = exists.get(0);
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
            rst.setCode(ErrorInfo.TXHASH_NOT_EXIST.code());
            rst.setMsg(ErrorInfo.TXHASH_NOT_EXIST.desc());
            //
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        rst.setResult(true);
        rst.setCode(ErrorInfo.SUCCESS.code());
        rst.setMsg(ErrorInfo.SUCCESS.desc());
        //
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/attestation/history")
    public ResponseEntity<ResponseBean> getHistory(@RequestBody LinkedHashMap<String, Object> obj) throws Exception {

        //
        ResponseBean rst = new ResponseBean();
        //
        Set<String> required = new HashSet<>();
        required.add("access_token");
        required.add("pageNum");
        required.add("pageSize");
        if (obj.containsKey("type")) {
            required.add("type");
        }

        //
        validateService.validateParamsKeys(obj, required);
        validateService.validateParamsValues(obj);

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
        String ontid = jwtService.getContentUser(accessToken);
        //
        List<Attestation> list = contractService.getHistoryByOntid(ontid, pageNum, pageSize, type);

        //
        rst.setResult(list);
        rst.setCode(ErrorInfo.SUCCESS.code());
        rst.setMsg(ErrorInfo.SUCCESS.desc());

        //
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/attestation/count")
    public ResponseEntity<ResponseBean> count(@RequestBody LinkedHashMap<String, Object> obj) throws Exception {

        //
        ResponseBean rst = new ResponseBean();

        //
        Set<String> required = new HashSet<>();
        required.add("access_token");

        //
        validateService.validateParamsKeys(obj, required);
        validateService.validateParamsValues(obj);

        //
        String accessToken = (String) obj.get("access_token");

        //
        String ontid = jwtService.getContentUser(accessToken);
        Integer count = contractService.count(ontid);

        //
        rst.setResult(count);
        rst.setCode(ErrorInfo.SUCCESS.code());
        rst.setMsg(ErrorInfo.SUCCESS.desc());

        //
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/attestation/explorer")
    public ResponseEntity<ResponseBean> getExplorer(@RequestBody LinkedHashMap<String, Object> obj) throws Exception {

        //
        ResponseBean rst = new ResponseBean();

        //
        Set<String> required = new HashSet<>();
        required.add("pageNum");
        required.add("pageSize");

        //
        validateService.validateParamsKeys(obj, required);
        validateService.validateParamsValues(obj);

        //
        int pageNum = Integer.parseInt(obj.get("pageNum").toString());
        int pageSize = Integer.parseInt(obj.get("pageSize").toString());

        //
        List<Attestation> list = new ArrayList<>();
        if (!StringUtils.isEmpty(GlobalVariable.CURRENT_CONTRACT_TABLE_NAME)) {
            list = contractService.getExplorerHistory(GlobalVariable.CURRENT_CONTRACT_TABLE_NAME, pageNum, pageSize);
        }

        //
        rst.setResult(list);
        rst.setCode(ErrorInfo.SUCCESS.code());
        rst.setMsg(ErrorInfo.SUCCESS.desc());

        //
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/attestation/explorer/hash")
    public ResponseEntity<ResponseBean> getExplorerHash(@RequestBody LinkedHashMap<String, Object> obj) throws Exception {

        //
        ResponseBean rst = new ResponseBean();

        //
        Set<String> required = new HashSet<>();
        required.add("hash");

        //
        validateService.validateParamsKeys(obj, required);
        validateService.validateParamsValues(obj);

        //
        String hash = (String) obj.get("hash");

        // ontid 也要作为条件，否则查到别人的了
        List<Attestation> list = contractService.selectByHash(hash);

        //
        rst.setResult(list);
        rst.setCode(ErrorInfo.SUCCESS.code());
        rst.setMsg(ErrorInfo.SUCCESS.desc());

        //
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }


    @PostMapping("/attestation/company/add")
    public ResponseEntity<ResponseBean> addCompany(@RequestBody LinkedHashMap<String, Object> obj) throws Exception {

        //
        ResponseBean rst = new ResponseBean();

        //
        Set<String> required = new HashSet<>();
        required.add("ontid");
        required.add("prikey");
        required.add("code_addr");
        //
        required.add("ont_password");

        //
        validateService.validateParamsKeys(obj, required);
        validateService.validateParamsValues(obj);

        //
        String ontid = (String) obj.get("ontid");
        String prikey = (String) obj.get("prikey");
        String code_addr = (String) obj.get("code_addr");

        //
        ContractCompany existed = contractService.getCompany(ontid);
        if (existed != null) {
            rst.setCode(ErrorInfo.ONTID_EXIST.code());
            rst.setMsg(ErrorInfo.ONTID_EXIST.desc());
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
        rst.setResult(true);
        rst.setCode(ErrorInfo.SUCCESS.code());
        rst.setMsg(ErrorInfo.SUCCESS.desc());
        return new ResponseEntity<>(rst, HttpStatus.OK);

    }

    @PostMapping("/attestation/company/update")
    public ResponseEntity<ResponseBean> updateCompany(@RequestBody LinkedHashMap<String, Object> obj) throws Exception {

        //
        ResponseBean rst = new ResponseBean();

        //
        Set<String> required = new HashSet<>();
        required.add("ontid");
        //
        required.add("ont_password");

        //
        validateService.validateParamsKeys(obj, required);
        validateService.validateParamsValues(obj);

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
            //
            rst.setResult(true);
            rst.setCode(ErrorInfo.ONTID_NOT_EXIST.code());
            rst.setMsg(ErrorInfo.ONTID_NOT_EXIST.desc());
            //
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
        rst.setResult(true);
        rst.setCode(ErrorInfo.SUCCESS.code());
        rst.setMsg(ErrorInfo.SUCCESS.desc());
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/attestation/company/get")
    public ResponseEntity<ResponseBean> getCompany(@RequestBody LinkedHashMap<String, Object> obj) throws Exception {

        //
        ResponseBean rst = new ResponseBean();

        //
        Set<String> required = new HashSet<>();
        required.add("ontid");
        required.add("ont_password");

        //
        validateService.validateParamsKeys(obj, required);
        validateService.validateParamsValues(obj);

        //
        String ontid = (String) obj.get("ontid");

        //
        ContractCompany company = contractService.getCompany(ontid);

        //
        rst.setResult(company);
        rst.setCode(ErrorInfo.SUCCESS.code());
        rst.setMsg(ErrorInfo.SUCCESS.desc());

        //
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/ontid/create")
    public ResponseEntity<ResponseBean> createOntid(@RequestBody LinkedHashMap<String, Object> obj) throws Exception {

        //
        ResponseBean rst = new ResponseBean();

        //
        Set<String> required = new HashSet<>();
        required.add("user_phone");
        // required.add("password");

        //
        validateService.validateParamsKeys(obj, required);
        validateService.validateParamsValues(obj);

        //
        String user_phone = (String) obj.get("user_phone");
        // String password = (String) obj.get("password");

        // String ontid = ontidServerService.registerPhoneWithoutCode(user_phone, password);
        String ontid = ontidServerService.registerPhoneWithoutCode(user_phone);
        //
        Map<String, String> m = new HashMap<>();
        m.put("user_ontid", ontid);
        //
        rst.setResult(m);
        rst.setCode(ErrorInfo.SUCCESS.code());
        rst.setMsg(ErrorInfo.SUCCESS.desc());
        //
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }
}
