package br.com.sousa.coopervote.domain.service;

import br.com.sousa.coopervote.domain.Pauta;
import br.com.sousa.coopervote.domain.Voto;
import br.com.sousa.coopervote.domain.utils.DomainException;
import br.com.sousa.coopervote.domain.utils.VotoEnum;

import java.util.List;
import java.util.Map;

public interface PautaService {
    Pauta createPauta(String descricao) throws DomainException;

    void addVoto(String id, VotoEnum voto, String cpf) throws DomainException;

    Pauta abrirPauta(String id, Integer minutos) throws DomainException;

    void finalizarPautas() throws DomainException;

    Map<VotoEnum, List<Voto>> resultado(String id) throws DomainException;

    Map<String, String> getPautas();

    Pauta finalizarPauta(String id) throws DomainException;
}
