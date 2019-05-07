package com.ontology.sourcing.service.oauth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.ontio.account.Account;
import com.github.ontio.common.Helper;
import com.ontology.sourcing.service.util.PropertiesService;
import com.ontology.sourcing.util._codec.Base64ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class OAuthService {

    //
    private PropertiesService propertiesService;

    @Autowired
    public OAuthService(PropertiesService propertiesService) {
        this.propertiesService = propertiesService;
    }

    /**
     * 校验token是否正确
     * verify token
     * @param token Token
     */

    public void verify(String token) throws Exception {

        //
        DecodedJWT jwt = JWT.decode(token);
        String content = String.format("%s.%s", jwt.getHeader(), jwt.getPayload());
        String signature = Base64ConvertUtil.decode(jwt.getSignature());

        //
        Account account = new Account(false, Helper.hexToBytes(propertiesService.ontidPublicKey));
        boolean flag = account.verifySignature(content.getBytes(), Helper.hexToBytes(signature));

        //
        if (!flag) {
            throw new Exception("Token signature verify error");
        }

        // TODO：搞成永不过期了
        // if (jwt.getExpiresAt().before(new Date())) {
        //     throw new Exception("Token expiration verify error");
        // }

        //
        //        String s = "did:ont:" + account.getAddressU160().toBase58();
        //        if (!s.equals(ontid)) {
        //            throw new Exception("Token verify error, not same ontid");
        //        }
        // TODO null
    }

    //    public void verifyRefreshToken(String token) throws Exception {
    //        if (!getContentType(token).equals(GlobalVariable.REFRESH_TOKEN)) {
    //            throw new Exception("Token verify error");
    //        }
    //        verify(token);
    //    }

    //    public void verifyAccessToken(String token) throws Exception {
    //        if (!getContentType(token).equals(Constant.ACCESS_TOKEN)) {
    //            throw new Exception("Token verify error");
    //        }
    //        verify(token);
    //    }

    /**
     * 获得Token中的信息无需secret解密也能获得
     * getRecord info from token without decrypt
     * @param token
     * @param claim
     * @return java.lang.String
     */
    public String getClaim(String token, String claim) throws Exception {
        try {
            DecodedJWT jwt = JWT.decode(token);
            // 只能输出String类型，如果是其他类型返回null
            return jwt.getClaim(claim).asString();
        } catch (JWTDecodeException e) {
            e.printStackTrace();
            throw new Exception("解密Token中的公共信息出现JWTDecodeException异常");
        }
    }

    /**
     * 获得Token中的用户
     * getRecord user's ontid from token
     * @param token
     * @return java.lang.String
     */
    public String getContentUser(String token) throws Exception {
        try {
            DecodedJWT jwt = JWT.decode(token);
            // 只能输出String类型，如果是其他类型返回null
            return (String) jwt.getClaim("content").asMap().get("ontid");
        } catch (JWTDecodeException e) {
            e.printStackTrace();
            throw new Exception("解密Token中的公共信息出现JWTDecodeException异常");
        }
    }

    /**
     * 获得Token类型
     * getRecord the type of token ,refresh or access
     * @param token
     * @return java.lang.String
     */
    public static String getContentType(String token) throws Exception {
        try {
            DecodedJWT jwt = JWT.decode(token);
            // 只能输出String类型，如果是其他类型返回null
            return (String) jwt.getClaim("content").asMap().get("type");
        } catch (JWTDecodeException e) {
            e.printStackTrace();
            throw new Exception("解密Token中的公共信息出现JWTDecodeException异常");
        }
    }

    /**
     * 获得Aud
     * @param token
     * @return java.lang.String
     */
    public String getAud(String token) throws Exception {
        try {
            DecodedJWT jwt = JWT.decode(token);
            // 只能输出String类型，如果是其他类型返回null
            return jwt.getClaim("aud").asString();
        } catch (JWTDecodeException e) {
            e.printStackTrace();
            throw new Exception("解密Token中的公共信息出现JWTDecodeException异常");
        }
    }

    /**
     * 生成jwt
     *
     * @return
     */
    //    public String sign(String aud, Object obj) throws Exception {
    ////        ONTID后台的ONTID
    ////        payload.put("iss", secureConfig.getWalletOntid());
    //////        过期时间
    ////        payload.put("exp", new Date().getTime() + Constant.TOKEN_EXPIRE);
    //////        受众
    ////        payload.put("aud", ontidProvide);
    //////        签发时间
    ////        payload.put("iat", new Date().getTime());
    //////        编号
    ////        payload.put("jti", jti);
    //        //设置token到期时间
    //        Date expireDate = new Date();
    //        return MyJwt.create().withIssuer(Constant.ONTID).withExpiresAt(expireDate).withAudience(aud).withIssuedAt(new Date()).
    //                withJWTId(UUID.randomUUID().toString().replace("-", "")).withClaim("content", obj).sign();
    //    }


}
