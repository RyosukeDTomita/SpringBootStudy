package com.example.study.domain;

import java.util.Optional;

// UserIdを使って探すというルールだけを定義するインターフェース
public interface UserRepository {
    Optional<User> findByUserId(String userId);
}
