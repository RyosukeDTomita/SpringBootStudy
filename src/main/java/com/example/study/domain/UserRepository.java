package com.example.study.domain;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUserId(String userId);
}
