package com.github.ontio.sourcing.util.sfl;

import java.io.BufferedReader;
import java.io.StringReader;
import java.security.*;
import java.security.spec.*;
import java.util.Arrays;
import java.util.Base64;


public class CertUtil {

    /**
     * 加签方法
     * @param: plain，明文
     * @param: priKey，私钥文本
     */
    public static String sign(String plain, String priKey) throws Exception {
        PrivateKey privateKey = getPrivateKey(priKey);
        Signature signature = Signature.getInstance("Sha256WithRSA");
        signature.initSign(privateKey);
        signature.update(plain.getBytes("UTF-8"));
        byte[] signed = signature.sign();
        return Hex.encode(signed);
    }

    /**
     * 验签方法
     * @param: plain，明文
     * @param: signedData，秘文
     * @param: pubKey，公钥文本
     */
    public static boolean rsaCert(String plain, String signedData, String pubkey) throws Exception {
        PublicKey publicKey = getPublicKey(pubkey);
        Signature signature2 = Signature.getInstance("Sha256WithRSA");
        signature2.initVerify(publicKey);
        signature2.update(plain.getBytes("UTF-8"));
        return signature2.verify(Hex.decode(signedData));
    }

    /**
     * 构造公钥数据
     * @param: pubKey，公钥文本
     */
    public static PublicKey getPublicKey(String pubKey) throws Exception {
        //1.获取公钥数据
        byte[] bytesPublicBase64 = readKeyDatas(pubKey);
        //2.对读取回来的数据进行Base64解码
        byte[] bytesPublic = Base64.getDecoder().decode(bytesPublicBase64);
        //3.把解码后的数据,重新封装成一个PublicKey对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytesPublic);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            throw e;
        } catch (InvalidKeySpecException e) {
            throw e;
        }
    }

    /**
     * 构造私钥数据
     * @param: pubKey，私钥文本
     */
    public static PrivateKey getPrivateKey(String priKey) throws Exception {
        //1.读取私钥文件,获取私钥数据
        byte[] bytesPrivateBase64 = readKeyDatas(priKey);
        //2.对读取回来的数据进行Base64解码
        byte[] bytesPrivate = Base64.getDecoder().decode(bytesPrivateBase64);
        //3.把解码后的数据,重新封装成一个PrivateKey对象
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytesPrivate);
        KeyFactory keyFactory = null;
        keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    private static byte[] readKeyDatas(String key) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new StringReader(key));
        String str = null;
        StringBuilder stringBuilder = new StringBuilder();
        while ((str = bufferedReader.readLine()) != null) {
            if (str.contains("---")) {
                continue;
            }
            stringBuilder.append(str);
        }
        return stringBuilder.toString().getBytes();
    }

    private static final class Hex {
        private static final char[] TO_TABLE = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        private static final int[] FROM_TABLE = new int[256];

        private Hex() {
        }

        public static String encode(byte[] src) {
            StringBuffer buff = new StringBuffer(src.length * 2);
            byte[] var2 = src;
            int var3 = src.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                byte b = var2[var4];
                buff.append(TO_TABLE[b >>> 4 & 15]);
                buff.append(TO_TABLE[b & 15]);
            }

            return buff.toString();
        }

        public static byte[] decode(String src) {
            byte[] rtv = new byte[src.length() / 2];
            int i = 0;

            for (int j = 0; i < src.length(); ++j) {
                rtv[j] = (byte) ((FROM_TABLE[src.charAt(i++)] << 4 | FROM_TABLE[src.charAt(i++)]) & 255);
            }

            return rtv;
        }

        static {
            Arrays.fill(FROM_TABLE, -1);

            for (int i = 0; i < TO_TABLE.length; FROM_TABLE[TO_TABLE[i] & 255] = i++) {
                ;
            }

        }
    }
}
