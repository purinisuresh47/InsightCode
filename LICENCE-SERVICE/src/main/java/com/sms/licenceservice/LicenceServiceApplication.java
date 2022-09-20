package com.sms.licenceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.sms.licenceservice.repository")
@EnableDiscoveryClient
public class LicenceServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LicenceServiceApplication.class, args);
	}

}
