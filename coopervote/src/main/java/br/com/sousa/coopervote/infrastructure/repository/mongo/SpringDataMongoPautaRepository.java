package br.com.sousa.coopervote.infrastructure.repository.mongo;

import br.com.sousa.coopervote.domain.Pauta;
import br.com.sousa.coopervote.domain.utils.SessaoEnum;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataMongoPautaRepository extends MongoRepository<Pauta, String> {

    List<Pauta> findBySessao(SessaoEnum sessaoEnum);
}
