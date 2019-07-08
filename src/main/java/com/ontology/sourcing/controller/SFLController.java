package com.ontology.sourcing.controller;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import com.ontology.sourcing.exception.ErrorInfo;
import com.ontology.sourcing.model.dao.sfl.SFLIdentity;
import com.ontology.sourcing.model.dao.sfl.SFLNotary;
import com.ontology.sourcing.model.dto.ResponseBean;
import com.ontology.sourcing.service.SFLService;
import com.ontology.sourcing.service.util.ValidateService;
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
    public SFLController(SFLService sflService,
                         ValidateService validateService) {
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
    public ResponseEntity<ResponseBean> putSFL(@RequestBody LinkedHashMap<String, Object> obj) throws Exception {

        //
        ResponseBean rst = new ResponseBean();

        //
        Set<String> required = new HashSet<>();
        required.add("userType");
        required.add("certType");
        required.add("certName");
        required.add("certNo");
        required.add("filehash");

        //
        validateService.validateParamsKeys(obj, required);
        validateService.validateParamsValues(obj);

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
        String token = sflService.getToken(sflIdentity);
        sflService.put(filehash, token, certNo);
        //
        rst.setResult(true);
        rst.setCode(ErrorInfo.SUCCESS.code());
        rst.setMsg(ErrorInfo.SUCCESS.desc());
        //
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/get")
    public ResponseEntity<ResponseBean> getSFL(@RequestBody LinkedHashMap<String, Object> obj) throws Exception {

        //
        ResponseBean rst = new ResponseBean();

        //
        Set<String> required = new HashSet<>();
        required.add("certNo");
        required.add("pageNum");
        required.add("pageSize");

        //
        validateService.validateParamsKeys(obj, required);
        validateService.validateParamsValues(obj);

        //
        String certNo = (String) obj.get("certNo");
        int pageNum = Integer.parseInt(obj.get("pageNum").toString());
        int pageSize = Integer.parseInt(obj.get("pageSize").toString());

        //
        List<SFLNotary> list = sflService.getHistoryByCertNo(certNo, pageNum, pageSize);

        //
        rst.setResult(list);
        rst.setCode(ErrorInfo.SUCCESS.code());
        rst.setMsg(ErrorInfo.SUCCESS.desc());

        //
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }
}
