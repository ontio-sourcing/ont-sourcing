package com.ontology.sourcing;

import com.github.ontio.account.Account;
import com.github.ontio.common.Address;
import com.github.ontio.common.Helper;
import com.github.ontio.crypto.SignatureScheme;
import com.github.ontio.sdk.wallet.Identity;
import com.ontology.sourcing.service.util.ChainService;
import com.ontology.sourcing.util.GlobalVariable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IdentityTests01 {

    //
    @Autowired
    ChainService chainService;


    // 付款的数字钱包
    private com.github.ontio.account.Account payerAccount = GlobalVariable.getInstanceOfAccount("6a62d116e416246f974229eee7d1b0894d8c2ab70446856e85e35b7f5d37adef");


    // 同一个 私钥，对应着 一套 数字资产账户 和 数字身份账户
    @Test
    public void example01() throws Exception {
        //
        String passphrase = "123456";
        String prikey = "0fb11f6ece9b9cba55936965c0eef8c15819bc74d7588c1feb3217b8948e1854";

        //
        Account account = new Account(Helper.hexToBytes(prikey), SignatureScheme.SHA256WITHECDSA);

        // 获取对应的 钱包账户 地址
        Address addr = account.getAddressU160();
        System.out.println(addr.toBase58());  // ALcwsJqdFyPr4sd1bnS5bdnaBYDgfmCsBd
    }

    @Test
    public void example02() throws Exception {
        //
        String passphrase = "123456";
        String prikey = "0fb11f6ece9b9cba55936965c0eef8c15819bc74d7588c1feb3217b8948e1854";
        //
        Identity identity = chainService.ontSdk.getWalletMgr().createIdentityFromPriKey(passphrase, prikey);
        System.out.println(identity);
/*
{
    "controls":[
        {
            "codeAddr":"ALcwsJqdFyPr4sd1bnS5bdnaBYDgfmCsBd",
            "algorithm":"ECDSA",
            "enc-alg":"aes-256-gcm",
            "hash":"sha256",
            "id":"keys-1",
            "key":"SABgi5AMddCYFtjpCkf/CXq8W9A/de7WUE8VWtEw+2TouXRdhRSRk68AwMggMvJk",
            "parameters":{
                "curve":"P-256"
            },
            "publicKey":"03088ae9cc9116e49e8c1b5b712b75e18c7d65e520cbc199d26d1765732a547e14",
            "salt":"GsuHQWNQji3YVRQV3TyHxg=="
        }
    ],
    "isDefault":false,
    "label":"70bf82c1",
    "lock":false,
    "ontid":"did:ont:ALcwsJqdFyPr4sd1bnS5bdnaBYDgfmCsBd"
}
 */

        // 再次运行
/*
{
    "controls":[
        {
            "codeAddr":"ALcwsJqdFyPr4sd1bnS5bdnaBYDgfmCsBd",
            "algorithm":"ECDSA",
            "enc-alg":"aes-256-gcm",
            "hash":"sha256",
            "id":"keys-1",
            "key":"n7L3AbfFXdtzKJoU0ktItE4AEVtAtSv8cXvWG8Uy+obsEWvUPourL4cB+50NmOje",
            "parameters":{
                "curve":"P-256"
            },
            "publicKey":"03088ae9cc9116e49e8c1b5b712b75e18c7d65e520cbc199d26d1765732a547e14",
            "salt":"ioDZsC7b8RfgH1XYAnF7+A=="
        }
    ],
    "isDefault":false,
    "label":"741e71ff",
    "lock":false,
    "ontid":"did:ont:ALcwsJqdFyPr4sd1bnS5bdnaBYDgfmCsBd"
}
 */
    }


}
