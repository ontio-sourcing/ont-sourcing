package com.ontology.sourcing.service;

import com.github.ontio.OntSdk;
import com.github.ontio.common.Helper;
import com.github.ontio.sdk.wallet.Account;
import com.ontology.sourcing.service.utils.PropertiesService;
import com.ontology.sourcing.utils.GlobalVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    //
    private PropertiesService propertiesService;
    private OntSdk ontSdk;

    @Autowired
    public WalletService(PropertiesService propertiesService) {
        //
        this.propertiesService = propertiesService;
        //
        ontSdk = GlobalVariable.getOntSdk(propertiesService.ontologyUrl, propertiesService.walletPath);
    }


    // 链下：创建数字资产账号
    public String createWallet(String pwd) throws Exception {

        Account walletAccount;

        //
        walletAccount = ontSdk.getWalletMgr().createAccount(pwd);

        //
        com.github.ontio.account.Account account = ontSdk.getWalletMgr().getAccount(walletAccount.address, pwd);
        String privateKey = Helper.toHexString(account.serializePrivateKey());

        //
        return walletAccount.address;
    }

    //    public String createIdentityAndRegister(String addr, String pwd) throws Exception {
    //
    //        // 链下：创建数字身份账号
    //        com.github.ontio.account.Account account = ontSdk.getWalletMgr().getAccount(addr, pwd);
    //        String privateKey = Helper.toHexString(account.serializePrivateKey());
    //
    //        Identity identity = ontSdk.getWalletMgr().createIdentityFromPriKey(pwd, privateKey);
    //
    //        // 链上：注册
    //        ontSdk.nativevm().ontId()
    //              .sendRegister(identity, pwd, account, ontSdk.DEFAULT_GAS_LIMIT, GlobalConst.DEFAULT_GAS_PRICE);
    //
    //        //
    //        String ontidStr = identity.ontid;
    //        Thread.sleep(6000);
    //        if (getDDO(ontidStr)) {
    //            return ontidStr;
    //        } else {
    //            throw new BlockChainTimeoutException("BLOCKCHAIN_CONFIRM_TIMEOUT");
    //        }
    //    }
}
