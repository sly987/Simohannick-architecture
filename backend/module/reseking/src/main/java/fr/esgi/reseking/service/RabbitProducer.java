package fr.esgi.reseking.service;

import fr.esgi.reseking.configuration.RabbitConfiguration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitProducer {

    private final RabbitTemplate rabbitTemplate;

    public RabbitProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(String message) {
        rabbitTemplate.convertAndSend(
                RabbitConfiguration.EXCHANGE,
                RabbitConfiguration.ROUTING_KEY,
                message
        );
    }

}
