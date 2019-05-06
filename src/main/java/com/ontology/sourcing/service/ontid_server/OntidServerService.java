package com.ontology.sourcing.service.ontid_server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ontology.sourcing.util._codec.Base64ConvertUtil;
import com.ontology.sourcing.util._crypt.*;
import com.ontology.sourcing.util._hash.HMACSha256;
import com.ontology.sourcing.util._hash.MD5Utils;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.UUID;

@Service
public class OntidServerService {

    //
    private final AESUtil utilAES;

    private final RSAUtil utilRSA;

    //
    private final String host;
    private final String pathRegister;
    //
    private final String appId;
    private final String appSecret;

    @Autowired
    public OntidServerService(@Value("${com.ontology.sourcing.ontid.app.id}") String appId,
                              @Value("${com.ontology.sourcing.ontid.app.secret}") String appSecret,
                              @Value("${com.ontology.sourcing.ontid.app.host}") String host,
                              @Value("${com.ontology.sourcing.ontid.app.path.register}") String pathRegister,
                              AESUtil utilAES,
                              RSAUtil utilRSA) {
        //
        this.appId = appId;
        this.appSecret = appSecret;
        //
        this.host = host;
        this.pathRegister = pathRegister;
        //
        this.utilAES = utilAES;
        this.utilRSA = utilRSA;
    }

    public String registerPhoneWithoutCode(String phone, String password) throws Exception {

        //
        final String URI = host + pathRegister;

        //
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", phone);
        jsonObject.put("password", password);
        String json = JSON.toJSONString(jsonObject);

        //
        String ontid = postHmac(URI, jsonObject);
        return ontid;
    }

    public String postHmac(String URI, JSONObject json) throws Exception {

        //加密
        String key = utilAES.generateKey();
        String enKey = null;
        try {
            //			json = ow.writeValueAsString(jsonObject);
            enKey = utilRSA.encryptByPublicKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
        String enJson = utilAES.encryptData(key, JSON.toJSONString(json));
        RequestBean requestBean = new RequestBean(enJson);

        //hmac
        byte[] md5 = MD5Utils.MD5Encode(JSON.toJSONString(requestBean));
        String requestContentBase64String = Base64ConvertUtil.encode(md5);

        Long tsLong = System.currentTimeMillis() / 1000;
        String requestTimeStamp = tsLong.toString();

        UUID uuid = UUID.randomUUID();
        String nonce = Base64ConvertUtil.encode(uuid.toString().getBytes());

        //
        String rawData = appId + "POST" + "/api/v1/ontid/login/phone" + requestTimeStamp + nonce + requestContentBase64String;  // TODO
        String signature = Base64ConvertUtil.encode(HMACSha256.sha256_HMAC(rawData, appSecret));
        String authHMAC = String.format("ont:%s:%s:%s:%s", appId, signature, nonce, requestTimeStamp);

        //
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHMAC);
        headers.set("Secure-Key", enKey);
        headers.setAccept(Arrays.asList(new MediaType("application", "ontid.manage.api.v1+json")));
        headers.setContentType(new MediaType("application", "ontid.manage.api.v1+json"));

        //
        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        final HttpEntity<RequestBean> entity = new HttpEntity<RequestBean>(requestBean, headers);

        //
        ResponseEntity<Result> response = restTemplate.postForEntity(URI, entity, Result.class);

        //
        String ontid = (String) response.getBody().Result;

        //
        // System.out.println("http code:" + response.getStatusCode().value());
        // System.out.println(ontid);

        //
        return ontid;
    }

}
