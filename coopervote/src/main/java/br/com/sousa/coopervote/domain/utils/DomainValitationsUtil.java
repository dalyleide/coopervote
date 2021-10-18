package br.com.sousa.coopervote.domain.utils;

import br.com.sousa.coopervote.domain.Pauta;

import java.time.LocalDateTime;

public final class DomainValitationsUtil {

    private final static Integer TEMPO_DEFAULT = 1;

    private DomainValitationsUtil(){}

    public static boolean isPautaExpirada(LocalDateTime fim) {
        return !fim.isAfter(LocalDateTime.now());
    }

    public static void verificarPautaPendente(SessaoEnum sessao) throws DomainException {
        if (!sessao.equals(SessaoEnum.PENDENTE))
            throw new DomainException("Pauta em votação ou expirada.");
    }

    public static Integer getTempoEmMinutosValido(final Integer minutos) throws DomainException {
        Integer tempo = minutos;
        if (tempo == null)
            tempo = TEMPO_DEFAULT;

        if (tempo <= 0)
            throw new DomainException("Tempo inválido.");

        return tempo;
    }

    public static void verificaPautaEmAberto(Pauta pauta) throws DomainException {
        if (!pauta.getSessao().equals(SessaoEnum.ABERTA))
            throw new DomainException("Pauta não está aberta para votação.");
        if (isPautaExpirada(pauta.getFim()))
            throw new DomainException("Pauta Expirada.");
    }
}
