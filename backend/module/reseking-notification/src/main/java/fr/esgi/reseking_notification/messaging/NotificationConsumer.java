package fr.esgi.reseking_notification.messaging;

import fr.esgi.reseking_notification.messaging.event.ReservationCreatedEvent;
import fr.esgi.reseking_notification.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private final EmailService emailService;

    public NotificationConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "test.queue")
    public void onReservationCreated(ReservationCreatedEvent event) {
        emailService.sendReservationCreated(event);
    }
}