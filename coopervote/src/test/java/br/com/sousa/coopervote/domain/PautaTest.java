package br.com.sousa.coopervote.domain;

import br.com.sousa.coopervote.domain.utils.DomainException;
import br.com.sousa.coopervote.domain.utils.SessaoEnum;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static br.com.sousa.coopervote.mocks.PautaMock.DEFAULT_DESCRIPTION;
import static br.com.sousa.coopervote.mocks.PautaMock.getPautaAbertaMock;
import static br.com.sousa.coopervote.mocks.PautaMock.getPautaPendenteMock;
import static br.com.sousa.coopervote.mocks.VotoMock.DEFAULT_CPF;
import static br.com.sousa.coopervote.mocks.VotoMock.getVotoNao;
import static br.com.sousa.coopervote.mocks.VotoMock.getVotoSim;
import static org.junit.jupiter.api.Assertions.*;

class PautaTest {

    private static final int ONE_MINUTE = 1;

    @Test
    void fabricaPauta() {
         DomainException exception = assertThrows(DomainException.class, () ->Pauta.fabricaPauta(""));
         assertTrue(exception.getMessage().contains("Descricao não pode estar em branco"));

         Pauta pauta = Pauta.fabricaPauta(DEFAULT_DESCRIPTION);
         assertEquals(DEFAULT_DESCRIPTION, pauta.getDescricao());
         assertNull(pauta.getFim());
         assertNull(pauta.getDuracao());
         assertNull(pauta.getInicio());
         assertEquals(SessaoEnum.PENDENTE, pauta.getSessao());
    }

    @Test
    void abrirSessao() {

        Pauta pauta = getPautaPendenteMock().abrirSessao(ONE_MINUTE);
        assertNotNull(pauta.getFim());
        assertNotNull(pauta.getDuracao());
        assertNotNull(pauta.getInicio());
        assertEquals(SessaoEnum.ABERTA, pauta.getSessao());
        assertEquals(Duration.between(pauta.getInicio(), pauta.getFim()).toMinutes(), ONE_MINUTE);

        DomainException exception = assertThrows(DomainException.class, () -> getPautaAbertaMock().abrirSessao(ONE_MINUTE));
        assertTrue(exception.getMessage().contains("Pauta aberta ou expirada."));
    }

    @Test
    void fecharSessao() {

        Pauta pauta = getPautaAbertaMock().fecharSessao();
        assertEquals(SessaoEnum.FECHADA, pauta.getSessao());

        DomainException exception = assertThrows(DomainException.class, () -> getPautaPendenteMock().fecharSessao());
        assertTrue(exception.getMessage().contains("Pauta pendente."));
    }

    @Test
    void addVoto() {
        Pauta pauta = getPautaAbertaMock();
        pauta.addVoto(getVotoSim(DEFAULT_CPF));
        pauta.addVoto(getVotoSim(DEFAULT_CPF));
        pauta.addVoto(getVotoNao(DEFAULT_CPF));
        pauta.addVoto(getVotoNao(DEFAULT_CPF));
        pauta.addVoto(getVotoNao(DEFAULT_CPF));

        assertEquals(pauta.getVotoSet().size(), 1);

        DomainException exception = assertThrows(DomainException.class, () -> getPautaPendenteMock().addVoto(getVotoNao(DEFAULT_CPF)));
        assertTrue(exception.getMessage().contains("Pauta não está aberta para votação"));
    }
}