package com.ontology.sourcing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@tk.mybatis.spring.annotation.MapperScan("com.ontology.sourcing.mapper")
public class OntSourcingApplication {

    public static void main(String[] args) {
        SpringApplication.run(OntSourcingApplication.class, args);
    }

}
