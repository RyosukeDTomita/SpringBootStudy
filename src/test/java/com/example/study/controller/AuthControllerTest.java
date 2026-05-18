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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

// /api/me は SecurityConfig で .authenticated()。Principal.getName() を返すだけのシンプルな API。
@WebMvcTest(AuthController.class)
@Import({AppConfig.class, SecurityConfig.class})
class AuthControllerTest {

  @Autowired private MockMvc mockMvc;

  @Test
  @WithMockUser(username = "alice")
  void me_認証済みユーザのusernameを返す() throws Exception {
    mockMvc
        .perform(get("/api/me"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("alice"));
  }

  @Test
  void me_未認証はログイン画面へリダイレクト() throws Exception {
    mockMvc.perform(get("/api/me")).andExpect(status().is3xxRedirection());
  }
}
