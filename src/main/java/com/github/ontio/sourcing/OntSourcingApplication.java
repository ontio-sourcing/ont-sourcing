package com.github.ontio.sourcing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@tk.mybatis.spring.annotation.MapperScan("com.github.ontio.sourcing.mapper")
public class OntSourcingApplication {

    public static void main(String[] args) {
        SpringApplication.run(OntSourcingApplication.class, args);
    }

}
