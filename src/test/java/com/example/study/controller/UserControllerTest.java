package com.example.study.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.study.config.AppConfig;
import com.example.study.config.SecurityConfig;
import com.example.study.converter.UserConverterImpl;
import com.example.study.domain.User;
import com.example.study.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

// Thymeleaf テンプレートを返すコントローラもそのままビュー名 / モデルを検証できる。
@WebMvcTest(UserController.class)
@Import({AppConfig.class, SecurityConfig.class, UserConverterImpl.class})
class UserControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private UserService userService;

  @Test
  void search_パラメータなしは検索フォームを表示() throws Exception {
    mockMvc
        .perform(get("/users"))
        .andExpect(status().isOk())
        .andExpect(view().name("user/search"));
  }

  @Test
  void search_userIdが空の場合はバリデーションエラーで検索フォームに戻る() throws Exception {
    // @NotBlank で弾かれることを検証
    mockMvc
        .perform(get("/users").param("userId", ""))
        .andExpect(status().isOk())
        .andExpect(view().name("user/search"))
        .andExpect(model().attributeHasFieldErrors("searchRequest", "userId"));
  }

  @Test
  void search_userIdが指定された場合はprofileへリダイレクト() throws Exception {
    mockMvc
        .perform(get("/users").param("userId", "alice"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/users/alice"));
  }

  @Test
  void showProfile_存在するユーザはプロフィールページを表示() throws Exception {
    when(userService.findByUserId("alice"))
        .thenReturn(Optional.of(new User("alice", "Hello, I am Alice.")));

    mockMvc
        .perform(get("/users/alice"))
        .andExpect(status().isOk())
        .andExpect(view().name("user/profile"))
        .andExpect(model().attributeExists("user"));
  }

  @Test
  void showProfile_存在しないユーザは検索フォームにエラー表示() throws Exception {
    when(userService.findByUserId("ghost")).thenReturn(Optional.empty());

    mockMvc
        .perform(get("/users/ghost"))
        .andExpect(status().isOk())
        .andExpect(view().name("user/search"))
        .andExpect(model().attributeExists("errorMessage"));
  }
}
