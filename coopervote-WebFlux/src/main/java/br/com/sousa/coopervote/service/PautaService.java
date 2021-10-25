package br.com.sousa.coopervote.service;

import br.com.sousa.coopervote.domain.Pauta;
import br.com.sousa.coopervote.domain.Voto;
import br.com.sousa.coopervote.domain.utils.DomainException;
import br.com.sousa.coopervote.domain.utils.VotoEnum;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface PautaService {
    Mono<Pauta> createPauta(String descricao) throws DomainException;

    void addVoto(String id, VotoEnum voto, String cpf) throws DomainException;

    Mono<Pauta> abrirPauta(String id, Integer minutos) throws DomainException;

    void finalizarPautas() throws DomainException;

    Mono<String> resultado(String id) throws DomainException;

    Mono<Map<String, String>> getPautas();

    Mono<Pauta> finalizarPauta(String id) throws DomainException;
}
