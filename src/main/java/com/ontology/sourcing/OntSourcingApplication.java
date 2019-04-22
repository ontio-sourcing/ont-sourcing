package com.ontology.sourcing;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.ontology.sourcing.mapper")
public class OntSourcingApplication {

    public static void main(String[] args) {
        SpringApplication.run(OntSourcingApplication.class, args);
    }

}
