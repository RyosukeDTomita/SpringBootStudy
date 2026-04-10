package com.example.study.dao;

public class UserEntity {
    private String userId;
    private String profile;

    public UserEntity() {
    }

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
