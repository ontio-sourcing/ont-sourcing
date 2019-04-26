package com.ontology.sourcing;

import com.ontology.sourcing.utils.HttpUtil;
import com.ontology.sourcing.utils.sfl.CertUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SFLTests {


    String priKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKPVtfCAMctN7h2W\n" + "DI8X1MtHNOqbBk20AMQV0BQeyy0PNLDTrT7jd9YDSgb41bpijWSGF+uNCMrX1/Hw\n" + "KZ56zUclcfUHs/7TEW1vNu+rLsAY5EPaMS2NDatRRE9K+oFG4aCpPohmod9ABIHv\n" + "DBOQmvsbm8U4WuDxNQChEXF2ASBtAgMBAAECgYAW8kjAPYQ74FdYZ3qs0/6m1ftQ\n" + "XJQeb/angBKbrtBwKDAOECe0pjFTlTRaE6IDr+fzy2AwSpmPp3mEVknN+48ijWq4\n" + "LzyQgnyhE+mSc9N/+UY6L0p28R3IV1TdZty7Gju8Tx0Y+yTKBz3RDvqR7e0eyF47\n" + "NoXgTNI/UbqjV/04VQJBANai8a86akq1gQNuG9E6xA6aVTdNe2U7mKRrZGFEw6I6\n" + "s/qZT3QlnfBrp9RTRCEmTeDyII2xB9rL/D3fNwDQeFsCQQDDaHk8nryGbkmAfQp5\n" + "7hPWIR3d+D6CKcYAyivgMiFLU2EukHhHMWTs3ydWOJqK5dyEPEV1OWbGoVm7E/dI\n" + "IeTXAkA8bzLn6OXG1WZXJei1N/u+NNxrruhgr7AiE473CFqhUjAdDo/dnz3iVR2z\n" + "XiKfxt5EyUg7PS59/9OtRC2DkC9jAkAYBq5xb6sKdGEBNV6mY0l5GJVNh4pYAr7f\n" + "tfvzEvbZBiV6zjSbvE0GeuGhTlBiJ6UXdTmtEiO65Hfd8rSC3/f9AkASEjgUeiow\n" + "MLOa5mcXskE5aEqxgsR52T1wG/p2g9A73k+jfQXLrohMRjMBZrpZu8z40+XDBzj2\n" + "mNOoVk9uSbqc";
    String accountId = "GONGDAOTEST";
    Integer bizId = 2;
    String timestamp = String.valueOf(System.currentTimeMillis());
    String notaryContent = "1234567890";

    @Test
    public void example01() throws Exception {


        //        SFLIdentity sflIdentity = new SFLIdentity();
        //        sflIdentity.setUserType("PERSON");
        //        sflIdentity.setCertName("刘猛");
        //        sflIdentity.setCertType("IDENTITY_CARD");
        //        sflIdentity.setCertNo("412827199405182010");
        //        sflIdentity.setMobileNo("");
        //        sflIdentity.setLegalPerson("");
        //        sflIdentity.setLegalPersonId("");
        //        sflIdentity.setAgent("");
        //        sflIdentity.setAgentId("");
        //        sflIdentity.setProperties("");
        //        String sflIdentityStr = gson.toJson(sflIdentity);  // TODO

        String signedData = CertUtil.sign(accountId + bizId + timestamp, priKey);

        JSONObject obj = new JSONObject();
        obj.put("accountId", accountId);
        obj.put("bizId", bizId);

        JSONObject iden = new JSONObject();
        iden.put("userType", "PERSON");
        iden.put("certName", "刘猛");
        iden.put("certType", "IDENTITY_CARD");
        // iden.put("certNo", "412827199405182010");
        iden.put("certNo", "412827199405180000");
        obj.put("customer", iden);

        obj.put("timestamp", timestamp);
        obj.put("signedData", signedData);
        String jsonString = obj.toString();

        System.out.println(jsonString);
/*
{
    "accountId":"GONGDAOTEST",
    "signedData":"696d8b0f5750d40471093faed673892ac8680a372fe7844a974587b1598d5f2efb879e5b152247dd258ef46011966d992f6561f14c42c5c1225b9cd0ecd2fae1b960fa41a20e4b073a11fbcece2f4b6616cc9a571870791222a1bf84f9321c0d8f025edadd10b5c13ea1defed94e962b2495016ff00ef922160717fdfff7058e",
    "bizId":2,
    "customer":{
        "certNo":"412827199405182010",
        "certType":"IDENTITY_CARD",
        "certName":"刘猛",
        "userType":"PERSON"
    },
    "timestamp":"1555484265303"
}
 */

        HttpUtil.HttpInfo httpInfo = HttpUtil.doPost("https://check.netcourt.gov.cn/api/blockChain/notaryToken", HttpUtil.JSON, obj);

        System.out.println("req h\n" + httpInfo.requestHeaders);
        /*
         */
        System.out.println("req b\n" + httpInfo.requestBody);
/*
{"accountId":"GONGDAOTEST","signedData":"572e6242b50cfc1870deff84afa46441a745faa083783d7c6f1cf2772e3b2b431ae611199248eb44c58164ed2c1404abfdbefd30f55f775592064672a995484ec596d9a7f4321415ae144be4b8abdc379b204b0b518f49f531e15012100f8ade237abfb7ef389e558e2719d4384570809d6528a0e7c73d45fc2df78ad64245db","bizId":2,"customer":{"certNo":"412827199405182010","certType":"IDENTITY_CARD","certName":"刘猛","userType":"PERSON"},"timestamp":"1555485450848"}
 */
        System.out.println("res h\n" + httpInfo.responseHeaders);
/*
Date: Wed, 17 Apr 2019 07:17:50 GMT
Content-Type: application/json
Content-Length: 174
Connection: keep-alive
X-Application-Context: application:staging:8080
 */
        System.out.println(httpInfo.responseBody);
/*
{
    "responseData":null,
    "success":false,
    "errMessage":"Invalid ID card Number",
    "code":"BAD_REQUEST",
    "notarySuc":false,
    "suc":false,
    "indentifySuc":false,
    "downloadSuc":false
}
*/

/*
{
    "responseData":"fe9b2523-67c1-4b6e-b8d1-6f77b0348040",
    "success":true,
    "errMessage":"",
    "codeAddr":"ACCEPTED",
    "notarySuc":true,
    "indentifySuc":false,
    "downloadSuc":false,
    "suc":false
}
 */
    }

    @Test
    public void example02() throws Exception {

        JSONObject notaryMeta = new JSONObject();
        notaryMeta.put("token", "fe9b2523-67c1-4b6e-b8d1-6f77b0348040");
        String phase = "";
        notaryMeta.put("phase", phase);
        notaryMeta.put("accountId", accountId);

        JSONObject obj = new JSONObject();
        obj.put("meta", notaryMeta);
        obj.put("notaryContent", notaryContent);
        obj.put("timestamp", timestamp);

        String signedData = CertUtil.sign(accountId + phase + timestamp, priKey);
        obj.put("signedData", signedData);

        String jsonString = obj.toString();
        System.out.println(jsonString);
/*
{
    "meta":{
        "phase":"",
        "accountId":"GONGDAOTEST",
        "token":"fe9b2523-67c1-4b6e-b8d1-6f77b0348040"
    },
    "signedData":"175ca6d1ae82cedffbd11ecaf24d4ece45dd32dee7ff4733b8f5dadbfd8df621a96edea589724be54b614be3a31cbaf12f9d03896d222ba90f860839c2441be988989e348bb990815a36d038bf5cfe9df65ed0b53eb9130499e5439110dc41db25c84ebcc2ee55cb46b64e31f116cf5beb5fab032ce2de299f3c536ae74a522a",
    "notaryContent":"1234567890",
    "timestamp":"1555484376239"
}
 */

        HttpUtil.HttpInfo httpInfo = HttpUtil.doPost("https://check.netcourt.gov.cn/api/blockChain/notary", HttpUtil.JSON, obj);
        System.out.println(httpInfo.responseBody);
/*
{
    "responseData":"f39ba350ee88171d43118e24bb44abe8bd6f1a9969a60f9f17b13752d010b7bb",
    "success":true,
    "errMessage":"",
    "codeAddr":"ACCEPTED",
    "notarySuc":true,
    "indentifySuc":false,
    "downloadSuc":false,
    "suc":false
}
 */
    }

    @Test
    public void example03() throws Exception {

        JSONObject notaryMeta = new JSONObject();
        notaryMeta.put("token", "fe9b2523-67c1-4b6e-b8d1-6f77b0348040");
        String phase = "";
        notaryMeta.put("phase", phase);
        notaryMeta.put("accountId", accountId);

        JSONObject obj = new JSONObject();
        obj.put("meta", notaryMeta);
        obj.put("notaryContent", notaryContent);
        obj.put("timestamp", timestamp);

        String signedData = CertUtil.sign(accountId + phase + timestamp, priKey);
        obj.put("signedData", signedData);

        String jsonString = obj.toString();
        System.out.println(jsonString);
/*
{
    "meta":{
        "phase":"",
        "accountId":"GONGDAOTEST",
        "token":"fe9b2523-67c1-4b6e-b8d1-6f77b0348040"
    },
    "signedData":"01641ce8b9a97cf3a5f2dc47a73a6eb19644541a1dbea557480f0478f9b04b723e169f91a49b8d2ceac556eb768d00c31d176384f5df309d9ac05fbe5b47b7e385f720a1cd435e30970bf3c3c2d0b3f9df5cd23054ed38f69e8c716cadafff4a090465681910adc2ae1cc492d9f670a383efeaf7d988720911d17ec88b7b2b56",
    "notaryContent":"1234567890",
    "timestamp":"1555484449728"
}
 */

        HttpUtil.HttpInfo httpInfo = HttpUtil.doPost("https://check.netcourt.gov.cn/api/blockChain/notaryCertUrl ", HttpUtil.JSON, obj);

        System.out.println(httpInfo.responseHeaders);
/*
Date: Wed, 17 Apr 2019 07:01:09 GMT
Content-Type: application/json
Content-Length: 202
Connection: keep-alive
X-Application-Context: application:staging:8080
certUrl: http://colima-oss-pro.oss-cn-hangzhou.aliyuncs.com/762418917523479.pdf?Expires=1555484589&OSSAccessKeyId=LTAIy0OdPPzHygy4&Signature=0CmjQs1S6WhS6IY3OvTnvTnOTrk%3D
 */

        System.out.println(httpInfo.responseHeaders.get("certUrl"));
        // http://colima-oss-pro.oss-cn-hangzhou.aliyuncs.com/762418917523479.pdf?Expires=1555484589&OSSAccessKeyId=LTAIy0OdPPzHygy4&Signature=0CmjQs1S6WhS6IY3OvTnvTnOTrk%3D

        System.out.println(httpInfo.responseBody);
/*
{
    "responseData":"1669ebf6a17ccaeeb9356f16d42f5f0b628cb1e8d151cb534dbc26987299f8e3",
    "success":true,
    "errMessage":"",
    "codeAddr":"ACCEPTED",
    "notarySuc":true,
    "indentifySuc":false,
    "downloadSuc":false,
    "suc":false
}
 */
    }

    @Test
    public void example04() throws IOException, JSONException {

        //
        JSONObject obj = new JSONObject();
        obj.put("content", notaryContent);
        obj.put("hash", "1669ebf6a17ccaeeb9356f16d42f5f0b628cb1e8d151cb534dbc26987299f8e3");
        //
        HttpUtil.HttpInfo httpInfo = HttpUtil.doPost("https://check.netcourt.gov.cn/api/blockChain/indentify", HttpUtil.FORM, obj);
        //
        System.out.println("req h\n" + httpInfo.requestHeaders);
        System.out.println("req b\n" + httpInfo.requestBody);
/*
content=1234567890&hash=1669ebf6a17ccaeeb9356f16d42f5f0b628cb1e8d151cb534dbc26987299f8e3
 */
        System.out.println("res h\n" + httpInfo.responseHeaders);
/*
Date: Wed, 17 Apr 2019 07:14:43 GMT
Content-Type: application/json
Content-Length: 76
Connection: keep-alive
X-Application-Context: application:staging:8080
 */
        System.out.println("res b\n" + httpInfo.responseBody);
/*
{
    "codeAddr":0,
    "message":"",
    "data":"核验信息与链上信息不符",
    "total":0
}
*/
/*
{
    "codeAddr":0,
    "message":"",
    "data":"比对结果一致，数据未被篡改，存证时间:2019-04-17 15:01:08，存证阶段:，存证类型:文本存证，所在区块哈希:da440bbf240d7f458ff0e90e6f2dc94faefa4b803f2dc7a961a72665822b299b",
    "total":0
}
 */
    }
}


