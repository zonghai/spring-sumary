package com.spring.flowcontrol;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @auth 十三先生
 * @date 2023/12/24
 * @desc
 */
@EnableDiscoveryClient
@EnableAdminServer
@SpringBootApplication
public class FlowControlApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowControlApplication.class);
    }
}
