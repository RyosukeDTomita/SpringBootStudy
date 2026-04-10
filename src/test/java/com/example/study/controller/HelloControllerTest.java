package com.example.study.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HelloController.class)
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void defaultGreeting() throws Exception {
        mockMvc.perform(get("/hello"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.message").value("Hello, World!"))
               .andExpect(jsonPath("$.name").value("World"));
    }

    @Test
    void namedGreeting() throws Exception {
        mockMvc.perform(get("/hello").param("name", "Spring"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.message").value("Hello, Spring!"))
               .andExpect(jsonPath("$.name").value("Spring"));
    }
}
