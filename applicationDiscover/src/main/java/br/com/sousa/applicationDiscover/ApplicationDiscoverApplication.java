package br.com.sousa.applicationDiscover;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ApplicationDiscoverApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationDiscoverApplication.class, args);
	}

}
