package com.ontology.sourcing;

import com.ontology.sourcing.dao.contract.Contract;
import com.ontology.sourcing.mapper.contract.ContractMapper;
import com.ontology.sourcing.service.ContractService;
import com.ontology.sourcing.service.util.PropertiesService;
import com.ontology.sourcing.util.GlobalVariable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContractTests04 {

    @Autowired private PropertiesService propertiesService;
    @Autowired private ContractMapper contractMapper;
    @Autowired private ContractService contractService;

    @Test
    public void example03() throws Exception {

        //
        List<Contract> list = contractMapper
                .selectByOntidAndHash("tbl_contract_201904101021", "did:ont:AdsCrp9dQy1D6aoFxjDCNm1hnq3Zajq9GE", "e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927102c67");
        Contract contract = list.get(0);
        System.out.println(contractService.contractToDigestForKey(contract));
        // 3259b97cd9ee79f72bd9a563017faaf2f4500293e34ea2ff2d357e1b034040a3

        //
        System.out.println(contractService.contractToDigestForValue(contract));
        // d3c2b7dc4365c13188a31c379ee17c016a6541eb5fe9a0e2c813f172eefcddcc

        // 从链上取
        String valueOnBlockchain = contractService.getContract(contract, GlobalVariable.getInstanceOfAccount(propertiesService.payerPrivateKey));
        System.out.println(valueOnBlockchain);
        // d3c2b7dc4365c13188a31c379ee17c016a6541eb5fe9a0e2c813f172eefcddcc

        // 比对
        boolean rst = contractService.verifyContractOnBlockchain(contract, GlobalVariable.getInstanceOfAccount(propertiesService.payerPrivateKey));
        System.out.println(rst);

    }

}
