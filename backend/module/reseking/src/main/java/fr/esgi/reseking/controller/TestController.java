package fr.esgi.reseking.controller;

import fr.esgi.reseking.model.TestResponse;
import fr.esgi.reseking.repository.TestResponseRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final TestResponseRepository testResponseRepository;

    public TestController(TestResponseRepository testResponseRepository) {
        this.testResponseRepository = testResponseRepository;
    }

    @PostMapping("/test")
    public void save(@RequestBody TestResponse response) {
        System.out.println("save " + response.getValue());
        testResponseRepository.save(response);
    }
}
