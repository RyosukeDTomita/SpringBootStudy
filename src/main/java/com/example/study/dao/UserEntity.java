package com.example.study.dao;

public class UserEntity {
  private String userId;
  private String profile;

  // NOTE: JPAが引数なしコンストラクタを必要としており、recordは引数なしコンストラクタを持たないため、手書きで作成されている。
  public UserEntity() {}

  public UserEntity(String userId, String profile) {
    this.userId = userId;
    this.profile = profile;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getProfile() {
    return profile;
  }

  public void setProfile(String profile) {
    this.profile = profile;
  }
}
