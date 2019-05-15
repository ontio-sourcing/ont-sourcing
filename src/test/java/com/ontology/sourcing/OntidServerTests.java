package com.ontology.sourcing;

import ch.qos.logback.classic.Logger;
import com.ontology.sourcing.service.ontid_server.OntidServerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OntidServerTests {

    //
    private Logger logger = (Logger) LoggerFactory.getLogger(OntidServerTests.class);

    @Autowired
    OntidServerService ontidServerService;

    @Test
    public void example01() {

        try {
            ontidServerService.registerPhoneWithoutCode("86*18616347300");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

}
