package com.example.study.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.example.study.domain.AuthUser;
import com.example.study.domain.AuthUserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

// Spring を起動せず Mockito だけで完結する純粋な単体テスト。
// DbUserDetailsService 自体は薄いが、UserDetails の組み立て (ROLE_ プレフィックスや権限) を検証する価値がある。
@ExtendWith(MockitoExtension.class)
class DbUserDetailsServiceTest {

  @Mock private AuthUserRepository authUserRepository;

  private DbUserDetailsService dbUserDetailsService;

  @BeforeEach
  void setUp() {
    dbUserDetailsService = new DbUserDetailsService(authUserRepository);
  }

  @Test
  void loadUserByUsername_存在するユーザはUserDetailsを返す() {
    var authUser = new AuthUser("admin", "{bcrypt}$2a$10$dummy", "ADMIN");
    when(authUserRepository.findByUserId("admin")).thenReturn(Optional.of(authUser));

    UserDetails result = dbUserDetailsService.loadUserByUsername("admin");

    assertThat(result.getUsername()).isEqualTo("admin");
    assertThat(result.getPassword()).isEqualTo("{bcrypt}$2a$10$dummy");
    // Spring Security は roles(...) で渡された値の前に "ROLE_" プレフィックスを付ける
    assertThat(result.getAuthorities()).extracting(Object::toString).containsExactly("ROLE_ADMIN");
  }

  @Test
  void loadUserByUsername_存在しないユーザはUsernameNotFoundException() {
    when(authUserRepository.findByUserId("ghost")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> dbUserDetailsService.loadUserByUsername("ghost"))
        .isInstanceOf(UsernameNotFoundException.class)
        .hasMessageContaining("ghost");
  }
}
