package br.com.sousa.coopervote.domain.service;

import br.com.sousa.coopervote.domain.Pauta;
import br.com.sousa.coopervote.domain.Voto;
import br.com.sousa.coopervote.domain.repository.PautaRepository;
import br.com.sousa.coopervote.domain.utils.DomainException;
import br.com.sousa.coopervote.domain.utils.SessaoEnum;
import br.com.sousa.coopervote.domain.utils.VotoEnum;
import br.com.sousa.coopervote.infrastructure.repository.mongo.MongoDbPautaRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static br.com.sousa.coopervote.mocks.PautaMock.DEFAULT_DESCRIPTION;
import static br.com.sousa.coopervote.mocks.PautaMock.getPautaAbertaMock;
import static br.com.sousa.coopervote.mocks.PautaMock.getPautaPendenteMock;
import static br.com.sousa.coopervote.mocks.VotoMock.DEFAULT_CPF;
import static br.com.sousa.coopervote.mocks.VotoMock.getVotoNao;
import static br.com.sousa.coopervote.mocks.VotoMock.getVotoSim;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PautaServiceImplTest {

    @Mock
    PautaRepository repository;

    @InjectMocks
    PautaServiceImpl pautaService;

    @Ignore
//    @Test
    void createPauta() {
        Pauta pauta = getPautaPendenteMock();
        when(repository.save(any())).thenReturn(getPautaPendenteMock());

        Pauta pautaSaved = pautaService.createPauta(DEFAULT_DESCRIPTION);
        assertEquals(pauta.getId(), pautaSaved.getId());
    }

    @Ignore
//    @Test
    void addVoto() {
        Pauta pauta = getPautaPendenteMock();
        when(repository.save(any())).thenReturn(getPautaPendenteMock());

        Pauta pautaSaved = pautaService.createPauta(DEFAULT_DESCRIPTION);
        assertEquals(pauta.getId(), pautaSaved.getId());
    }

    @Ignore
//    @Test
    void abrirPauta() {
        Pauta pauta = getPautaPendenteMock();
        when(repository.findById(pauta.getId())).thenReturn(Optional.of(pauta));

        Pauta pautaSaved = pautaService.abrirPauta(pauta.getId(), 1);
        assertEquals(pauta.getId(), pautaSaved.getId());
        assertEquals(SessaoEnum.ABERTA, pautaSaved.getSessao());
    }

    @Ignore
//    @Test
    void finalizarPautas() {
        Pauta pauta = getPautaPendenteMock();
        when(repository.findById(pauta.getId())).thenReturn(Optional.of(pauta));

        Pauta pautaSaved = pautaService.finalizarPauta(pauta.getId());
        assertEquals(pauta.getId(), pautaSaved.getId());
        assertEquals(SessaoEnum.FECHADA, pautaSaved.getSessao());
    }

    @Ignore
//    @Test
    void resultado() {
        Pauta pauta = getPautaAbertaMock();
        pauta.addVoto(getVotoSim(DEFAULT_CPF));
        pauta.addVoto(getVotoNao(DEFAULT_CPF));
        pauta.addVoto(getVotoSim("51265014027"));
        pauta.addVoto(getVotoNao("57052862018"));
        when(repository.findById(pauta.getId())).thenReturn(Optional.of(pauta));

        Map<VotoEnum, List<Voto>> votos = pautaService.resultado(pauta.getId());
        assertEquals(votos.get(VotoEnum.Não).size(), 1);
        assertEquals(votos.get(VotoEnum.Sim).size(), 2);

        DomainException exception = assertThrows(DomainException.class, () -> getPautaPendenteMock().addVoto(getVotoNao(DEFAULT_CPF)));
        assertTrue(exception.getMessage().contains("Pauta em aberto ou não iniciada."));
    }

}