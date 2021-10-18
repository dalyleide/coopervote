package br.com.sousa.coopervote.publisher;

import br.com.sousa.coopervote.domain.Pauta;
import br.com.sousa.coopervote.domain.service.PautaService;
import br.com.sousa.coopervote.domain.utils.SessaoEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

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

    @RabbitListener(queues = {"${close-pauta.rabbitmq.queue}"})
    public void receive(@Payload String pautaId) {
        LOOGER.info("Message received: ".concat(pautaId));
        Pauta pauta = pautaService.finalizarPauta(pautaId);
        if (pauta.getSessao().equals(SessaoEnum.ABERTA)) {
            LOOGER.info(String.format("Pauta has not expired Id: %s Date end: %s", pautaId, pauta.getFim().toString()));
            sendMessage.sendPautaCloseMessage(pautaId, pauta.getFim());
        } else {
            LOOGER.info("Pauta closed Id:".concat(pautaId));
        }
    }
}
