package com.example.study.domain;

public class User {
    private final String userId;
    private final String profile;

    public User(String userId, String profile) {
        this.userId = userId;
        this.profile = profile;
    }

    public String getUserId() {
        return userId;
    }

    public String getProfile() {
        return profile;
    }
}
