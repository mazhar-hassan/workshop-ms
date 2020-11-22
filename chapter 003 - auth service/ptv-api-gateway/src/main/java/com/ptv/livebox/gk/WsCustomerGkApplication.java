package com.ptv.livebox.gk;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

@EnableEurekaClient
@SpringBootApplication
public class WsCustomerGkApplication {


	public static void main(String[] args) throws FileNotFoundException {


		SpringApplication.run(WsCustomerGkApplication.class, args);
	}



}
