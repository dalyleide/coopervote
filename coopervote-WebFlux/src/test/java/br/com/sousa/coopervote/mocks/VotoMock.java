package br.com.sousa.coopervote.mocks;

import br.com.sousa.coopervote.domain.Voto;
import br.com.sousa.coopervote.domain.utils.VotoEnum;

public class VotoMock {

    public static final String DEFAULT_CPF = "41244933040";
    public static Voto getVotoSim(String cpf){
        return Voto.fabricaVoto(VotoEnum.Sim, cpf);
    }

    public static Voto getVotoNao(String cpf){
        return Voto.fabricaVoto(VotoEnum.Não, cpf);
    }
}
