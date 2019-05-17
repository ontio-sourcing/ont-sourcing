package com.ontology.sourcing.controller;

import ch.qos.logback.classic.Logger;
import com.github.ontio.network.exception.RestfulException;
import com.lambdaworks.crypto.SCryptUtil;
import com.ontology.sourcing.dao.ddo.*;
import com.ontology.sourcing.mapper.ddo.*;
import com.ontology.sourcing.model.ddo.DDOPojo;
import com.ontology.sourcing.model.util.Result;
import com.ontology.sourcing.service.OntidService;
import com.ontology.sourcing.service.util.SyncService;
import com.ontology.sourcing.service.util.ValidateService;
import com.ontology.sourcing.util.GlobalVariable;
import com.ontology.sourcing.util.exp.ErrorCode;
import com.ontology.sourcing.util.exp.ExistedException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;

@RestController
@RequestMapping("/api/v1/ddo")
public class OntidController {

    //
    private Logger logger = (Logger) LoggerFactory.getLogger(OntidController.class);

    //
    private OntidService ontidService;
    private ValidateService validateService;
    private SyncService syncService;
    //
    private ActionIndexMapper actionIndexMapper;
    private ActionOntidMapper actionOntidMapper;
    private ActionOntidControlMapper actionOntidControlMapper;

    @Autowired
    public OntidController(OntidService ontidService,
                           ValidateService validateService,
                           SyncService syncService,
                           ActionIndexMapper actionIndexMapper,
                           ActionOntidMapper actionOntidMapper,
                           ActionOntidControlMapper actionOntidControlMapper) {
        //
        this.ontidService = ontidService;
        this.validateService = validateService;
        this.syncService = syncService;
        //
        this.actionIndexMapper = actionIndexMapper;
        this.actionOntidMapper = actionOntidMapper;
        this.actionOntidControlMapper = actionOntidControlMapper;
    }

    //
    @PostConstruct
    public void postConstructor() {
        //
        logger.info("OntidController PostConstruct start ...");
    }

    @PostMapping("/create")
    public ResponseEntity<Result> createOntid(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("createOntid");

        //
        Set<String> required = new HashSet<>();
        required.add("username");
        required.add("password");

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
        String username = (String) obj.get("username");
        String password = (String) obj.get("password");

        //
        try {
            Map<String, String> map = ontidService.createOntid(password);
            //
            String keystore = map.get("keystore");
            String txhash = map.get("txhash");
            String ontid = map.get("ontid");
            //
            syncService.confirmTxAndDDO(ontid, txhash);
            // 写入本地表
            ActionOntid record = new ActionOntid();
            record.setUsername(username);
            record.setPassword(SCryptUtil.scrypt(password, GlobalVariable.scrypt_N, GlobalVariable.scrypt_r, GlobalVariable.scrypt_p));
            record.setOntid(ontid);
            record.setKeystore(keystore);
            record.setTxhash(txhash);
            record.setActionIndex(GlobalVariable.CURRENT_ACTION_TABLE_INDEX);
            record.setCreateTime(new Date());
            actionOntidMapper.save(record);
            //
            rst.setResult(ontid);
            //
            rst.setErrorAndDesc(ErrorCode.SUCCESSS);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }
    }

