package br.com.sousa.coopervote.domain.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DomainValitationsUtilTest {

    private static final int ONE_MINUTE = 1;

    @Test
    void isPautaExpirada() {
        assertTrue(DomainValitationsUtil.isPautaExpirada(LocalDateTime.now().minusMinutes(ONE_MINUTE)));
        assertFalse(DomainValitationsUtil.isPautaExpirada(LocalDateTime.now().plusMinutes(ONE_MINUTE)));
    }

    @Test
    void verificarPautaPendente() {

    }

    @Test
    void getTempoEmMinutosValido() {
    }

    @Test
    void verificaPautaEmAberto() {
    }
}