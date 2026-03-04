package fr.esgi.reseking_notification.service;

import fr.esgi.reseking_notification.messaging.event.ReservationCreatedEvent;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendTestEmail() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("someone@company.com");
        msg.setFrom("reseking@company.com");
        msg.setSubject("Reseking test email");
        msg.setText("If you see this in MailHog, SMTP works.");

        mailSender.send(msg);
    }
    public void sendReservationCreated(ReservationCreatedEvent event) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(event.getEmployeeEmail());
        msg.setFrom("reseking@company.com");
        msg.setSubject("Parking reservation confirmed");
        msg.setText(
                "Your reservation is confirmed.\n" +
                        "Reservation ID: " + event.getReservationId() + "\n" +
                        "Spot: " + event.getSpotLabel() + "\n" +
                        "From: " + event.getStartDate() + "\n" +
                        "To: " + event.getEndDate()
        );

        mailSender.send(msg);
    }
}