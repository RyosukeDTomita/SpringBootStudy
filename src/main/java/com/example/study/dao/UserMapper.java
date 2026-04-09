package com.example.study.dao;

import com.example.study.domain.User;
import com.example.study.domain.UserRepository;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper extends UserRepository {
    @Override
    Optional<User> findByUserId(String userId);
}
