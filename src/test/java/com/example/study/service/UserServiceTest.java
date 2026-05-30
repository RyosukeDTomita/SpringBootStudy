package com.example.study.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.study.domain.User;
import com.example.study.domain.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;

  private UserService userService;

  @BeforeEach
  void setUp() {
    userService = new UserService(userRepository);
  }

  @Test
  void findByUserId_存在するユーザを返す() {
    var user = new User("user01", "テストユーザ");
    when(userRepository.findByUserId("user01")).thenReturn(Optional.of(user));

    Optional<User> result = userService.findByUserId("user01");

    assertTrue(result.isPresent());
    assertEquals("user01", result.get().getUserId());
    assertEquals("テストユーザ", result.get().getProfile());
  }

  @Test
  void findByUserId_存在しない場合は空を返す() {
    when(userRepository.findByUserId("unknown")).thenReturn(Optional.empty());

    Optional<User> result = userService.findByUserId("unknown");

    assertTrue(result.isEmpty());
  }
}
