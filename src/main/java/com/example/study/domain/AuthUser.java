package com.example.study.domain;

public class AuthUser {
  private final String userId;
  private final String password;
  private final String role;

  public AuthUser(String userId, String password, String role) {
    this.userId = userId;
    this.password = password;
    this.role = role;
  }

  public String getUserId() {
    return userId;
  }

  public String getPassword() {
    return password;
  }

  public String getRole() {
    return role;
  }
}
