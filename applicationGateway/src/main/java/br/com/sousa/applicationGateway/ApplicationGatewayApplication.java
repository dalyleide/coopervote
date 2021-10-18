package br.com.sousa.applicationGateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ApplicationGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationGatewayApplication.class, args);
	}

}
