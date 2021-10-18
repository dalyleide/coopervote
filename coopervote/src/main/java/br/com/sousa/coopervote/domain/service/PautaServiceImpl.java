package br.com.sousa.coopervote.domain.service;

import br.com.sousa.coopervote.domain.Pauta;
import br.com.sousa.coopervote.domain.Voto;
import br.com.sousa.coopervote.domain.repository.PautaRepository;
import br.com.sousa.coopervote.domain.utils.DomainException;
import br.com.sousa.coopervote.domain.utils.SessaoEnum;
import br.com.sousa.coopervote.domain.utils.VotoEnum;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import static br.com.sousa.coopervote.domain.utils.DomainValitationsUtil.*;

public class PautaServiceImpl implements PautaService {

    private final PautaRepository pautaRepository;

    public PautaServiceImpl(final PautaRepository pautaRepository) {
        this.pautaRepository = pautaRepository;
    }

    @Override
    public Pauta createPauta(final String descricao) throws DomainException {
        final Pauta pauta = Pauta.fabricaPauta(descricao);
        return pautaRepository.save(pauta);
    }

    @Override
    public void addVoto(final String id, final VotoEnum voto, final String cpf) throws DomainException {
        final Pauta pauta = getPauta(id);
        verificaPautaEmAberto(pauta);

        final Voto votoEntity = Voto.fabricaVoto(voto, cpf);
        pauta.addVoto(votoEntity);

        pautaRepository.save(pauta);
    }

    @Override
    public Pauta abrirPauta(final String id, final Integer minutos) throws DomainException {

        final Pauta pauta = getPauta(id);
        verificarPautaPendente(pauta.getSessao());

        return pautaRepository.save(pauta.abrirSessao(getTempoEmMinutosValido(minutos)));
    }

    @Override
    public void finalizarPautas() throws DomainException {

        List<Pauta> pautas = pautaRepository.findBySessao(SessaoEnum.ABERTA);
        pautas.stream().filter(p-> isPautaExpirada(p.getFim()))
                .forEach(this::finalizarPauta);
    }

    @Override
    public Map<VotoEnum, List<Voto>> resultado(final String id) throws DomainException {
        final Pauta pauta = getPauta(id);
        verificaPautaNaoFinalizada(pauta);
        //Set não permite objetos duplicados
        return Optional.ofNullable(pauta.getVotoSet()).orElse(new HashSet<>()).stream().collect(Collectors.groupingBy(Voto::getVoto));
    }

    @Override
    public Map<String, String> getPautas() {
        List<Pauta> pautas = pautaRepository.findAll();
        return pautas.stream().collect(Collectors.toMap(Pauta::getId, Pauta::toString));
    }

    @Override
    public Pauta finalizarPauta(String id) throws DomainException {
        Pauta pauta = getPauta(id);
        if (pauta.getSessao().equals(SessaoEnum.ABERTA) && isPautaExpirada(pauta.getFim()))
            pauta = finalizarPauta(pauta);
        return pauta;
    }

    private void verificaPautaNaoFinalizada(final Pauta pauta) throws DomainException {
        if (!pauta.getSessao().equals(SessaoEnum.FECHADA)) {
            if (pauta.getSessao().equals(SessaoEnum.ABERTA) && isPautaExpirada(pauta.getFim()))
                finalizarPauta(pauta);
            else
                throw new DomainException("Pauta em aberto ou não iniciada.");
        }
    }

    private Pauta finalizarPauta(Pauta pauta) {
        return pautaRepository.save(pauta.fecharSessao());
    }

    private Pauta getPauta(String id) {
        return pautaRepository
                .findById(id)
                .orElseThrow(() -> new DomainException("Não exite Pauta para o Id fornecido"));
    }

}
