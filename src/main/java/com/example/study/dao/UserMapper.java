package com.example.study.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {
    Optional<UserEntity> findByUserId(String userId);
}
