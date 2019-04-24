package com.ontology.sourcing;

import com.alibaba.fastjson.JSON;
import com.github.ontio.OntSdk;
import com.github.ontio.account.Account;
import com.github.ontio.common.Address;
import com.github.ontio.common.Helper;
import com.github.ontio.core.transaction.Transaction;
import com.github.ontio.sdk.exception.SDKException;
import com.github.ontio.smartcontract.neovm.abi.BuildParams;
import com.ontology.sourcing.utils.GlobalVariable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContractTests03 {

    //
    private OntSdk ontSdk = GlobalVariable.getOntSdk("http://polaris1.ont.io", "/Volumes/Data/_work/201802_Ontology/ONTSouring/ont-sourcing/config/wallet.json");

    // 付款的数字钱包
    private com.github.ontio.account.Account payerAccount = GlobalVariable.getInstanceOfAccount("6a62d116e416246f974229eee7d1b0894d8c2ab70446856e85e35b7f5d37adef");

    // https://github.com/ontio/documentation/blob/master/smart-contract-tutorial/examples/contractRecord.cs
    // AVM字节码
    private String contractCode = "5ac56b6c766b00527ac46c766b51527ac4616c766b00c303507574876c766b52527ac46c766b52c3645d00616c766b51c3c0529c009c6c766b55527ac46c766b55c3640e00006c766b56527ac462a2006c766b51c300c36c766b53527ac46c766b51c351c36c766b54527ac46c766b53c36c766b54c3617c6580006c766b56527ac4626d006c766b00c303476574876c766b57527ac46c766b57c3644900616c766b51c3c0519c009c6c766b59527ac46c766b59c3640e00006c766b56527ac4622f006c766b51c300c36c766b58527ac46c766b58c36165dd006c766b56527ac4620e00006c766b56527ac46203006c766b56c3616c756653c56b6c766b00527ac46c766b51527ac46161681953797374656d2e53746f726167652e476574436f6e746578746c766b00c36c766b51c3615272681253797374656d2e53746f726167652e5075746161035075746c766b00c36c766b51c3615272097075745265636f726454c1681553797374656d2e52756e74696d652e4e6f746966796151c5760003507574c461681553797374656d2e52756e74696d652e4e6f7469667961516c766b52527ac46203006c766b52c3616c756652c56b6c766b00527ac46161034765746c766b00c3617c096765745265636f726453c1681553797374656d2e52756e74696d652e4e6f746966796151c5760003476574c461681553797374656d2e52756e74696d652e4e6f746966796161681953797374656d2e53746f726167652e476574436f6e746578746c766b00c3617c681253797374656d2e53746f726167652e4765746c766b51527ac46203006c766b51c3616c7566";

    // 合约哈希/合约地址/contract codeAddr
    private String codeAddr = Address.AddressFromVmCode(contractCode).toHexString();
    // 6864a62235279e4c5c3fba004905f30e2157169a
    // TODO ABI

    @Test
    public void example01() throws Exception {

        List paramList = new ArrayList<>();
        paramList.add("Put".getBytes());

        List args = new ArrayList();
        args.add("key738548730");
        args.add("value6354369258");

        paramList.add(args);
        byte[] params = BuildParams.createCodeParamsScript(paramList);

        //
        Map<String, String> map = invokeContractPreExec(Helper.reverse(codeAddr), null, params, payerAccount, ontSdk.DEFAULT_GAS_LIMIT, GlobalVariable.DEFAULT_GAS_PRICE);

        //
        String txhash = map.get("txhash");
        System.out.println(txhash);
        // 84988564443e54827b238513f04379d427a6e66869cdc4c70951eed61fe9b516

        String result = map.get("result");
        System.out.println(result);
/*
{
    "Notify":[
        {
            "States":[
                "7075745265636f7264",
                "507574",
                "6b6579373338353438373330",
                "76616c756536333534333639323538"
            ],
            "ContractAddress":"6864a62235279e4c5c3fba004905f30e2157169a"
        },
        {
            "States":[
                "507574"
            ],
            "ContractAddress":"6864a62235279e4c5c3fba004905f30e2157169a"
        }
    ],
    "State":1,
    "Gas":20000,
    "Result":"01"
}
 */

        String s1 = JSON.parseObject(result).getString("Result");
        System.out.println(s1);
        // 01

        byte[] s2 = Helper.hexToBytes(s1);

        String s3 = new String(s2);
        System.out.println(s3);
        //
    }

    @Test
    public void example02() throws Exception {

        List paramList = new ArrayList<>();
        paramList.add("Get".getBytes());

        List args = new ArrayList();
        args.add("key738548730");

        paramList.add(args);
        byte[] params = BuildParams.createCodeParamsScript(paramList);

        //
        Map<String, String> map = invokeContractPreExec(Helper.reverse(codeAddr), null, params, payerAccount, ontSdk.DEFAULT_GAS_LIMIT, GlobalVariable.DEFAULT_GAS_PRICE);

        //
        String txhash = map.get("txhash");
        System.out.println(txhash);
        // 922e23a4d249b5c2f1a4417dde025099ff54fe3614ecfd93f6734c679586faee

        String result = map.get("result");
        System.out.println(result);
/*
{
    "Notify":[
        {
            "States":[
                "6765745265636f7264",
                "476574",
                "6b6579373338353438373330"
            ],
            "ContractAddress":"6864a62235279e4c5c3fba004905f30e2157169a"
        },
        {
            "States":[
                "476574"
            ],
            "ContractAddress":"6864a62235279e4c5c3fba004905f30e2157169a"
        }
    ],
    "State":1,
    "Gas":20000,
    "Result":""
}
 */

        String s1 = JSON.parseObject(result).getString("Result");
        System.out.println(s1);
        //

        byte[] s2 = Helper.hexToBytes(s1);

        String s3 = new String(s2);
        System.out.println(s3);
    }


    public Map<String, String> invokeContractPreExec(String codeAddr, String method, byte[] params, Account payerAcct, long gaslimit, long gasprice) throws Exception {

        //
        if (payerAcct == null) {
            throw new SDKException("params should not be null");
        }
        if (gaslimit < 0 || gasprice < 0) {
            throw new SDKException("gaslimit or gasprice should not be less than 0");
        }

        //
        Map<String, String> map = new HashMap<String, String>();

        Transaction tx = ontSdk.vm().makeInvokeCodeTransaction(codeAddr, method, params, payerAcct.getAddressU160().toBase58(), gaslimit, gasprice);
        System.out.println(tx);
        // com.github.ontio.core.payload.InvokeCode@fecc2faa

        //
        ontSdk.addSign(tx, payerAcct);

        //
        Object result = ontSdk.getConnect().sendRawTransactionPreExec(tx.toHexString());

        //
        String txhash = tx.hash().toString();

        //
        map.put("txhash", txhash);
        map.put("result", result.toString());
        return map;
    }

}
