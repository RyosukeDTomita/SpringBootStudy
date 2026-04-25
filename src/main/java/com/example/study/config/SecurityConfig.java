package com.example.study.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            auth ->
                auth
                    // 商品登録は管理者のみ（静的ページ item.html も含む）
                    .requestMatchers("/register/**", "/item.html")
                    .hasRole("ADMIN")
                    // 購入画面はログイン済みユーザ
                    .requestMatchers("/shop/**")
                    .authenticated()
                    // 認証ユーザ情報 API
                    .requestMatchers("/api/me")
                    .authenticated()
                    // それ以外は誰でもアクセス可能
                    .anyRequest()
                    .permitAll())
        // Spring Security デフォルトのログインページを使用
        // ログイン成功後はトップページへ遷移（元々アクセスしようとしたURLがあればそちらを優先）
        .formLogin(form -> form.defaultSuccessUrl("/", false).permitAll())
        .logout(logout -> logout.permitAll())
        // CSRF トークンを Cookie に書き出し、JavaScript から読めるようにする
        // デフォルトの XorCsrfTokenRequestAttributeHandler は XOR エンコードされたトークンを期待するが、
        // Cookie の生トークンをそのまま JS から送るため、プレーンなハンドラーを使用する
        .csrf(
            csrf ->
                csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
        // Spring Security 6 では CsrfToken が遅延ロードされるため、
        // フィルターで明示的にトークンを読み込み Cookie への書き出しを強制する
        .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class);

    return http.build();
  }

  // Spring Security 6 では CsrfToken が遅延ロードされ、Cookie が書き出されないことがある。
  // このフィルターで getToken() を呼び出し、Cookie への書き出しを強制する。
  static class CsrfCookieFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
      CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
      if (csrfToken != null) {
        csrfToken.getToken();
      }
      filterChain.doFilter(request, response);
    }
  }

  @Bean
  public UserDetailsService userDetailsService() {
    var admin =
        User.withDefaultPasswordEncoder()
            .username("admin")
            .password("admin")
            .roles("ADMIN", "USER")
            .build();

    var user =
        User.withDefaultPasswordEncoder().username("user").password("user").roles("USER").build();

    return new InMemoryUserDetailsManager(admin, user);
  }
}
