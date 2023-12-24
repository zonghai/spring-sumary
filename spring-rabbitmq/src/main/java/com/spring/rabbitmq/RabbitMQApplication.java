package com.spring.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class RabbitMQApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitMQApplication.class, args);
	}
}
