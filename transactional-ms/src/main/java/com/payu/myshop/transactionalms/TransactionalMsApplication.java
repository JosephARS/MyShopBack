package com.payu.myshop.transactionalms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableEurekaClient
@PropertySource("classpath:variables.yml")
public class TransactionalMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionalMsApplication.class, args);
	}

}
