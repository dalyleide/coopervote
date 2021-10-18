package br.com.sousa.coopervote.infrastructure.configuration;

import br.com.sousa.coopervote.infrastructure.repository.mongo.SpringDataMongoPautaRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackageClasses = SpringDataMongoPautaRepository.class)
public class MongoDBConfiguration {
}
