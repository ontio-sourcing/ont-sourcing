package com.ontology.sourcing;

import com.github.ontio.account.Account;
import com.github.ontio.common.Helper;
import com.github.ontio.network.exception.ConnectorException;
import com.github.ontio.smartcontract.neovm.abi.BuildParams;
import com.google.gson.Gson;
import com.ontology.sourcing.model.dto.event.EventPojo;
import com.ontology.sourcing.model.dto.event.Notify;
import com.ontology.sourcing.service.ContractService;
import com.ontology.sourcing.service.util.ChainService;
import com.ontology.sourcing.util.GlobalVariable;
import org.apache.commons.codec.DecoderException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "file:/Volumes/Data/_work/201802_Ontology/ONTSouring/ont-sourcing/config/application-local.properties")
public class AttestationTests04 {

    //
    private Gson gson = new Gson();

    //
    @Autowired
    ChainService chainService;
    @Autowired
    ContractService contractService;

    // 付款的数字钱包
    private Account payerAccount = GlobalVariable.getInstanceOfAccount("6a62d116e416246f974229eee7d1b0894d8c2ab70446856e85e35b7f5d37adef");

    // 公共合约_test
    private String codeAddr = "e2510ed1044503faf6e3e66b98372606bbeae38f";

    @Test
    public void example01() throws Exception {

        List paramList = new ArrayList<>();
        paramList.add("putRecord".getBytes());

        List args = new ArrayList();
        args.add("7467b431e3acc8861f6a10a9b312de99f0e4b532de423cc5df2ff10addab0375");
        args.add("e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927102c67");

        paramList.add(args);
        byte[] params = BuildParams.createCodeParamsScript(paramList);

        //
        Map<String, String> map = contractService.invokeContract(Helper.reverse(codeAddr), null, params, payerAccount, 76220L, GlobalVariable.DEFAULT_GAS_PRICE, false);

        //
        String txhash = map.get("txhash");
        System.out.println(txhash);
        // da0a97b7e8e4c3d49645bbdf8579d8badfcc8aeb8a45384ec7dc8209e184d50a

        String result = map.get("result");
        System.out.println(result);
        // true
    }

    @Test
    public void example02() throws ConnectorException, IOException, DecoderException {

        Object rst = chainService.ontSdk.getConnect().getSmartCodeEvent("da0a97b7e8e4c3d49645bbdf8579d8badfcc8aeb8a45384ec7dc8209e184d50a");

        //
        String ntfy = rst.toString();

        //
        EventPojo ep = gson.fromJson(ntfy, EventPojo.class);

        //
        Notify n0 = ep.getNotify().get(0);

        //
        List<String> states = n0.getStates();

        //
        String s1 = states.get(0);
        String n1 = new String(Helper.hexToBytes(s1));
        System.out.println(n1);
        // putRecord

        String s2 = states.get(1);
        String n2 = new String(Helper.hexToBytes(s2));
        System.out.println(n2);
        // 7467b431e3acc8861f6a10a9b312de99f0e4b532de423cc5df2ff10addab0375

        String s3 = states.get(2);
        String n3 = new String(Helper.hexToBytes(s3));
        System.out.println(n3);
        // e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927102c67
    }

}
