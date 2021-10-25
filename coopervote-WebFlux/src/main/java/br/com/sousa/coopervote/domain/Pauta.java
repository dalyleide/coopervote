package br.com.sousa.coopervote.domain;

import br.com.sousa.coopervote.domain.utils.DomainException;
import br.com.sousa.coopervote.domain.utils.SessaoEnum;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static br.com.sousa.coopervote.domain.utils.DomainValitationsUtil.getTempoEmMinutosValido;

@Getter
public final class Pauta extends Entidade {

    private static final long serialVersionUID = 5515406194160290608L;

    private String descricao;
    private SessaoEnum sessao;
    private Integer duracao;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private Set<Voto> votoSet;

    public static Pauta fabricaPauta(String descricao) throws DomainException {
        if (descricao == null || descricao.isBlank())
            throw new DomainException("Descricao não pode estar em branco");

        return new Pauta(descricao);
    }

    private Pauta(String descricao) {
        this.descricao = descricao;
        this.sessao = SessaoEnum.PENDENTE;
    }

    public Pauta abrirSessao(Integer minutos) throws DomainException {

        if (this.getSessao() != SessaoEnum.PENDENTE)
            throw new DomainException("Pauta aberta ou expirada.");

        minutos = getTempoEmMinutosValido(minutos);

        this.sessao = SessaoEnum.ABERTA;
        this.duracao = minutos;
        this.inicio = LocalDateTime.now();
        this.fim = LocalDateTime.now().plusMinutes(minutos);
        return this;
    }

    public Pauta fecharSessao() {
        if (this.getSessao() == SessaoEnum.PENDENTE)
            throw new DomainException("Pauta pendente.");

        this.sessao = SessaoEnum.FECHADA;
        return this;
    }

    public void addVoto(Voto voto) {

        if(!SessaoEnum.ABERTA.equals(this.getSessao()))
            throw new DomainException("Pauta não está aberta para votação");

        if (this.votoSet == null)
            votoSet = new HashSet<>();
        this.votoSet.add(voto);
    }

    @Override
    public String toString() {
        return "Pauta{" +
                "descricao='" + descricao + '\'' +
                ", sessao=" + sessao +
                ", inicio=" + inicio +
                ", fim=" + fim + '}';
    }
}
