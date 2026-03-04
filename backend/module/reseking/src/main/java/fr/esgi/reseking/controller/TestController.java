package fr.esgi.reseking.controller;

import fr.esgi.reseking.model.TestResponse;
import fr.esgi.reseking.repository.TestResponseRepository;
import fr.esgi.reseking.messaging.RabbitProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Test", description = "Test endpoints for development and debugging")
public class TestController {

    private final TestResponseRepository testResponseRepository;
    private final RabbitProducer rabbitProducer;

    public TestController(TestResponseRepository testResponseRepository, RabbitProducer rabbitProducer) {
        this.testResponseRepository = testResponseRepository;
        this.rabbitProducer = rabbitProducer;
    }

    @PostMapping("/test")
    @Operation(summary = "Test endpoint", description = "Test endpoint for saving test responses and sending to RabbitMQ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Test response saved and sent successfully")
    })
    public void save(@RequestBody TestResponse response) {
        System.out.println("save " + response.getValue());
        rabbitProducer.send(response.getValue());
        testResponseRepository.save(response);
    }

    @PostMapping("/test/rabbit")
    @Operation(summary = "Test RabbitMQ", description = "Test endpoint for sending a message to RabbitMQ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message sent successfully")
    })
    public void test() {
        rabbitProducer.send("hello world");
    }
}
