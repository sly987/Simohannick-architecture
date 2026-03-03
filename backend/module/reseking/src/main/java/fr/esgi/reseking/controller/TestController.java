package fr.esgi.reseking.controller;

import fr.esgi.reseking.model.TestResponse;
import fr.esgi.reseking.repository.TestResponseRepository;
import fr.esgi.reseking.service.RabbitProducer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final TestResponseRepository testResponseRepository;
    private final RabbitProducer rabbitProducer;

    public TestController(TestResponseRepository testResponseRepository, RabbitProducer rabbitProducer) {
        this.testResponseRepository = testResponseRepository;
        this.rabbitProducer = rabbitProducer;
    }

    @PostMapping("/test")
    public void save(@RequestBody TestResponse response) {
        System.out.println("save " + response.getValue());
        rabbitProducer.send(response.getValue());
        testResponseRepository.save(response);
    }
}
