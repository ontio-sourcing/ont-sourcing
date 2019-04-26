package com.ontology.sourcing.util;

import com.github.ontio.crypto.Digest;

public class CryptoUtil {

    public static String sha256(String data) {
        byte[] bytes = Digest.sha256(data.getBytes());
        return com.github.ontio.common.Helper.toHexString(bytes);
    }
}
