package com.example.study.service;

import com.example.study.domain.User;
import com.example.study.domain.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }
}
