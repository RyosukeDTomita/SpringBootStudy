package com.example.study;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    record Greeting(String message, String name) {}

    @GetMapping("/hello")
    public Greeting hello(
            @RequestParam(defaultValue = "World") String name) {
        return new Greeting("Hello, %s!".formatted(name), name);
    }
}
