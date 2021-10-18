package br.com.sousa.coopervote;

import br.com.sousa.coopervote.infrastructure.repository.mongo.SpringDataMongoPautaRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = SpringDataMongoPautaRepository.class)
@EnableDiscoveryClient
public class CoopervoteApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoopervoteApplication.class, args);
	}

}
