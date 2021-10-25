package br.com.sousa.coopervote.domain;

import br.com.sousa.coopervote.domain.utils.DomainException;
import br.com.sousa.coopervote.domain.utils.VotoEnum;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public final class Voto extends Entidade {

    private static final long serialVersionUID = 3369466638086946931L;

    private VotoEnum voto;
    private String cpf;

    public static Voto fabricaVoto(VotoEnum voto, String cpf) throws DomainException {
        if (cpf == null || cpf.isBlank())
            throw new DomainException("Cpf não pode estar em branco");

        return new Voto(voto, cpf);
    }

    private Voto(VotoEnum voto, String cpf){
        this.id = UUID.randomUUID().toString();
        this.voto = voto;
        this.cpf = cpf;
    }

    //Se um cpf votar duas vezes na mesma pauta é considerado como um voto igual
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Voto voto = (Voto) o;
        return Objects.equals(cpf, voto.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }
}
