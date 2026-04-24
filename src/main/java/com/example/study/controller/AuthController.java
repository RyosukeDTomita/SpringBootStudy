package com.example.study.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

// 認証済みユーザの情報を返す API（静的ページから利用）
@RestController
public class AuthController {

    record MeResponse(String username) {}

    @GetMapping("/api/me")
    public MeResponse me(Principal principal) {
        return new MeResponse(principal.getName());
    }
}
