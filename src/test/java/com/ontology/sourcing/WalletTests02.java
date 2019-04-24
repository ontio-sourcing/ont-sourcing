package com.ontology.sourcing;

import com.alibaba.fastjson.JSON;
import com.github.ontio.OntSdk;
import com.github.ontio.common.WalletQR;
import com.github.ontio.sdk.exception.SDKException;
import com.github.ontio.sdk.wallet.Account;
import com.github.ontio.sdk.wallet.Scrypt;
import com.google.gson.Gson;
import com.ontology.sourcing.utils.GlobalVariable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WalletTests02 {

    private Gson gson = new Gson();

    //
    private OntSdk ontSdk = GlobalVariable.getOntSdk("http://polaris1.ont.io", "/Volumes/Data/_work/201802_Ontology/ONTSouring/ont-sourcing/config/wallet.json");

    // 付款的数字钱包
    private com.github.ontio.account.Account payerAccount = GlobalVariable.getInstanceOfAccount("6a62d116e416246f974229eee7d1b0894d8c2ab70446856e85e35b7f5d37adef");
    
    // cool orbit donor annual fix achieve rigid rival lumber obscure cliff castle
    // 51437e5a77201e8ca68f3c2fa474b2373e38da6ed31a7bf23e1594b49406b13e
    @Test
    public void example01() throws Exception {
        //
        String passphrase = "999999";
        String prikey = "51437e5a77201e8ca68f3c2fa474b2373e38da6ed31a7bf23e1594b49406b13e";
        String label = "test_label";
        //
        Account acct = ontSdk.getWalletMgr().createAccountFromPriKey(label, passphrase, prikey);
        System.out.println(acct);
/*
{
    "codeAddr":"AZsDe5NpCZ4TCyGyaLGrKpoSz3zFY5QrSM",
    "algorithm":"ECDSA",
    "enc-alg":"aes-256-gcm",
    "hash":"sha256",
    "isDefault":true,
    "key":"bLGevo86Mqv9RImT0RuZQEgWJqWIVkRgmUQfri6P/E3x4Bw0BPUhdzaRRRpu+TIS",
    "label":"test_label",
    "lock":false,
    "parameters":{
        "curve":"P-256"
    },
    "publicKey":"0249044cbc47802cf78c55c277609a28679fb14fb6d4814d8601f4c0414a32a78d",
    "salt":"2a7rH4Z/r1Ou/PcHQA8vTA==",
    "signatureScheme":"SHA256withECDSA"
}
 */
        //
        ontSdk.getWalletMgr().writeWallet();
/*
// wallet_test.json
{
  "accounts": [
    {
      "codeAddr": "AZsDe5NpCZ4TCyGyaLGrKpoSz3zFY5QrSM",
      "algorithm": "ECDSA",
      "enc-alg": "aes-256-gcm",
      "hash": "sha256",
      "isDefault": true,
      "key": "bLGevo86Mqv9RImT0RuZQEgWJqWIVkRgmUQfri6P/E3x4Bw0BPUhdzaRRRpu+TIS",
      "label": "test_label",
      "lock": false,
      "parameters": {
        "curve": "P-256"
      },
      "publicKey": "0249044cbc47802cf78c55c277609a28679fb14fb6d4814d8601f4c0414a32a78d",
      "salt": "2a7rH4Z/r1Ou/PcHQA8vTA==",
      "signatureScheme": "SHA256withECDSA"
    }
  ],
  "createTime": "2019-03-29T15:43:45Z",
  "defaultAccountAddress": "AZsDe5NpCZ4TCyGyaLGrKpoSz3zFY5QrSM",
  "defaultOntid": "",
  "identities": [],
  "name": "com.github.ontio",
  "scrypt": {
    "dkLen": 64,
    "n": 16384,
    "p": 8,
    "r": 8
  },
  "version": "1.0"
}
 */
    }

    // 通过 钱包地址、钱包文件 获取 数字资产账户
    @Test
    public void example02() throws SDKException {

        // 查询 钱包文件 中不存在的 地址
        Account account1 = ontSdk.getWalletMgr().getWallet().getAccount("ALcwsJqdFyPr4sd1bnS5bdnaBYDgfmCsBd");
        System.out.println(account1);  // null

        //
        Account account2 = ontSdk.getWalletMgr().getWallet().getAccount("AZsDe5NpCZ4TCyGyaLGrKpoSz3zFY5QrSM");
        System.out.println(account2);
/*
{
    "codeAddr":"AZsDe5NpCZ4TCyGyaLGrKpoSz3zFY5QrSM",
    "algorithm":"ECDSA",
    "enc-alg":"aes-256-gcm",
    "hash":"sha256",
    "isDefault":true,
    "key":"bLGevo86Mqv9RImT0RuZQEgWJqWIVkRgmUQfri6P/E3x4Bw0BPUhdzaRRRpu+TIS",
    "label":"test_label",
    "lock":false,
    "parameters":{
        "curve":"P-256"
    },
    "publicKey":"0249044cbc47802cf78c55c277609a28679fb14fb6d4814d8601f4c0414a32a78d",
    "salt":"2a7rH4Z/r1Ou/PcHQA8vTA==",
    "signatureScheme":"SHA256withECDSA"
}
 */
    }


    // 通过 keystore、passphrase，获取 私钥
    @Test
    public void example04() throws Exception {
        //
        Account account = ontSdk.getWalletMgr().getWallet().getAccount("AZsDe5NpCZ4TCyGyaLGrKpoSz3zFY5QrSM");

        // 通过 数字资产账户 获取 keystore
        Scrypt scrypt = new Scrypt();
        Map keystore = WalletQR.exportAccountQRCode(scrypt, account);
        System.out.println(JSON.toJSONString(keystore));
/*
{
    "codeAddr":"AZsDe5NpCZ4TCyGyaLGrKpoSz3zFY5QrSM",
    "salt":"2a7rH4Z/r1Ou/PcHQA8vTA==",
    "label":"test_label",
    "type":"A",
    "parameters":{
        "curve":"P-256"
    },
    "scrypt":{
        "dkLen":64,
        "n":16384,
        "p":8,
        "r":8
    },
    "key":"bLGevo86Mqv9RImT0RuZQEgWJqWIVkRgmUQfri6P/E3x4Bw0BPUhdzaRRRpu+TIS",
    "algorithm":"ECDSA"
}
 */
        //
        String passphrase = "999999";
        //
        String privateKey = WalletQR.getPriKeyFromQrCode(JSON.toJSONString(keystore), passphrase);
        System.out.println(privateKey);
        // 51437e5a77201e8ca68f3c2fa474b2373e38da6ed31a7bf23e1594b49406b13e
    }

    // 从 钱包文件 读取 数字资产账号
    @Test
    public void example05() throws SDKException {
        //
        List<Account> accounts = ontSdk.getWalletMgr().getWallet().getAccounts();
        Account account = accounts.get(0);
        String addr = account.address;
        //
        System.out.println(addr);
        // AGbL9NGxBosqRxiD34ZXPHigeti5AZBnfP
    }

}
