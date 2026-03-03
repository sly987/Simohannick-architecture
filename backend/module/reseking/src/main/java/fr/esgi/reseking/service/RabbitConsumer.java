package fr.esgi.reseking.service;

import fr.esgi.reseking.configuration.RabbitConfiguration;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Component
public class RabbitConsumer {


    private static final Path FILE =
            Paths.get("/app/data/messages.txt");

    @RabbitListener(queues = RabbitConfiguration.QUEUE)
    public void receive(String message) throws IOException {
        Files.writeString(
                FILE,
                message + System.lineSeparator(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }
}
