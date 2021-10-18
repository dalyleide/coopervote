package br.com.sousa.coopervote.infrastructure.repository.mongo;

import br.com.sousa.coopervote.domain.Pauta;
import br.com.sousa.coopervote.domain.repository.PautaRepository;
import br.com.sousa.coopervote.domain.utils.SessaoEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MongoDbPautaRepository implements PautaRepository {

    private final SpringDataMongoPautaRepository springRepository;

    @Autowired
    public MongoDbPautaRepository(final SpringDataMongoPautaRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public Optional<Pauta> findById(String id) {
        return springRepository.findById(id);
    }

    @Override
    public List<Pauta> findBySessao(SessaoEnum sessaoEnum) {
        return springRepository.findBySessao(sessaoEnum);
    }

    @Override
    public List<Pauta> findAll() {
        return springRepository.findAll();
    }

    @Override
    public Pauta save(Pauta pauta) { return springRepository.save(pauta); }
}
