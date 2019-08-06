package com.zjw.zerer.miscellaneous;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.zjw.zerer.core.common"})
public class MiscellaneousServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiscellaneousServiceApplication.class, args);
    }

}
