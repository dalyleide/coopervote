package br.com.sousa.coopervote.publisher;

import br.com.sousa.coopervote.domain.Pauta;
import br.com.sousa.coopervote.service.PautaService;
import br.com.sousa.coopervote.domain.utils.SessaoEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ReceiveMessage {

    private final Logger LOOGER = LoggerFactory.getLogger(ReceiveMessage.class);

    private final PautaService pautaService;
    private final SendMessage sendMessage;

    @Autowired
    public ReceiveMessage(PautaService pautaService, SendMessage sendMessage) {
        this.pautaService = pautaService;
        this.sendMessage = sendMessage;
    }

    //TODO: REMOVER PARA OUTRO PROJETO! está bloqueando WebFlux
    @RabbitListener(queues = {"${close-pauta.rabbitmq.queue}"})
    public void receive(@Payload String pautaId) {
        LOOGER.info(String.format("Message received: %s", pautaId));
        Mono.just(pautaId)
                .flatMap(pautaService::finalizarPauta)
                .filter(pauta -> pauta.getSessao().equals(SessaoEnum.ABERTA))
                .subscribe( m -> {
                    LOOGER.info(String.format("Pauta has not expired Id: %s Date end: %s", pautaId, m.getFim()));
                    sendMessage.sendPautaCloseMessage(pautaId, m.getFim());
                });
        LOOGER.info(String.format("End - Message received: %s", pautaId));
    }
}
