package com.github.ontio.sourcing.controller;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import com.github.ontio.sourcing.exception.ErrorInfo;
import com.github.ontio.sourcing.model.dto.ResponseBean;
import com.github.ontio.sourcing.service.WalletService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {

    //
    private Logger logger = (Logger) LoggerFactory.getLogger(WalletController.class);

    //
    private Gson gson = new Gson();

    //
    private WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        //
        this.walletService = walletService;
    }

    //
    @PostConstruct
    public void postConstructor() {
        //
        logger.info("WalletController PostConstruct start ...");
    }

    @GetMapping("/test")
    public ResponseEntity<ResponseBean> test() {

        //
        ResponseBean rst = new ResponseBean();

        //
        walletService.testFeign();

        //
        rst.setCode(0);
        rst.setMsg("hello world.");
        rst.setResult("");
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseBean> createWallet(@RequestBody LinkedHashMap<String, Object> obj) throws Exception {

        //
        ResponseBean rst = new ResponseBean();

        //
        String pwd = (String) obj.get("password");
        if (StringUtils.isEmpty(pwd)) {
            rst.setCode(ErrorInfo.PARAMS.code());
            rst.setMsg(ErrorInfo.PARAMS.desc());
        } else {
            String addr = walletService.createWallet(pwd);
            rst.setCode(ErrorInfo.SUCCESS.code());
            rst.setMsg(ErrorInfo.SUCCESS.desc());
            rst.setResult(addr);
        }

        //
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }
}
