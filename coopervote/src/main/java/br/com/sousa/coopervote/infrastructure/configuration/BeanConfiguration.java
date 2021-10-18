package br.com.sousa.coopervote.infrastructure.configuration;

import br.com.sousa.coopervote.CoopervoteApplication;
import br.com.sousa.coopervote.domain.repository.PautaRepository;
import br.com.sousa.coopervote.domain.service.PautaService;
import br.com.sousa.coopervote.domain.service.PautaServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = CoopervoteApplication.class)
public class BeanConfiguration {

    @Bean
    PautaService pautaService(final PautaRepository pautaRepository) {
        return new PautaServiceImpl(pautaRepository);
    }
}