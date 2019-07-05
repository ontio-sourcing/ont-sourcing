package com.ontology.sourcing.controller;

import ch.qos.logback.classic.Logger;
import com.ontology.sourcing.exception.ErrorInfo;
import com.ontology.sourcing.exception.exp.ExistedException;
import com.ontology.sourcing.mapper.ddo.*;
import com.ontology.sourcing.model.common.ddo.Action;
import com.ontology.sourcing.model.common.ddo.ActionTypes;
import com.ontology.sourcing.model.dao.ddo.*;
import com.ontology.sourcing.model.dto.ResponseBean;
import com.ontology.sourcing.model.dto.ddo.DDOPojo;
import com.ontology.sourcing.service.OntidService;
import com.ontology.sourcing.service.util.SyncService;
import com.ontology.sourcing.service.util.ValidateService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import java.util.*;

@RestController
@RequestMapping("/api/v1/ddo")
public class DDOController {

    //
    private Logger logger = (Logger) LoggerFactory.getLogger(DDOController.class);

    //
    private OntidService ontidService;
    private ValidateService validateService;
    private SyncService syncService;
    //
    private ActionIndexMapper actionIndexMapper;
    private ActionOntidMapper actionOntidMapper;
    private ActionOntidControlMapper actionOntidControlMapper;

