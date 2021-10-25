package br.com.sousa.coopervote.repository;

import br.com.sousa.coopervote.domain.Pauta;
import br.com.sousa.coopervote.domain.utils.SessaoEnum;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface PautaRepository extends ReactiveMongoRepository<Pauta, String> {

    Flux<Pauta> findBySessao(SessaoEnum sessaoEnum);
}
