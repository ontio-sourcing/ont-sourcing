package com.ontology.sourcing;

import com.github.ontio.OntSdk;
import com.github.ontio.sdk.wallet.Identity;
import com.ontology.sourcing.util.GlobalVariable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SDKTests {

    //
    private OntSdk ontSdk = GlobalVariable.getOntSdk("http://polaris1.ont.io", "/Volumes/Data/_work/201802_Ontology/ONTSouring/ont-sourcing/config/wallet.json");

    // 付款的数字钱包
    private com.github.ontio.account.Account payerAccount = GlobalVariable.getInstanceOfAccount("6a62d116e416246f974229eee7d1b0894d8c2ab70446856e85e35b7f5d37adef");

    @Test
    public void example01() {
        //
        List<Identity> list = ontSdk.getWalletMgr().getWallet().getIdentities();
        System.out.println(list.size());
        System.out.println(list);
    }

    @Test
    public void example02() {
        //
        ontSdk.getWalletMgr().getWallet().clearIdentity();
    }
}
