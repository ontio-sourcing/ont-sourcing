package com.ontology.sourcing.util;

import com.github.ontio.OntSdk;
import com.github.ontio.account.Account;
import com.github.ontio.common.Helper;
import com.github.ontio.crypto.SignatureScheme;
import com.github.ontio.sdk.exception.SDKException;

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
    public static OntSdk getOntSdk(String ontologyUrl,String walletPath) {

        //        String ip = "http://127.0.0.1";
        //        String ip = "http://polaris1.ont.io";
        //        String ip = "http://139.219.129.55";
        //        String ip = "http://101.132.193.149";
        String restUrl = ontologyUrl + ":" + "20334";
        String rpcUrl = ontologyUrl + ":" + "20336";
        String wsUrl = ontologyUrl + ":" + "20335";

        OntSdk wm = OntSdk.getInstance();
        wm.setRpc(rpcUrl);
        wm.setRestful(restUrl);
        try {
            wm.setDefaultConnect(wm.getRestful());
        } catch (SDKException e) {
            e.printStackTrace();
        }

        wm.openWalletFile(walletPath);  // TODO

        return wm;
    }


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
