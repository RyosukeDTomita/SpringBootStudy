package com.example.study.controller;

import com.example.study.controller.dto.UserResponse;
import com.example.study.converter.UserConverter;
import com.example.study.domain.User;
import com.example.study.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserConverter userConverter;

    public UserController(UserService userService, UserConverter userConverter) {
        this.userService = userService;
        this.userConverter = userConverter;
    }

    @GetMapping
    public String searchForm() {
        return "user/search";
    }

    @GetMapping("/{userId}")
    public String showProfile(@PathVariable String userId, Model model) {
        Optional<User> user = userService.findByUserId(userId);
        if (user.isEmpty()) {
            model.addAttribute("errorMessage", "ユーザーが見つかりませんでした: " + userId);
            return "user/search";
        }
        UserResponse response = userConverter.toResponse(user.get());
        model.addAttribute("user", response);
        return "user/profile";
    }
}
