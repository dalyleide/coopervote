package br.com.sousa.coopervote.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import static br.com.sousa.coopervote.publisher.utils.DelayUtil.*;

@Component
public class SendMessage {

    private final Logger LOOGER = LoggerFactory.getLogger(SendMessage.class);

    @Value("${close-pauta.rabbitmq.exchange}")
    String exchange;

    @Value("${close-pauta.rabbitmq.routingkey}")
    String routingkey;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public SendMessage(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendPautaCloseMessage(String pautaId, int minutes) {
        sendDelayedMessage(pautaId, convertToMilis(minutes));
    }

    public void sendPautaCloseMessage(String pautaId, LocalDateTime fim) {
        sendDelayedMessage(pautaId, getDurationInMillis(fim));
    }

    private void sendDelayedMessage(String pautaId, Integer delay) {
        rabbitTemplate.convertAndSend(exchange, routingkey, pautaId,
                message -> {
                    message.getMessageProperties().setDelay(delay);
                    return message;
                });
        LOOGER.info("Message send: Pauta Id ".concat(pautaId) );
    }

}
