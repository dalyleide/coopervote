package br.com.sousa.coopervote.publisher.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClosePautaMessageConfig {

    @Value("${close-pauta.rabbitmq.exchange}")
    String exchange;

    @Value("${close-pauta.rabbitmq.queue}")
    String queue;

    @Value("${close-pauta.rabbitmq.routingkey}")
    String routingkey;

    @Bean
    public Exchange declareExchange() {
        return ExchangeBuilder.directExchange(exchange).durable(true).delayed().build();
    }

    @Bean Queue declareQueue(){
        return QueueBuilder.durable(queue).build();
    }

    @Bean Binding declareBinding(Queue queue, Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingkey).noargs();
    }
}
