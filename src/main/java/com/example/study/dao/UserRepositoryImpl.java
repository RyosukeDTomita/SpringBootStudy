package com.example.study.dao;

import com.example.study.converter.UserConverter;
import com.example.study.domain.User;
import com.example.study.domain.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper userMapper;
    private final UserConverter userConverter;

    public UserRepositoryImpl(UserMapper userMapper, UserConverter userConverter) {
        this.userMapper = userMapper;
        this.userConverter = userConverter;
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return userMapper.findByUserId(userId)
                .map(userConverter::toUser);
    }
}
