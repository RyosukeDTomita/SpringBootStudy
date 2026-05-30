package com.example.study.domain;

import java.util.Optional;

// 認証用ユーザの取得ルールだけを定義するインターフェース
public interface AuthUserRepository {
  Optional<AuthUser> findByUserId(String userId);
}
