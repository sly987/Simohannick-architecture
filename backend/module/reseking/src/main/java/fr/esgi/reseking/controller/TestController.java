package fr.esgi.reseking.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @PostMapping("/test")
    public void test(@RequestBody String testMessage) {
        System.out.println(testMessage);
    }
}
