package com.ontology.sourcing;

import ch.qos.logback.classic.Logger;
import com.github.ontio.common.Helper;
import com.github.ontio.core.ontid.Attribute;
import com.github.ontio.crypto.SignatureScheme;
import com.github.ontio.sdk.info.IdentityInfo;
import com.github.ontio.sdk.wallet.Identity;
import com.google.gson.Gson;
import com.ontology.sourcing.mapper.EventMapper;
import com.ontology.sourcing.model.ddo.DDOPojo;
import com.ontology.sourcing.model.ddo.identity.OntidPojo;
import com.ontology.sourcing.service.util.ChainService;
import com.ontology.sourcing.util.GlobalVariable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "file:/Volumes/Data/_work/201802_Ontology/ONTSouring/ont-sourcing/config/application-local.properties")
public class IdentityTests {

    //
    private Logger logger = (Logger) LoggerFactory.getLogger(IdentityTests.class);
    private Gson gson = new Gson();

    //
    @Autowired
    ChainService chainService;

    // 付款的数字钱包
    private com.github.ontio.account.Account payerAccount = GlobalVariable.getInstanceOfAccount("6a62d116e416246f974229eee7d1b0894d8c2ab70446856e85e35b7f5d37adef");

    // 要操作的实物的数字身份
    private String entityIdentityStr = "{\n" + "  \"controls\": [\n" + "    {\n" + "      \"codeAddr\": \"ALiQf85m87bggdguYGEBaA4m4YTo764YDW\",\n" + "      \"algorithm\": \"ECDSA\",\n" + "      \"enc-alg\": \"aes-256-gcm\",\n" + "      \"hash\": \"sha256\",\n" + "      \"id\": \"keys-1\",\n" + "      \"key\": \"z+lASc14/m4rlkVqPruD51uPuMfnBdVI2nXhvGG/543KsF4KMGvRWkbQ1SFNEJ1B\",\n" + "      \"parameters\": {\n" + "        \"curve\": \"P-256\"\n" + "      },\n" + "      \"publicKey\": \"03854ec502bcd514ae86329ea848a87e001b3b90500c8fabad2f167d80cf64932b\",\n" + "      \"salt\": \"Ih0CIOgA1Q1aC5Fhj5DV1w==\"\n" + "    }\n" + "  ],\n" + "  \"isDefault\": false,\n" + "  \"label\": \"5a7f1346\",\n" + "  \"lock\": false,\n" + "  \"ontid\": \"did:ont:ALiQf85m87bggdguYGEBaA4m4YTo764YDW\"\n" + "}";
    private String entityIdentityPassword = "i1";
    private OntidPojo entityIdentity;

    // Control的数字身份
    private String controlIdentity1Str = "{\n" + "  \"controls\": [\n" + "    {\n" + "      \"codeAddr\": \"AGUqccDaZyiP3YxmpxTSTHUvkjX7SPCn92\",\n" + "      \"algorithm\": \"ECDSA\",\n" + "      \"enc-alg\": \"aes-256-gcm\",\n" + "      \"hash\": \"sha256\",\n" + "      \"id\": \"keys-1\",\n" + "      \"key\": \"H1B8+onxoNMtW0cdYykY6REezviB3hef8BeCXwMwogbAupwiFiKqBwV/4+/FzicM\",\n" + "      \"parameters\": {\n" + "        \"curve\": \"P-256\"\n" + "      },\n" + "      \"publicKey\": \"039e628ffdcdb59e69411c1265a5b154ebf8195589bc94705fb2aaca34aa3d852a\",\n" + "      \"salt\": \"Ggb1+pBA3aW+DCnjV7jzxQ==\"\n" + "    }\n" + "  ],\n" + "  \"isDefault\": false,\n" + "  \"label\": \"ae907a60\",\n" + "  \"lock\": false,\n" + "  \"ontid\": \"did:ont:AGUqccDaZyiP3YxmpxTSTHUvkjX7SPCn92\"\n" + "}";
    private String controlIdentity1Password = "p1";
    private OntidPojo controlIdentity1;


