package com.example.study.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.study.config.AppConfig;
import com.example.study.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HelloController.class)
@Import({AppConfig.class, SecurityConfig.class})
class HelloControllerTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void defaultGreeting() throws Exception {
    mockMvc
        .perform(get("/hello"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Hello, World!"))
        .andExpect(jsonPath("$.name").value("World"));
  }

  @Test
  void namedGreeting() throws Exception {
    mockMvc
        .perform(get("/hello").param("name", "Spring"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Hello, Spring!"))
        .andExpect(jsonPath("$.name").value("Spring"));
  }
}
