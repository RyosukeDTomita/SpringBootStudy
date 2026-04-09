package com.example.study.controller;

import com.example.study.domain.User;
import com.example.study.domain.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String searchForm() {
        return "user/search";
    }

    @GetMapping("/{userId}")
    public String showProfile(@PathVariable String userId, Model model) {
        Optional<User> user = userRepository.findByUserId(userId);
        if (user.isEmpty()) {
            model.addAttribute("errorMessage", "ユーザーが見つかりませんでした: " + userId);
            return "user/search";
        }
        model.addAttribute("user", user.get());
        return "user/profile";
    }
}
