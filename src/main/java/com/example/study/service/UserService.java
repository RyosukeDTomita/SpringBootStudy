package com.example.study.service;

import com.example.study.domain.User;
import com.example.study.domain.UserRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  // @Autowired // コンストラクタ1つの場合は省略可能
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Optional<User> findByUserId(String userId) {
    return userRepository.findByUserId(userId);
  }
}