    @Autowired
    public DDOController(OntidService ontidService,
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
    public ResponseEntity<ResponseBean> createOntid(@RequestBody LinkedHashMap<String, Object> obj) throws Exception {

        //
        ResponseBean rst = new ResponseBean();

        //
        Set<String> required = new HashSet<>();
        required.add("username");
        required.add("password");

        //
        validateService.validateParamsKeys(obj, required);
        validateService.validateParamsValues(obj);

        //
        String username = (String) obj.get("username");
        String password = (String) obj.get("password");

        //
        Map<String, String> map = ontidService.createOntid(username, password);
        //
        String keystore = map.get("keystore");
        String txhash = map.get("txhash");
        String ontid = map.get("ontid");
        //
        syncService.confirmTxAndDDO(ontid, txhash);

        //
        rst.setResult(ontid);
        rst.setCode(ErrorInfo.SUCCESS.code());
        rst.setMsg(ErrorInfo.SUCCESS.desc());
        //
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseBean> login(@RequestBody LinkedHashMap<String, Object> obj) throws Exception {

        //
        ResponseBean rst = new ResponseBean();

        //
        Set<String> required = new HashSet<>();
        required.add("username");
        required.add("password");

        //
        validateService.validateParamsKeys(obj, required);
        validateService.validateParamsValues(obj);

        //
        String username = (String) obj.get("username");
        String password = (String) obj.get("password");

        //
        Map<String, String> map = ontidService.login(username, password);

        //
        rst.setResult(map);
        rst.setCode(ErrorInfo.SUCCESS.code());
        rst.setMsg(ErrorInfo.SUCCESS.desc());
        //
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/update/attribute")
    public ResponseEntity<ResponseBean> updateOntidAttribute(@RequestBody LinkedHashMap<String, Object> obj) throws Exception {

        //
        ResponseBean rst = new ResponseBean();

        //
        Set<String> required = new HashSet<>();
        required.add("ontid");
        required.add("password");
        required.add("attribute");

        //
        validateService.validateParamsKeys(obj, required);
        validateService.validateParamsValues(obj);

        //
        String ontid = (String) obj.get("ontid");
        String password = (String) obj.get("password");
        String attributeJson = obj.get("attribute").toString();

        //
        validateService.isExistedOntid(ontid);

        //
        Map<String, String> map = ontidService.updateOntidAttribute(ontid, password, attributeJson);
        String txhash = map.get("txhash");
        //
        ontidService.save(ontid, ontid, txhash, ActionTypes.ADD.getId(), attributeJson);
        syncService.confirmTxAndDDO(ontid, txhash);

        //
        rst.setResult(true);
        rst.setCode(ErrorInfo.SUCCESS.code());
        rst.setMsg(ErrorInfo.SUCCESS.desc());
        //
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/delete/attribute")
    public ResponseEntity<ResponseBean> deleteOntidAttribute(@RequestBody LinkedHashMap<String, Object> obj) throws Exception {

        //
        ResponseBean rst = new ResponseBean();

        //
        Set<String> required = new HashSet<>();
        required.add("ontid");
        required.add("password");
        required.add("path_key");

        //
        validateService.validateParamsKeys(obj, required);
        validateService.validateParamsValues(obj);

        //
        String ontid = (String) obj.get("ontid");
        String password = (String) obj.get("password");
        String pathKey = (String) obj.get("path_key");

        //
        validateService.isExistedOntid(ontid);

        //
        Map<String, String> map = ontidService.deleteEntityIdentityAttribute(ontid, password, pathKey);
        String txhash = map.get("txhash");
        //
        ontidService.save(ontid, ontid, txhash, ActionTypes.DELETE.getId(), pathKey);
        syncService.confirmTxAndDDO(ontid, txhash);
        //
        rst.setResult(true);
        rst.setCode(ErrorInfo.SUCCESS.code());
        rst.setMsg(ErrorInfo.SUCCESS.desc());
        //
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/update/control")
    public ResponseEntity<ResponseBean> updateOntidControl(@RequestBody LinkedHashMap<String, Object> obj) throws Exception {

        //
        ResponseBean rst = new ResponseBean();

        //
        Set<String> required = new HashSet<>();
        required.add("ontid");
        required.add("password");
        required.add("controlOntid");

        //
        validateService.validateParamsKeys(obj, required);
        validateService.validateParamsValues(obj);

        //
        String ontid = (String) obj.get("ontid");
        String password = (String) obj.get("password");
        String controlOntid = (String) obj.get("controlOntid");

        //
        validateService.isExistedOntid(ontid);
        validateService.isExistedOntid(controlOntid);

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
            actionOntidControlMapper.insertSelective(record);
            // 链同步
            syncService.confirmTxAndDDO(ontid, txhash);
            //
            rst.setResult(true);
            rst.setCode(ErrorInfo.SUCCESS.code());
            rst.setMsg(ErrorInfo.SUCCESS.desc());
            //
            return new ResponseEntity<>(rst, HttpStatus.OK);
        } catch (ExistedException e) {
            logger.error(e.getMessage());
            //
            rst.setCode(ErrorInfo.ONTID_PubKey_EXIST.code());
            rst.setMsg(ErrorInfo.ONTID_PubKey_EXIST.desc());
            //
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }
    }

    @PostMapping("/update/attribute/control")
    public ResponseEntity<ResponseBean> updateOntidAttributeByControl(@RequestBody LinkedHashMap<String, Object> obj) throws Exception {
        //
        ResponseBean rst = new ResponseBean();

        //
        Set<String> required = new HashSet<>();
        required.add("ontid");
        required.add("password");
        required.add("controlOntid");
        required.add("controlPassword");

        //
        validateService.validateParamsKeys(obj, required);
        validateService.validateParamsValues(obj);

        //
        String ontid = (String) obj.get("ontid");
        String attributeJson = (String) obj.get("attribute").toString();
        String controlOntid = (String) obj.get("controlOntid");
        String controlPassword = (String) obj.get("controlPassword");

        //
        validateService.isExistedOntid(ontid);
        validateService.isExistedOntid(controlOntid);

        //
        Map<String, String> map = ontidService.updateOntidAttributeByControl(ontid, attributeJson, controlOntid, controlPassword);
        String txhash = map.get("txhash");
        //
        ontidService.save(ontid, controlOntid, txhash, ActionTypes.ADD.getId(), attributeJson);
        syncService.confirmTxAndDDO(ontid, txhash);

        //
        rst.setResult(true);
        rst.setCode(ErrorInfo.SUCCESS.code());
        rst.setMsg(ErrorInfo.SUCCESS.desc());
        //
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/delete/attribute/control")
    public ResponseEntity<ResponseBean> deleteOntidAttributeByControl(@RequestBody LinkedHashMap<String, Object> obj) throws Exception {
        //
        ResponseBean rst = new ResponseBean();

        //
        Set<String> required = new HashSet<>();
        required.add("ontid");
        required.add("path_key");
        required.add("controlOntid");
        required.add("controlPassword");

        //
        validateService.validateParamsKeys(obj, required);
        validateService.validateParamsValues(obj);

        //
        String ontid = (String) obj.get("ontid");
        String pathKey = (String) obj.get("path_key");
        String controlOntid = (String) obj.get("controlOntid");
        String controlPassword = (String) obj.get("controlPassword");

        //
        validateService.isExistedOntid(ontid);
        validateService.isExistedOntid(controlOntid);

        //
        Map<String, String> map = ontidService.deleteEntityIdentityAttributeByControl(ontid, pathKey, controlOntid, controlPassword);
        String txhash = map.get("txhash");
        //
        ontidService.save(ontid, controlOntid, txhash, ActionTypes.DELETE.getId(), pathKey);
        syncService.confirmTxAndDDO(ontid, txhash);
        //
        rst.setResult(true);
        rst.setCode(ErrorInfo.SUCCESS.code());
        rst.setMsg(ErrorInfo.SUCCESS.desc());
        //
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/getddo")
    public ResponseEntity<ResponseBean> getDDO(@RequestBody LinkedHashMap<String, Object> obj) throws Exception {

        //
        ResponseBean rst = new ResponseBean();

        //
        Set<String> required = new HashSet<>();
        required.add("ontid");

        //
        validateService.validateParamsKeys(obj, required);
        validateService.validateParamsValues(obj);

        //
        String ontid = (String) obj.get("ontid");

        //
        validateService.isExistedOntid(ontid);

        //
        DDOPojo ddoPojo = ontidService.getDDO(ontid);
        //
        rst.setResult(ddoPojo);
        rst.setCode(ErrorInfo.SUCCESS.code());
        rst.setMsg(ErrorInfo.SUCCESS.desc());
        //
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/count")
    public ResponseEntity<ResponseBean> count(@RequestBody LinkedHashMap<String, Object> obj) throws Exception {

        //
        ResponseBean rst = new ResponseBean();

        //
        Set<String> required = new HashSet<>();
        required.add("ontid");

        //
        validateService.validateParamsKeys(obj, required);
        validateService.validateParamsValues(obj);

        //
        String ontid = (String) obj.get("ontid");

        //
        validateService.isExistedOntid(ontid);

        //
        Example example = new Example(ActionOntid.class);
        example.createCriteria().andCondition("ontid=", ontid);
        ActionOntid actionOntidRecord = actionOntidMapper.selectOneByExample(ontid);

        // 所在的表索引
        ActionIndex index = actionIndexMapper.selectByPrimaryKey(actionOntidRecord.getActionIndex());
        //
        Integer count = ontidService.count(index.getName(), ontid);
        //
        rst.setResult(count);
        rst.setCode(ErrorInfo.SUCCESS.code());
        rst.setMsg(ErrorInfo.SUCCESS.desc());
        //
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/history")
    public ResponseEntity<ResponseBean> getHistory(@RequestBody LinkedHashMap<String, Object> obj) throws Exception {
        //
        ResponseBean rst = new ResponseBean();

        //
        Set<String> required = new HashSet<>();
        required.add("ontid");
        required.add("pageNum");
        required.add("pageSize");

        //
        validateService.validateParamsKeys(obj, required);
        validateService.validateParamsValues(obj);

        //
        String ontid = (String) obj.get("ontid");
        int pageNum = Integer.parseInt(obj.get("pageNum").toString());
        int pageSize = Integer.parseInt(obj.get("pageSize").toString());

        //
        validateService.isExistedOntid(ontid);

        //
        Example example = new Example(ActionOntid.class);
        example.createCriteria().andCondition("ontid=", ontid);
        ActionOntid actionOntidRecord = actionOntidMapper.selectOneByExample(example);
        // 所在的表索引
        ActionIndex index = actionIndexMapper.selectByPrimaryKey(actionOntidRecord.getActionIndex());
        //
        List<Action> list = ontidService.getActionHistory(index.getName(), ontid, pageNum, pageSize);
        //
        rst.setResult(list);
        rst.setCode(ErrorInfo.SUCCESS.code());
        rst.setMsg(ErrorInfo.SUCCESS.desc());
        //
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }
}
