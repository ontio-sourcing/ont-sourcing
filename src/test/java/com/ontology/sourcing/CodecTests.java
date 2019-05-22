package com.ontology.sourcing;

import com.ontology.sourcing.util._codec.Base64ConvertUtil;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "file:/Volumes/Data/_work/201802_Ontology/ONTSouring/ont-sourcing/config/application-local.properties")
public class CodecTests {

    // org.apache.commons._codec.binary.Hex
    @Test
    public void example01() {
        String foo = "I am a string";
        byte[] bytes = foo.getBytes();  // string 转 bytes
        System.out.println(Hex.encodeHexString(bytes));
        // 4920616d206120737472696e67
    }

    @Test
    public void example02() {
        String foo = "I am a string";
        byte[] bytes = foo.getBytes();
        System.out.println(bytesToHexString(bytes));
        // 4920616D206120737472696E67  // 说明这个实现也正确
    }


    @Test
    public void example03() throws DecoderException {
        String hexStr = "4920616d206120737472696e67";
        byte[] bytes = Hex.decodeHex(hexStr);
        String foo = new String(bytes);  // bytes 转 string
        System.out.println(foo);
        // I am a string
    }

    @Test
    public void example04() throws DecoderException {
        String hexStr = "4920616d206120737472696e67";
        byte[] bytes = hexStringToByteArray(hexStr);
        String foo = new String(bytes);  // bytes 转 string
        System.out.println(foo);
        // I am a string  // 说明这个实现也正确
    }


    //
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    //
    public static String bytesToHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    @Test
    public void example05() throws UnsupportedEncodingException {
        String s = "";
        System.out.println(s.length());
        String afterBase64 = Base64ConvertUtil.encode(s);
        System.out.println(afterBase64);
        System.out.println(afterBase64);

    }

    @Test
    public void example06() {


    }

}