    @Autowired
    EventMapper eventMapper;

    public IdentityTests() {

        //
        try {
            //
            this.entityIdentity = gson.fromJson(this.entityIdentityStr, OntidPojo.class);
            //            System.out.println(this.entityIdentity);
            //
            this.controlIdentity1 = gson.fromJson(this.controlIdentity1Str, OntidPojo.class);
            //            System.out.println(this.controlIdentity1);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Test
    public void createIdentity() {

        try {
            // 创建 identity1
            Identity identity1 = chainService.ontSdk.getWalletMgr().createIdentity("i1");
            System.out.println(identity1);
/*
{
  "controls": [
    {
      "codeAddr": "ATjTrm7cJwK6yYA6SpQXcuyeoZbHaek1no",
      "algorithm": "ECDSA",
      "enc-alg": "aes-256-gcm",
      "hash": "sha256",
      "id": "keys-1",
      "key": "M0doldsF8VmZkIiE7THuoVy33I5oG4DCzRtS/77tot1UgMvnxBhkzr45CqcmWQIL",
      "parameters": {
        "curve": "P-256"
      },
      "publicKey": "03c11484d066d8ac8e6d775ce7b28c75fa7e7632fd9ddef8bc5170cd2c14fafc2c",
      "salt": "ciJOz+BSoKeNDvr1ubBMRA=="
    }
  ],
  "isDefault": true,
  "label": "d86e4fa8",
  "lock": false,
  "ontid": "did:ont:ATjTrm7cJwK6yYA6SpQXcuyeoZbHaek1no"
}
 */

            Identity identity2 = chainService.ontSdk.getWalletMgr().createIdentity("i2");
            System.out.println(identity2);
/*
{
  "controls": [
    {
      "codeAddr": "AdFY9DejFGdur8hAvwABu5r9DDHGo1XWWx",
      "algorithm": "ECDSA",
      "enc-alg": "aes-256-gcm",
      "hash": "sha256",
      "id": "keys-1",
      "key": "BXEtxRsbsJFFwoxWmow0XHwwvhz9cMgmfsTgbryjqbGXhZeuV4F6PZ9DpMu0s9n3",
      "parameters": {
        "curve": "P-256"
      },
      "publicKey": "0344382d1e1087863fff04f0cd77afca769ae16c5836c77ff44aa2cda286fbaed7",
      "salt": "ObfkNB8LM1oPUSfcYSaC3A=="
    }
  ],
  "isDefault": true,
  "label": "711b5671",
  "lock": false,
  "ontid": "did:ont:AdFY9DejFGdur8hAvwABu5r9DDHGo1XWWx"
}
 */
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Test
    public void createEntityIdentityAndRegisterOnBlockChain() {
        //
        Map<String, String> map = createIdentityAndRegisterOnBlockChain("i1");
/*
{
  "controls": [
    {
      "codeAddr": "ALiQf85m87bggdguYGEBaA4m4YTo764YDW",
      "algorithm": "ECDSA",
      "enc-alg": "aes-256-gcm",
      "hash": "sha256",
      "id": "keys-1",
      "key": "z+lASc14/m4rlkVqPruD51uPuMfnBdVI2nXhvGG/543KsF4KMGvRWkbQ1SFNEJ1B",
      "parameters": {
        "curve": "P-256"
      },
      "publicKey": "03854ec502bcd514ae86329ea848a87e001b3b90500c8fabad2f167d80cf64932b",
      "salt": "Ih0CIOgA1Q1aC5Fhj5DV1w=="
    }
  ],
  "isDefault": false,
  "label": "5a7f1346",
  "lock": false,
  "ontid": "did:ont:ALiQf85m87bggdguYGEBaA4m4YTo764YDW"
}
 */
    }

    private Map<String, String> createIdentityAndRegisterOnBlockChain(String i_password) {

        //
        Map<String, String> map = new HashMap<String, String>();

        // 创建 identity1
        Identity identity1 = null;
        try {
            identity1 = chainService.ontSdk.getWalletMgr().createIdentity(i_password);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        map.put("identity", String.valueOf(identity1));

        // 链上：注册 identity1
        try {
            String rsp = chainService.ontSdk.nativevm().ontId().sendRegister(identity1,
                                                                             i_password,
                                                                             payerAccount,
                                                                             chainService.ontSdk.DEFAULT_GAS_LIMIT,
                                                                             GlobalVariable.DEFAULT_GAS_PRICE);
            map.put("txhash", rsp);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        //
        String ddoStr = readDDO(identity1.ontid);
        map.put("ddo", ddoStr);

        //
        return map;

    }

    @Test
    public void createIdentityFromPrikeyAndRegisterOnBlockChain() {

        //
        Map<String, String> map = new HashMap<String, String>();

        String passphrase = "123456";

        // 创建 identity1
        Identity identity1 = null;
        try {
            //
            String prikey = "3fb33f6ece9b9cba55936965c0eef8c15819bc74d7588c1feb3217b8948e1854";
            //
            identity1 = chainService.ontSdk.getWalletMgr().createIdentityFromPriKey(passphrase, prikey);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        map.put("identity", String.valueOf(identity1));

        // 链上：注册 identity1
        try {
            String rsp = chainService.ontSdk.nativevm().ontId().sendRegister(identity1,
                                                                             passphrase,
                                                                             payerAccount,
                                                                             chainService.ontSdk.DEFAULT_GAS_LIMIT,
                                                                             GlobalVariable.DEFAULT_GAS_PRICE);
            map.put("txhash", rsp);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        //
        String ddoStr = readDDO(identity1.ontid);
        map.put("ddo", ddoStr);

        //
        System.out.println(map);
        // {identity={"controls":[{"address":"AYcZZYxgAVw7uoizVJFGmEYEdbFomeXhEG","algorithm":"ECDSA","enc-alg":"aes-256-gcm","hash":"sha256","id":"keys-1","key":"2GnmuxtCCIuWWD/S80zrmGGX/p32S0LLQ1ZNvwRp4NoBlSmL/haNPM4L+FZoxrSR","parameters":{"curve":"P-256"},"publicKey":"02c1c8d9ee6f5c3bbc901aa00406fe819b4cc5bb85e605c3970a97ac400416b764","salt":"wc6IbOKu3IsBAAIpFxcnGA=="}],"isDefault":false,"label":"7c3c4d86","lock":false,"ontid":"did:ont:AYcZZYxgAVw7uoizVJFGmEYEdbFomeXhEG"}, ddo={"Attributes":[],"OntId":"did:ont:AYcZZYxgAVw7uoizVJFGmEYEdbFomeXhEG","Owners":[{"Type":"ECDSA","Curve":"P256","Value":"02c1c8d9ee6f5c3bbc901aa00406fe819b4cc5bb85e605c3970a97ac400416b764","PubKeyId":"did:ont:AYcZZYxgAVw7uoizVJFGmEYEdbFomeXhEG#keys-1"}]}, txhash=84384591ae4363882465cff9a924b9d1d92668c110aa03065deaa3ac30be0cba}

    }

    @Test
    public void updateIdentityAttribute() {

        //
        readDDO(entityIdentity.getOntid());
/*
{
  "Attributes": [],
  "OntId": "did:ont:ALiQf85m87bggdguYGEBaA4m4YTo764YDW",
  "Owners": [
    {
      "Type": "ECDSA",
      "Curve": "P256",
      "Value": "03854ec502bcd514ae86329ea848a87e001b3b90500c8fabad2f167d80cf64932b",
      "PubKeyId": "did:ont:ALiQf85m87bggdguYGEBaA4m4YTo764YDW#keys-1"
    }
  ]
}
 */
        //
        try {
            //
            Attribute[] attributes = new Attribute[1];
            attributes[0] = new Attribute("key1".getBytes(), "String".getBytes(), "value1".getBytes());

            //
            String rsp = chainService.ontSdk.nativevm().ontId().sendAddAttributes(entityIdentity.getOntid(),
                                                                                  entityIdentityPassword,
                                                                                  entityIdentity.getControls().get(0).getSalt(),
                                                                                  attributes,
                                                                                  this.payerAccount,
                                                                                  chainService.ontSdk.DEFAULT_GAS_LIMIT,
                                                                                  500);
            System.out.println(rsp);
            //
            readDDO(entityIdentity.getOntid());
/*
{
  "Attributes": [
    {
      "Type": "String",
      "Value": "value1",
      "Key": "key1"
    }
  ],
  "OntId": "did:ont:ALiQf85m87bggdguYGEBaA4m4YTo764YDW",
  "Owners": [
    {
      "Type": "ECDSA",
      "Curve": "P256",
      "Value": "03854ec502bcd514ae86329ea848a87e001b3b90500c8fabad2f167d80cf64932b",
      "PubKeyId": "did:ont:ALiQf85m87bggdguYGEBaA4m4YTo764YDW#keys-1"
    }
  ]
}
 */

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Test
    public void deleteIdentityAttribute() {
        //
        readDDO(entityIdentity.getOntid());
/*
{
  "Attributes": [
    {
      "Type": "String",
      "Value": "value1",
      "Key": "key1"
    }
  ],
  "OntId": "did:ont:ALiQf85m87bggdguYGEBaA4m4YTo764YDW",
  "Owners": [
    {
      "Type": "ECDSA",
      "Curve": "P256",
      "Value": "03854ec502bcd514ae86329ea848a87e001b3b90500c8fabad2f167d80cf64932b",
      "PubKeyId": "did:ont:ALiQf85m87bggdguYGEBaA4m4YTo764YDW#keys-1"
    }
  ]
}
 */
        //
        try {
            String rsp = chainService.ontSdk.nativevm().ontId().sendRemoveAttribute(entityIdentity.getOntid(),
                                                                                    entityIdentityPassword,
                                                                                    entityIdentity.getControls().get(0).getSalt(),
                                                                                    "key1",
                                                                                    payerAccount,
                                                                                    chainService.ontSdk.DEFAULT_GAS_LIMIT,
                                                                                    500);
            System.out.println(rsp);
            // 33d7d9df0e34dda70f08681b3655033236c8750dfeefd83bc99e16eeb579d691
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        //
        readDDO(entityIdentity.getOntid());
/*
{
  "Attributes": [],
  "OntId": "did:ont:ALiQf85m87bggdguYGEBaA4m4YTo764YDW",
  "Owners": [
    {
      "Type": "ECDSA",
      "Curve": "P256",
      "Value": "03854ec502bcd514ae86329ea848a87e001b3b90500c8fabad2f167d80cf64932b",
      "PubKeyId": "did:ont:ALiQf85m87bggdguYGEBaA4m4YTo764YDW#keys-1"
    }
  ]
}
 */
    }

    //
    private String readDDO(String ontid) {
        //
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        //
        String ddoStr = "";
        //
        try {
            ddoStr = chainService.ontSdk.nativevm().ontId().sendGetDDO(ontid);
            System.out.println(ddoStr);
            DDOPojo ddoPojo = gson.fromJson(ddoStr, DDOPojo.class);
            System.out.println(ddoPojo);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return ddoStr;
    }

    @Test
    public void createControlIdentityAndRegisterOnBlockChain() {
        //
        Map<String, String> map = createIdentityAndRegisterOnBlockChain("p1");
        //
        System.out.println(map.get("identity"));
/*
{
  "controls": [
    {
      "codeAddr": "AGUqccDaZyiP3YxmpxTSTHUvkjX7SPCn92",
      "algorithm": "ECDSA",
      "enc-alg": "aes-256-gcm",
      "hash": "sha256",
      "id": "keys-1",
      "key": "H1B8+onxoNMtW0cdYykY6REezviB3hef8BeCXwMwogbAupwiFiKqBwV/4+/FzicM",
      "parameters": {
        "curve": "P-256"
      },
      "publicKey": "039e628ffdcdb59e69411c1265a5b154ebf8195589bc94705fb2aaca34aa3d852a",
      "salt": "Ggb1+pBA3aW+DCnjV7jzxQ=="
    }
  ],
  "isDefault": false,
  "label": "ae907a60",
  "lock": false,
  "ontid": "did:ont:AGUqccDaZyiP3YxmpxTSTHUvkjX7SPCn92"
}
 */
        System.out.println(map.get("txhash"));
        //256cc59f1eabf4542c723ba0a9587a567d47ec00308ae2495ccef08781f6832a

        System.out.println(map.get("ddo"));
/*
{
  "Attributes": [],
  "OntId": "did:ont:AGUqccDaZyiP3YxmpxTSTHUvkjX7SPCn92",
  "Owners": [
    {
      "Type": "ECDSA",
      "Curve": "P256",
      "Value": "039e628ffdcdb59e69411c1265a5b154ebf8195589bc94705fb2aaca34aa3d852a",
      "PubKeyId": "did:ont:AGUqccDaZyiP3YxmpxTSTHUvkjX7SPCn92#keys-1"
    }
  ]
}
 */
    }

    @Test
    public void updateEntityIdentityControls() {

        //
        readDDO(entityIdentity.getOntid());
/*
{
  "Attributes": [],
  "OntId": "did:ont:ALiQf85m87bggdguYGEBaA4m4YTo764YDW",
  "Owners": [
    {
      "Type": "ECDSA",
      "Curve": "P256",
      "Value": "03854ec502bcd514ae86329ea848a87e001b3b90500c8fabad2f167d80cf64932b",
      "PubKeyId": "did:ont:ALiQf85m87bggdguYGEBaA4m4YTo764YDW#keys-1"
    }
  ]
}
 */

        //
        IdentityInfo controlIdentity1Info = new IdentityInfo();
        try {
            controlIdentity1Info = chainService.ontSdk.getWalletMgr().getIdentityInfo(controlIdentity1.getOntid(),
                                                                                      controlIdentity1Password,
                                                                                      controlIdentity1.getControls().get(0).getSalt());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        try {
            String rsp = chainService.ontSdk.nativevm().ontId().sendAddPubKey(entityIdentity.getOntid(),
                                                                              entityIdentityPassword,
                                                                              entityIdentity.getControls().get(0).getSalt(),
                                                                              controlIdentity1Info.pubkey,
                                                                              payerAccount,
                                                                              chainService.ontSdk.DEFAULT_GAS_LIMIT,
                                                                              GlobalVariable.DEFAULT_GAS_PRICE);
            //
            System.out.println(rsp);
            // 1e7356035a7bbc080cc5440542236b363036438b3af66cdd3195149c35df7032
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        //
        readDDO(entityIdentity.getOntid());
/*
{
  "Attributes": [],
  "OntId": "did:ont:ALiQf85m87bggdguYGEBaA4m4YTo764YDW",
  "Owners": [
    {
      "Type": "ECDSA",
      "Curve": "P256",
      "Value": "03854ec502bcd514ae86329ea848a87e001b3b90500c8fabad2f167d80cf64932b",
      "PubKeyId": "did:ont:ALiQf85m87bggdguYGEBaA4m4YTo764YDW#keys-1"
    },
    {
      "Type": "ECDSA",
      "Curve": "P256",
      "Value": "039e628ffdcdb59e69411c1265a5b154ebf8195589bc94705fb2aaca34aa3d852a",
      "PubKeyId": "did:ont:ALiQf85m87bggdguYGEBaA4m4YTo764YDW#keys-2"
    }
  ]
}
 */
    }

    @Test
    public void updateEntityIdentityAttributeByControl() {

        //
        readDDO(entityIdentity.getOntid());
/*
{
  "Attributes": [
    {
      "Type": "String",
      "Value": "value1",
      "Key": "key1"
    }
  ],
  "OntId": "did:ont:ALiQf85m87bggdguYGEBaA4m4YTo764YDW",
  "Owners": [
    {
      "Type": "ECDSA",
      "Curve": "P256",
      "Value": "03854ec502bcd514ae86329ea848a87e001b3b90500c8fabad2f167d80cf64932b",
      "PubKeyId": "did:ont:ALiQf85m87bggdguYGEBaA4m4YTo764YDW#keys-1"
    },
    {
      "Type": "ECDSA",
      "Curve": "P256",
      "Value": "039e628ffdcdb59e69411c1265a5b154ebf8195589bc94705fb2aaca34aa3d852a",
      "PubKeyId": "did:ont:ALiQf85m87bggdguYGEBaA4m4YTo764YDW#keys-2"
    }
  ]
}
 */

        //
        try {
            //
            Attribute[] attributes = new Attribute[1];
            attributes[0] = new Attribute("key1".getBytes(), "String".getBytes(), "value1".getBytes());

            //
            IdentityInfo controlIdentity1Info = chainService.ontSdk.getWalletMgr().getIdentityInfo(controlIdentity1.getOntid(),
                                                                                                   controlIdentity1Password,
                                                                                                   controlIdentity1.getControls().get(0).getSalt());

            System.out.println(controlIdentity1Info.encryptedPrikey);
            // H1B8+onxoNMtW0cdYykY6REezviB3hef8BeCXwMwogbAupwiFiKqBwV/4+/FzicM

            //
            String priKey = com.github.ontio.account.Account.getGcmDecodedPrivateKey(controlIdentity1Info.encryptedPrikey,
                                                                                     controlIdentity1Password,
                                                                                     controlIdentity1.getControls().get(0).getAddress(),
                                                                                     controlIdentity1.getControls().get(0).getSalt(),
                                                                                     16384,
                                                                                     SignatureScheme.SHA256WITHECDSA);
            System.out.println(priKey);
            // 6134efe790e110168c697b4e868081bfcd3e055aac412d5a3600fb45a29860b2

            //
            com.github.ontio.account.Account controlAccount = new com.github.ontio.account.Account(Helper.hexToBytes(priKey), SignatureScheme.SHA256WITHECDSA);

            //
            String rsp = chainService.ontSdk.nativevm().ontId().sendAddAttributes(entityIdentity.getOntid(),
                                                                                  controlAccount,
                                                                                  attributes,
                                                                                  this.payerAccount,
                                                                                  chainService.ontSdk.DEFAULT_GAS_LIMIT,
                                                                                  GlobalVariable.DEFAULT_GAS_PRICE);
            System.out.println(rsp);
            // ae86ed81c2adb1a50ecdad7c4e7927f217633ff6f3590270a221522078dad994

            //
            readDDO(entityIdentity.getOntid());
/*
{
  "Attributes": [
    {
      "Type": "String",
      "Value": "value1",
      "Key": "key1"
    }
  ],
  "OntId": "did:ont:ALiQf85m87bggdguYGEBaA4m4YTo764YDW",
  "Owners": [
    {
      "Type": "ECDSA",
      "Curve": "P256",
      "Value": "03854ec502bcd514ae86329ea848a87e001b3b90500c8fabad2f167d80cf64932b",
      "PubKeyId": "did:ont:ALiQf85m87bggdguYGEBaA4m4YTo764YDW#keys-1"
    },
    {
      "Type": "ECDSA",
      "Curve": "P256",
      "Value": "039e628ffdcdb59e69411c1265a5b154ebf8195589bc94705fb2aaca34aa3d852a",
      "PubKeyId": "did:ont:ALiQf85m87bggdguYGEBaA4m4YTo764YDW#keys-2"
    }
  ]
}
 */

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

}
