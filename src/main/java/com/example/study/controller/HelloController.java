package com.example.study.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.LocalDateTime;

@RestController
public class HelloController {

    private final Clock clock;

    public HelloController(Clock clock) {
        this.clock = clock;
    }

    record Greeting(String message, String name, LocalDateTime timestamp) {}

    @GetMapping("/hello")
    public Greeting hello(
            @RequestParam(defaultValue = "World") String name) {
        return new Greeting("Hello, %s!".formatted(name), name, LocalDateTime.now(clock));
    }
}
