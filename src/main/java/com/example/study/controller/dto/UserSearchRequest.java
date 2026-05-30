package com.example.study.controller.dto;

import jakarta.validation.constraints.NotBlank;

// Bean Validationを使ってユーザー検索時の入力値を検証するDTO
// /users?userId=hoge の形式でクエリパラメータを受け取る
public record UserSearchRequest(@NotBlank(message = "ユーザーIDを入力してください") String userId) {}