    @PostMapping("/update/attribute")
    public ResponseEntity<Result> updateOntidAttribute(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("updateOntidAttribute");

        //
        Set<String> required = new HashSet<>();
        required.add("ontid");
        required.add("password");
        required.add("attribute");

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
        String password = (String) obj.get("password");
        String attributeJson = obj.get("attribute").toString();

        //
        try {
            validateService.isExistedOntid(ontid);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        try {
            //
            Map<String, String> map = ontidService.updateOntidAttribute(ontid, password, attributeJson);
            String txhash = map.get("txhash");
            //
            ontidService.save(ontid, ontid, txhash, ActionTypes.ADD.getId(), attributeJson);
            syncService.confirmTxAndDDO(ontid, txhash);
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

    @PostMapping("/delete/attribute")
    public ResponseEntity<Result> deleteOntidAttribute(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("deleteOntidAttribute");

        //
        Set<String> required = new HashSet<>();
        required.add("ontid");
        required.add("password");
        required.add("path_key");

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
        String password = (String) obj.get("password");
        String path_key = (String) obj.get("path_key");

        //
        try {
            validateService.isExistedOntid(ontid);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        try {
            Map<String, String> map = ontidService.deleteEntityIdentityAttribute(ontid, password, path_key);
            String txhash = map.get("txhash");
            //
            ontidService.save(ontid, ontid, txhash, ActionTypes.DELETE.getId(), path_key);
            syncService.confirmTxAndDDO(ontid, txhash);
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

    @PostMapping("/update/control")
    public ResponseEntity<Result> updateOntidControl(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("updateOntidControl");

        //
        Set<String> required = new HashSet<>();
        required.add("ontid");
        required.add("password");
        required.add("controlOntid");

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
        String password = (String) obj.get("password");
        String controlOntid = (String) obj.get("controlOntid");

        //
        try {
            validateService.isExistedOntid(ontid);
            validateService.isExistedOntid(controlOntid);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        try {
            Map<String, String> map = ontidService.updateOntidControl(ontid, password, controlOntid);
            String txhash = map.get("txhash");
            // 写入本地表
            ActionOntidControl record = new ActionOntidControl();
            record.setOntid(ontid);
            record.setControl(controlOntid);
            record.setTxhash(txhash);
            record.setCreateTime(new Date());
            actionOntidControlMapper.save(record);
            // 链同步
            syncService.confirmTxAndDDO(ontid, txhash);
            //
            rst.setResult(true);
            rst.setErrorAndDesc(ErrorCode.SUCCESSS);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        } catch (ExistedException e) {
            logger.error(e.getMessage());
            rst.setError(ErrorCode.ONTID_PubKey_EXIST.getId());
            rst.setDesc(ErrorCode.ONTID_PubKey_EXIST.getMessage());
            return new ResponseEntity<>(rst, HttpStatus.OK);
        } catch (RestfulException e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }
    }

    @PostMapping("/update/attribute/control")
    public ResponseEntity<Result> updateOntidAttributeByControl(@RequestBody LinkedHashMap<String, Object> obj) {
        //
        Result rst = new Result("updateOntidAttributeByControl");

        //
        Set<String> required = new HashSet<>();
        required.add("ontid");
        required.add("password");
        required.add("controlOntid");
        required.add("controlPassword");

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
        String attributeJson = (String) obj.get("attribute").toString();
        String controlOntid = (String) obj.get("controlOntid");
        String controlPassword = (String) obj.get("controlPassword");

        //
        try {
            validateService.isExistedOntid(ontid);
            validateService.isExistedOntid(controlOntid);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        try {
            Map<String, String> map = ontidService.updateOntidAttributeByControl(ontid, attributeJson, controlOntid, controlPassword);
            String txhash = map.get("txhash");
            //
            ontidService.save(ontid, controlOntid, txhash, ActionTypes.ADD.getId(), attributeJson);
            syncService.confirmTxAndDDO(ontid, txhash);
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

    @PostMapping("/delete/attribute/control")
    public ResponseEntity<Result> deleteOntidAttributeByControl(@RequestBody LinkedHashMap<String, Object> obj) {
        //
        Result rst = new Result("updateOntidAttributeByControl");

        //
        Set<String> required = new HashSet<>();
        required.add("ontid");
        required.add("path_key");
        required.add("controlOntid");
        required.add("controlPassword");

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
        String path_key = (String) obj.get("path_key");
        String controlOntid = (String) obj.get("controlOntid");
        String controlPassword = (String) obj.get("controlPassword");

        //
        try {
            validateService.isExistedOntid(ontid);
            validateService.isExistedOntid(controlOntid);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        try {
            Map<String, String> map = ontidService.deleteEntityIdentityAttributeByControl(ontid, path_key, controlOntid, controlPassword);
            String txhash = map.get("txhash");
            //
            ontidService.save(ontid, controlOntid, txhash, ActionTypes.DELETE.getId(), path_key);
            syncService.confirmTxAndDDO(ontid, txhash);
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

    @PostMapping("/getddo")
    public ResponseEntity<Result> getDDO(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("getDDO");

        //
        Set<String> required = new HashSet<>();
        required.add("ontid");

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
        try {
            validateService.isExistedOntid(ontid);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        try {
            //
            DDOPojo ddoPojo = ontidService.getDDO(ontid);
            //
            rst.setResult(ddoPojo);
            //
            rst.setErrorAndDesc(ErrorCode.SUCCESSS);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }
    }

    @PostMapping("/count")
    public ResponseEntity<Result> count(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("count");

        //
        Set<String> required = new HashSet<>();
        required.add("ontid");

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
        try {
            validateService.isExistedOntid(ontid);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        ActionOntid actionOntidRecord = actionOntidMapper.findByOntid(ontid);
        // 所在的表索引
        ActionIndex index = actionIndexMapper.selectByPrimaryKey(actionOntidRecord.getActionIndex());
        //
        Integer count = ontidService.count(index.getName(), ontid);
        //
        rst.setResult(count);
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/history")
    public ResponseEntity<Result> getHistory(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("getExplorerHistory");

        //
        Set<String> required = new HashSet<>();
        required.add("ontid");
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
        String ontid = (String) obj.get("ontid");
        int pageNum = Integer.parseInt(obj.get("pageNum").toString());
        int pageSize = Integer.parseInt(obj.get("pageSize").toString());

        //
        try {
            validateService.isExistedOntid(ontid);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }

        //
        ActionOntid actionOntidRecord = actionOntidMapper.findByOntid(ontid);
        // 所在的表索引
        ActionIndex index = actionIndexMapper.selectByPrimaryKey(actionOntidRecord.getActionIndex());
        //
        List<Action> list = ontidService.getActionHistory(index.getName(), ontid, pageNum, pageSize);
        //
        rst.setResult(list);
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }
}
