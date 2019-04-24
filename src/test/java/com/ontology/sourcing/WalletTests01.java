package com.ontology.sourcing;

import com.github.ontio.OntSdk;
import com.github.ontio.common.Helper;
import com.github.ontio.crypto.MnemonicCode;
import com.github.ontio.crypto.SignatureScheme;
import com.github.ontio.sdk.wallet.Account;
import com.ontology.sourcing.utils.GlobalVariable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Base64;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WalletTests01 {

    //
    private OntSdk ontSdk = GlobalVariable.getOntSdk("http://polaris1.ont.io", "/Volumes/Data/_work/201802_Ontology/ONTSouring/ont-sourcing/config/wallet.json");

    // 付款的数字钱包
    private com.github.ontio.account.Account payerAccount = GlobalVariable.getInstanceOfAccount("6a62d116e416246f974229eee7d1b0894d8c2ab70446856e85e35b7f5d37adef");

    /**
     * 通过 Mnemonic phrase 创建钱包账户
     */
    @Test
    public void example01() {
        //generate Mnemonic Code
        String code = MnemonicCode.generateMnemonicCodesStr();
        System.out.println(code);
        // finger kitchen always promote torch section enough worth cigar ritual boost thought
    }

    @Test
    public void example02() throws Exception {
        String code = "finger kitchen always promote torch section enough worth cigar ritual boost thought";
        // getRecord private key from Mnemonic Code
        byte[] prikeyb = MnemonicCode.getPrikeyFromMnemonicCodesStrBip44(code);
        String prikey = Helper.toHexString(prikeyb);
        System.out.println(prikey);
        // 0fb11f6ece9b9cba55936965c0eef8c15819bc74d7588c1feb3217b8948e1854
    }

    // TODO 反过来由私钥能推出助记词吗？

    // 同一个 私钥，创建出来的 codeAddr 和 publicKey 都一样
    // passphrase 通过 getGcmDecodedPrivateKey 得到 私钥
    @Test
    public void example03_1() throws Exception {
        //
        String passphrase = "123456";
        String prikey = "0fb11f6ece9b9cba55936965c0eef8c15819bc74d7588c1feb3217b8948e1854";
        String label = "test_label_01";
        //
        Account acct = ontSdk.getWalletMgr().createAccountFromPriKey(label, passphrase, prikey);
        System.out.println(acct);
/*
{
    "codeAddr":"ALcwsJqdFyPr4sd1bnS5bdnaBYDgfmCsBd",
    "algorithm":"ECDSA",
    "enc-alg":"aes-256-gcm",
    "hash":"sha256",
    "isDefault":false,
    "key":"YfM2prI920MhudiqFvkJPb9pzq1iDHi8pe1i+rhEdcTAVvJ1OLo+xUDDuIgvZz/M",
    "label":"test_label_01",
    "lock":false,
    "parameters":{
        "curve":"P-256"
    },
    "publicKey":"03088ae9cc9116e49e8c1b5b712b75e18c7d65e520cbc199d26d1765732a547e14",
    "salt":"wvvZ52UxshLbQ0+MwAlT7g==",
    "signatureScheme":"SHA256withECDSA"
}
 */

        // 再次运行
/*
{
    "codeAddr":"ALcwsJqdFyPr4sd1bnS5bdnaBYDgfmCsBd",
    "algorithm":"ECDSA",
    "enc-alg":"aes-256-gcm",
    "hash":"sha256",
    "isDefault":false,
    "key":"27NFnZoQ9X1lCJK/shBNj3VgFK2dqRbfLGtWldEsrxBaIwv3mmXgWG7BBVqNhQB8",
    "label":"test_label_01",
    "lock":false,
    "parameters":{
        "curve":"P-256"
    },
    "publicKey":"03088ae9cc9116e49e8c1b5b712b75e18c7d65e520cbc199d26d1765732a547e14",
    "salt":"DmXuwvZbtZA2Xq1jINb27g==",
    "signatureScheme":"SHA256withECDSA"
}
 */
    }

    @Test
    public void example03_2() throws Exception {
        //
        String passphrase = "654321";
        String prikey = "0fb11f6ece9b9cba55936965c0eef8c15819bc74d7588c1feb3217b8948e1854";
        String label = "test_label_01";
        //
        Account acct = ontSdk.getWalletMgr().createAccountFromPriKey(label, passphrase, prikey);
        System.out.println(acct);
/*
{
    "codeAddr":"ALcwsJqdFyPr4sd1bnS5bdnaBYDgfmCsBd",
    "algorithm":"ECDSA",
    "enc-alg":"aes-256-gcm",
    "hash":"sha256",
    "isDefault":false,
    "key":"B7axc/9hQD6taeRopEIPxF8QKggmmnhpbLwTXHl+10ifm1zdEqN5aP37dkhcWLLP",
    "label":"test_label_01",
    "lock":false,
    "parameters":{
        "curve":"P-256"
    },
    "publicKey":"03088ae9cc9116e49e8c1b5b712b75e18c7d65e520cbc199d26d1765732a547e14",
    "salt":"sLyWi3xAOF0jW8itMdAfGQ==",
    "signatureScheme":"SHA256withECDSA"
}
 */
    }

    @Test
    public void example04() throws Exception {

        /*
        String encryptedPrikey = "YfM2prI920MhudiqFvkJPb9pzq1iDHi8pe1i+rhEdcTAVvJ1OLo+xUDDuIgvZz/M";
        String passphrase = "123456";
        String saltStr = "wvvZ52UxshLbQ0+MwAlT7g==";
        String codeAddr = "ALcwsJqdFyPr4sd1bnS5bdnaBYDgfmCsBd";
         */

        /*
        String encryptedPrikey = "B7axc/9hQD6taeRopEIPxF8QKggmmnhpbLwTXHl+10ifm1zdEqN5aP37dkhcWLLP";
        String passphrase = "654321";
        String saltStr = "sLyWi3xAOF0jW8itMdAfGQ==";
        String codeAddr = "ALcwsJqdFyPr4sd1bnS5bdnaBYDgfmCsBd";
        */

        String encryptedPrikey = "BXsX1+P+t1+5RMEe0nRYacE8aynm+9hpyJwkMu41uTCRRe4FBPPh4l2uI+ATC/Pf";
        String passphrase = "123456";
        String saltStr = "40QV2Qf2T1tDNs+1aEY3BQ==";
        String address = "AGbL9NGxBosqRxiD34ZXPHigeti5AZBnfP";

        //
        String prikey = getPrivateKey(encryptedPrikey, passphrase, saltStr, address);
        //
        System.out.println(prikey);
    }

    private String getPrivateKey(String encryptedPrikey, String passphrase, String saltStr, String address) throws Exception {
        //
        byte[] salt = Base64.getDecoder().decode(saltStr);
        //
        String privateKey = com.github.ontio.account.Account.getGcmDecodedPrivateKey(encryptedPrikey, passphrase, address, salt, 16384, SignatureScheme.SHA256WITHECDSA);
        //
        return privateKey;
    }

    @Test
    public void example05() {
        //
        Account account = ontSdk.getWalletMgr().getWallet().getAccount("ALcwsJqdFyPr4sd1bnS5bdnaBYDgfmCsBd");
        System.out.println(account);
        // null
        // 因为 ontSdk 没有设置 钱包文件，之前的操作都没有写入 钱包文件
    }
}
