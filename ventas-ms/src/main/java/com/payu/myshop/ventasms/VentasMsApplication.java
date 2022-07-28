package com.payu.myshop.ventasms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication
@EnableEurekaClient
public class VentasMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(VentasMsApplication.class, args);
	}

}
