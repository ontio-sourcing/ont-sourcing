package com.ontology.sourcing.controller;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import com.ontology.sourcing.model.dao.sfl.SFLIdentity;
import com.ontology.sourcing.model.dao.sfl.SFLNotary;
import com.ontology.sourcing.model.common.Result;
import com.ontology.sourcing.service.SFLService;
import com.ontology.sourcing.service.util.ValidateService;
import com.ontology.sourcing.util.exp.ErrorCode;
import com.ontology.sourcing.util.exp.ONTSourcingException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;

@RestController
@RequestMapping("/api/v1/sfl")
public class SFLController {

    //
    private Logger logger = (Logger) LoggerFactory.getLogger(SFLController.class);

    //
    private Gson gson = new Gson();

    //
    private SFLService sflService;
    private ValidateService validateService;

    @Autowired
    public SFLController(SFLService sflService, ValidateService validateService) {
        //
        this.sflService = sflService;
        this.validateService = validateService;
    }

    //
    @PostConstruct
    public void postConstructor() {
        //
        logger.info("SFLController PostConstruct start ...");
    }

    @PostMapping("/put")
    public ResponseEntity<Result> putSFL(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("putSFL");

        //
        Set<String> required = new HashSet<>();
        required.add("userType");
        required.add("certType");
        required.add("certName");
        required.add("certNo");
        required.add("filehash");

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
        String userType = (String) obj.get("userType");
        String certType = (String) obj.get("certType");
        String certName = (String) obj.get("certName");
        String certNo = (String) obj.get("certNo");
        String filehash = (String) obj.get("filehash");
        //
        String mobileNo = "";
        if (obj.containsKey("mobileNo")) {
            mobileNo = (String) obj.get("mobileNo");
        }
        String legalPerson = "";
        if (obj.containsKey("legalPerson")) {
            legalPerson = (String) obj.get("legalPerson");
        }
        String legalPersonId = "";
        if (obj.containsKey("legalPersonId")) {
            legalPersonId = (String) obj.get("legalPersonId");
        }
        String agent = "";
        if (obj.containsKey("agent")) {
            agent = (String) obj.get("agent");
        }
        String agentId = "";
        if (obj.containsKey("agentId")) {
            agentId = (String) obj.get("agentId");
        }
        String properties = "";
        if (obj.containsKey("properties")) {
            properties = (String) obj.get("properties");
        }

        //
        SFLIdentity sflIdentity = new SFLIdentity();
        sflIdentity.setUserType(userType);
        sflIdentity.setCertType(certType);
        sflIdentity.setCertName(certName);
        sflIdentity.setCertNo(certNo);
        sflIdentity.setMobileNo(mobileNo);
        sflIdentity.setLegalPerson(legalPerson);
        sflIdentity.setLegalPersonId(legalPersonId);
        sflIdentity.setAgent(agent);
        sflIdentity.setAgentId(agentId);
        sflIdentity.setProperties(properties);

        //
        String token = "";
        try {
            //
            token = sflService.getToken(sflIdentity);
            sflService.put(filehash, token, certNo);
            //
            rst.setResult(true);
            rst.setErrorAndDesc(ErrorCode.SUCCESSS);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        } catch (ONTSourcingException e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            rst.setErrorAndDesc(e);
            return new ResponseEntity<>(rst, HttpStatus.OK);
        }
    }

    @PostMapping("/get")
    public ResponseEntity<Result> getSFL(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result("getSFL");

        //
        Set<String> required = new HashSet<>();
        required.add("certNo");
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
        String certNo = (String) obj.get("certNo");
        int pageNum = Integer.parseInt(obj.get("pageNum").toString());
        int pageSize = Integer.parseInt(obj.get("pageSize").toString());

        //
        List<SFLNotary> list = sflService.getByCertNoPageable(certNo, pageNum, pageSize);
        //
        rst.setResult(list);
        rst.setErrorAndDesc(ErrorCode.SUCCESSS);
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }
}
