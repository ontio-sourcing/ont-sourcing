package com.github.ontio.sourcing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@tk.mybatis.spring.annotation.MapperScan("com.github.ontio.sourcing.mapper")
// eureka
@EnableDiscoveryClient
// 熔断及监控
@EnableFeignClients
@EnableHystrixDashboard
@EnableCircuitBreaker
public class OntSourcingApplication {

    public static void main(String[] args) {
        SpringApplication.run(OntSourcingApplication.class, args);
    }

}
