package fr.esgi.reseking.messaging;

import fr.esgi.reseking.configuration.RabbitConfiguration;
import fr.esgi.reseking.messaging.event.ReservationCreatedEvent;
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

    public void sendReservationCreated(ReservationCreatedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitConfiguration.EXCHANGE,
                RabbitConfiguration.ROUTING_KEY,
                event
        );
    }

}
