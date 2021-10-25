package br.com.sousa.coopervote.mocks;

import br.com.sousa.coopervote.domain.Pauta;

public class PautaMock {

    public static final String DEFAULT_DESCRIPTION = "PAUTA_DESCRIPTION";

    public static Pauta getPautaPendenteMock(){
        Pauta pauta = Pauta.fabricaPauta(DEFAULT_DESCRIPTION);
        return pauta;
    }

    public static Pauta getPautaAbertaMock(){
        return getPautaPendenteMock().abrirSessao(0);
    }

    public static Pauta getPautaFechadaMock(){
        return getPautaPendenteMock().fecharSessao();
    }
}
