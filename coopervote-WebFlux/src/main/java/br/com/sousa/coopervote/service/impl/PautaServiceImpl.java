package br.com.sousa.coopervote.service.impl;

import br.com.sousa.coopervote.domain.Pauta;
import br.com.sousa.coopervote.domain.Voto;
import br.com.sousa.coopervote.domain.utils.DomainException;
import br.com.sousa.coopervote.domain.utils.SessaoEnum;
import br.com.sousa.coopervote.domain.utils.VotoEnum;
import br.com.sousa.coopervote.repository.PautaRepository;
import br.com.sousa.coopervote.service.PautaService;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import static br.com.sousa.coopervote.domain.utils.DomainValitationsUtil.*;

@Service
public class PautaServiceImpl implements PautaService {

    private final PautaRepository pautaRepository;

    public PautaServiceImpl(final PautaRepository pautaRepository) {
        this.pautaRepository = pautaRepository;
    }

    @Override
    public Mono<Pauta> createPauta(final String descricao) throws DomainException {
        final Pauta pauta = Pauta.fabricaPauta(descricao);
        return pautaRepository.save(pauta);
    }

    @Override
    public void addVoto(final String id, final VotoEnum voto, final String cpf) throws DomainException {
        final Mono<Pauta> pauta = getPauta(id);
        pauta.subscribe(p -> {
            verificaPautaEmAberto(p);

            final Voto votoEntity = Voto.fabricaVoto(voto, cpf);
            p.addVoto(votoEntity);

            pautaRepository.save(p);
        });
    }

    @Override
    public Mono<Pauta> abrirPauta(final String id, final Integer minutos) throws DomainException {

        return Mono.just(id)
                .flatMap(pautaRepository::findById)
                .filter(p -> verificarPautaPendente(p.getSessao()))
                .flatMap(p-> {
                    p.abrirSessao(minutos);
                    return pautaRepository.save(p);
                });
    }

    @Override
    public void finalizarPautas() throws DomainException {

        Flux<Pauta> pautas = pautaRepository.findBySessao(SessaoEnum.ABERTA);
        pautas.filter(p-> isPautaExpirada(p.getFim()))
                .subscribe(this::finalizarPauta);
    }

    @Override
    public Mono<String> resultado(final String id) throws DomainException {
        return getPauta(id)
                .filter(p->verificaPautaNaoFinalizada(p))
                .map(pauta -> {
                    Set<Voto> votos = pauta.getVotoSet();
                    if (votos != null) {
                        Map<VotoEnum, List<Voto>> map = votos.stream().collect(Collectors.groupingBy(Voto::getVoto));
                        return map.keySet().stream().map(
                                key -> key.name().concat(" : " + map.get(key).size())
                        ).collect(Collectors.joining(","));
                    } else
                        return "Não houve votos registrados.";
                });
    }

    @Override
    public Mono<Map<String, String>> getPautas() {
        Flux<Pauta> pautas = pautaRepository.findAll();
        return pautas.collect(Collectors.toMap(Pauta::getId, Pauta::toString));
    }

    @Override
    public Mono<Pauta> finalizarPauta(String id) throws DomainException {
        return getPauta(id)
                .filter(p -> p.getSessao().equals(SessaoEnum.ABERTA) && isPautaExpirada(p.getFim()))
                .flatMap( p-> {
                    p.fecharSessao(); return pautaRepository.save(p);
                });
    }

    private boolean verificaPautaNaoFinalizada(final Pauta pauta) throws DomainException {
        if (!pauta.getSessao().equals(SessaoEnum.FECHADA)) {
            if (pauta.getSessao().equals(SessaoEnum.ABERTA) && isPautaExpirada(pauta.getFim()))
                finalizarPauta(pauta);
            else
                throw new DomainException("Pauta em aberto ou não iniciada.");
        }
        return Boolean.TRUE;
    }

    private Mono<Pauta> finalizarPauta(Pauta pauta) {
        return pautaRepository.save(pauta.fecharSessao());
    }

    private Mono<Pauta> getPauta(String id) {
        return  Mono.just(id)
                .flatMap(pautaRepository::findById)
                .switchIfEmpty(Mono.error(new DomainException(String.format("Não existe pauta para o id %s", id))));
    }

}
