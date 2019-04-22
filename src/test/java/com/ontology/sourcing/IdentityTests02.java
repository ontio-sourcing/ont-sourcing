package com.ontology.sourcing;

import com.github.ontio.OntSdk;
import com.github.ontio.sdk.info.IdentityInfo;
import com.github.ontio.sdk.wallet.Identity;
import com.ontology.sourcing.utils.GlobalVariable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Base64;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IdentityTests02 {

    //
    private OntSdk ontSdk = GlobalVariable.getOntSdk("http://polaris1.ont.io", "/Volumes/Data/_work/201802_Ontology/ONTSouring/ont-sourcing/config/wallet.json");

    // 付款的数字钱包
    private com.github.ontio.account.Account payerAccount = GlobalVariable.getInstanceOfAccount("6a62d116e416246f974229eee7d1b0894d8c2ab70446856e85e35b7f5d37adef");

    // 同一个 私钥，对应着 一套 数字资产账户 和 数字身份账户
    // 写入 钱包文件
    @Test
    public void example01() throws Exception {
        //
        String passphrase = "123456";
        String prikey = "0fb11f6ece9b9cba55936965c0eef8c15819bc74d7588c1feb3217b8948e1854";
        //
        Identity identity = ontSdk.getWalletMgr().createIdentityFromPriKey(passphrase, prikey);
        System.out.println(identity);
/*
{
    "controls":[
        {
            "address":"ALcwsJqdFyPr4sd1bnS5bdnaBYDgfmCsBd",
            "algorithm":"ECDSA",
            "enc-alg":"aes-256-gcm",
            "hash":"sha256",
            "id":"keys-1",
            "key":"BECnxqHuiNAI6YOl1eQmg2LLkYkX+LQGGknxKWI2E4WZuViCQ47MFGzy9sBqkd3M",
            "parameters":{
                "curve":"P-256"
            },
            "publicKey":"03088ae9cc9116e49e8c1b5b712b75e18c7d65e520cbc199d26d1765732a547e14",
            "salt":"lQPiKF2I61nKUJxpTugDdA=="
        }
    ],
    "isDefault":true,
    "label":"cf404cec",
    "lock":false,
    "ontid":"did:ont:ALcwsJqdFyPr4sd1bnS5bdnaBYDgfmCsBd"
}
 */
        //
        ontSdk.getWalletMgr().writeWallet();
/*
// wallet_test.json
{
  "accounts": [],
  "createTime": "2019-03-29T16:50:47Z",
  "defaultAccountAddress": "",
  "defaultOntid": "did:ont:ALcwsJqdFyPr4sd1bnS5bdnaBYDgfmCsBd",
  "identities": [
    {
      "controls": [
        {
          "address": "ALcwsJqdFyPr4sd1bnS5bdnaBYDgfmCsBd",
          "algorithm": "ECDSA",
          "enc-alg": "aes-256-gcm",
          "hash": "sha256",
          "id": "keys-1",
          "key": "BECnxqHuiNAI6YOl1eQmg2LLkYkX+LQGGknxKWI2E4WZuViCQ47MFGzy9sBqkd3M",
          "parameters": {
            "curve": "P-256"
          },
          "publicKey": "03088ae9cc9116e49e8c1b5b712b75e18c7d65e520cbc199d26d1765732a547e14",
          "salt": "lQPiKF2I61nKUJxpTugDdA=="
        }
      ],
      "isDefault": true,
      "label": "cf404cec",
      "lock": false,
      "ontid": "did:ont:ALcwsJqdFyPr4sd1bnS5bdnaBYDgfmCsBd"
    }
  ],
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

    // 从 钱包文件 读取 数字身份账号
    @Test
    public void example02() throws Exception {
        //
        String ontid = "did:ont:ALcwsJqdFyPr4sd1bnS5bdnaBYDgfmCsBd";
        String passphrase = "123456";

        //
        Identity identity = ontSdk.getWalletMgr().getWallet().getIdentity(ontid);
        System.out.println(identity);
/*
{
    "controls":[
        {
            "address":"ALcwsJqdFyPr4sd1bnS5bdnaBYDgfmCsBd",
            "algorithm":"ECDSA",
            "enc-alg":"aes-256-gcm",
            "hash":"sha256",
            "id":"keys-1",
            "key":"BECnxqHuiNAI6YOl1eQmg2LLkYkX+LQGGknxKWI2E4WZuViCQ47MFGzy9sBqkd3M",
            "parameters":{
                "curve":"P-256"
            },
            "publicKey":"03088ae9cc9116e49e8c1b5b712b75e18c7d65e520cbc199d26d1765732a547e14",
            "salt":"lQPiKF2I61nKUJxpTugDdA=="
        }
    ],
    "isDefault":true,
    "label":"cf404cec",
    "lock":false,
    "ontid":"did:ont:ALcwsJqdFyPr4sd1bnS5bdnaBYDgfmCsBd"
}
 */
    }

    @Test
    public void example03() throws Exception {
        //
        String address = "ALcwsJqdFyPr4sd1bnS5bdnaBYDgfmCsBd";
        String passphrase = "123456";
        String encryptedPrikey = "BECnxqHuiNAI6YOl1eQmg2LLkYkX+LQGGknxKWI2E4WZuViCQ47MFGzy9sBqkd3M";
        byte[] salt = Base64.getDecoder().decode("lQPiKF2I61nKUJxpTugDdA==");

        //
        Identity identity = ontSdk.getWalletMgr().importIdentity(encryptedPrikey, passphrase, salt, address);
        System.out.println(identity);
/*
{
    "controls":[
        {
            "address":"ALcwsJqdFyPr4sd1bnS5bdnaBYDgfmCsBd",
            "algorithm":"ECDSA",
            "enc-alg":"aes-256-gcm",
            "hash":"sha256",
            "id":"keys-1",
            "key":"BECnxqHuiNAI6YOl1eQmg2LLkYkX+LQGGknxKWI2E4WZuViCQ47MFGzy9sBqkd3M",
            "parameters":{
                "curve":"P-256"
            },
            "publicKey":"03088ae9cc9116e49e8c1b5b712b75e18c7d65e520cbc199d26d1765732a547e14",
            "salt":"lQPiKF2I61nKUJxpTugDdA=="
        }
    ],
    "isDefault":true,
    "label":"cf404cec",
    "lock":false,
    "ontid":"did:ont:ALcwsJqdFyPr4sd1bnS5bdnaBYDgfmCsBd"
}
 */
        ontSdk.getWalletMgr().writeWallet();
/*
// wallet_test_01.json
{
  "accounts": [],
  "createTime": "2019-03-29T17:13:23Z",
  "defaultAccountAddress": "",
  "defaultOntid": "did:ont:ALcwsJqdFyPr4sd1bnS5bdnaBYDgfmCsBd",
  "identities": [
    {
      "controls": [
        {
          "address": "ALcwsJqdFyPr4sd1bnS5bdnaBYDgfmCsBd",
          "algorithm": "ECDSA",
          "enc-alg": "aes-256-gcm",
          "hash": "sha256",
          "id": "keys-1",
          "key": "BECnxqHuiNAI6YOl1eQmg2LLkYkX+LQGGknxKWI2E4WZuViCQ47MFGzy9sBqkd3M",
          "parameters": {
            "curve": "P-256"
          },
          "publicKey": "03088ae9cc9116e49e8c1b5b712b75e18c7d65e520cbc199d26d1765732a547e14",
          "salt": "lQPiKF2I61nKUJxpTugDdA=="
        }
      ],
      "isDefault": true,
      "label": "2c97959f",
      "lock": false,
      "ontid": "did:ont:ALcwsJqdFyPr4sd1bnS5bdnaBYDgfmCsBd"
    }
  ],
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

    // 从 钱包文件 获取 公钥 等 identityInfo
    @Test
    public void example04() throws Exception {
        //
        String ontid = "did:ont:ALcwsJqdFyPr4sd1bnS5bdnaBYDgfmCsBd";
        String passphrase = "123456";
        byte[] salt = Base64.getDecoder().decode("lQPiKF2I61nKUJxpTugDdA==");
        //
        IdentityInfo identityInfo = ontSdk.getWalletMgr().getIdentityInfo(ontid, passphrase, salt);
        System.out.println(identityInfo);
/*
{
    "addressU160":"b4dcce8a892eeb5b8804d9187c394f0d9e682835",
    "encryptedPrikey":"BECnxqHuiNAI6YOl1eQmg2LLkYkX+LQGGknxKWI2E4WZuViCQ47MFGzy9sBqkd3M",
    "ontid":"did:ont:ALcwsJqdFyPr4sd1bnS5bdnaBYDgfmCsBd",
    "pubkey":"03088ae9cc9116e49e8c1b5b712b75e18c7d65e520cbc199d26d1765732a547e14"
}
 */
    }

}
