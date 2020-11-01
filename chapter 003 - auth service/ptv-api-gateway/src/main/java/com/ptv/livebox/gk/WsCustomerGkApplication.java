package com.ptv.livebox.gk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class WsCustomerGkApplication {

	public static void main(String[] args) {
		SpringApplication.run(WsCustomerGkApplication.class, args);
	}

}
