package com.ontology.sourcing;

import com.github.ontio.OntSdk;
import com.github.ontio.core.transaction.Transaction;
import com.github.ontio.network.exception.ConnectorException;
import com.ontology.sourcing.utils.GlobalVariable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionTests {

    //
    private OntSdk ontSdk = GlobalVariable.getOntSdk("http://polaris1.ont.io", "/Volumes/Data/_work/201802_Ontology/ONTSouring/ont-sourcing/config/wallet.json");

    // 付款的数字钱包
    private com.github.ontio.account.Account payerAccount = GlobalVariable.getInstanceOfAccount("6a62d116e416246f974229eee7d1b0894d8c2ab70446856e85e35b7f5d37adef");

    @Test
    public void example01() {
        Transaction tx = null;
        try {
            tx = ontSdk.getConnect().getTransaction("ded11ea2558ed7ed075e99f2e58c053708cb71f6beb8124e1956b6e8955dcc0a");
        } catch (ConnectorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(tx.toHexString());
        System.out.println(ontSdk.verifyTransaction(tx));
    }

    @Test
    public void example02() {
        try {
            // int height = ontSdk.getConnect().getBlockHeightByTxHash("ded11ea2558ed7ed075e99f2e58c053708cb71f6beb8124e1956b6e8955dcc0a");
            // System.out.println(height);  // 1329380
            int height = ontSdk.getConnect().getBlockHeightByTxHash("ded11ea2558ed7ed075e99f2e58c053708cb71f6beb8124e1956b6e895000000");
            System.out.println(height);
        } catch (ConnectorException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void example03() {

        try {
            Object o = ontSdk.getConnect().getSmartCodeEvent("a5ad71aafe40eae19f540508829dfcd2b0823289c5c28b319b7e860b3c3a6a07");  //
            System.out.println(o);
/*
{
    "GasConsumed":10000000,
    "event":[
        {
            "States":[
                "Attribute",
                "add",
                "did:ont:AHYEKDyAcCg968yfxLLyQiD9x9UygxKyhW",
                [
                    "6b657931"
                ]
            ],
            "ContractAddress":"0300000000000000000000000000000000000000"
        },
        {
            "States":[
                "transfer",
                "AGbL9NGxBosqRxiD34ZXPHigeti5AZBnfP",
                "AFmseVrdL9f9oyCzZefL9tG6UbviEH9ugK",
                10000000
            ],
            "ContractAddress":"0200000000000000000000000000000000000000"
        }
    ],
    "TxHash":"ded11ea2558ed7ed075e99f2e58c053708cb71f6beb8124e1956b6e8955dcc0a",
    "State":1
}
 */

        } catch (ConnectorException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void example04() {

        try {
            Object o = ontSdk.getConnect().getSmartCodeEvent("bb4d3c87344f7e289c9fbbf7bc114a82037573731dd46fb9db3b49cd948ee6a1");  //
            System.out.println(o);
        } catch (ConnectorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
