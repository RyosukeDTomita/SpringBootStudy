package com.example.study.service;

import com.example.study.domain.AuthUser;
import com.example.study.domain.AuthUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security の {@link UserDetailsService} 実装。 DB の auth_users テーブルからユーザを引き、 認証用の {@link
 * UserDetails} を組み立てる。
 *
 * <p>パスワードは DB に {noop}xxx 形式で格納されている前提。 デフォルトの DelegatingPasswordEncoder が {noop} プレフィックスを
 * 「ハッシュなし」として認識する。
 */
@Service
public class DbUserDetailsService implements UserDetailsService {

  private final AuthUserRepository authUserRepository;

  public DbUserDetailsService(AuthUserRepository authUserRepository) {
    this.authUserRepository = authUserRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    AuthUser authUser =
        authUserRepository
            .findByUserId(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

    return User.withUsername(authUser.getUserId())
        .password(authUser.getPassword())
        .roles(authUser.getRole())
        .build();
  }
}
