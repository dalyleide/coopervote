package br.com.sousa.coopervote.domain.repository;

import br.com.sousa.coopervote.domain.Pauta;
import br.com.sousa.coopervote.domain.utils.SessaoEnum;

import java.util.List;
import java.util.Optional;

public interface PautaRepository {

    Optional<Pauta> findById(String id);
    List<Pauta> findBySessao(SessaoEnum sessaoEnum);
    List<Pauta> findAll();
    Pauta save(Pauta pauta);
}
