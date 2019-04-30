package com.ontology.sourcing.util;

import com.github.ontio.account.Account;
import com.github.ontio.common.Helper;
import com.github.ontio.crypto.SignatureScheme;

public class GlobalVariable {

    //
    public static String API_VERSION = "1.0.0";

    //
    public static int CURRENT_ACTION_TABLE_INDEX;
    public static String CURRENT_ACTION_TABLE_NAME;

    //
    public static int CURRENT_CONTRACT_TABLE_INDEX;
    public static String CURRENT_CONTRACT_TABLE_NAME;

    //
    public static int DEFAULT_GAS_PRICE = 500;

    //
    private static Account instanceOfAccount = null;

    public static synchronized Account getInstanceOfAccount(String payerPrivateKey) {
        if (instanceOfAccount == null) {
            try {
                // 方法一，当时设置的passphrase若已被记录到钱包文件中，所以必须对应
                // instanceOfAccount = getOntSdk().getWalletMgr().getAccount(payerAddress, payerPassphrase);
                // 方法二，直接从私钥
                instanceOfAccount = new Account(Helper.hexToBytes(payerPrivateKey), SignatureScheme.SHA256WITHECDSA);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instanceOfAccount;
    }

}
