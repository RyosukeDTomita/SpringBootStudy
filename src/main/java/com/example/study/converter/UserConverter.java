package com.example.study.converter;

import com.example.study.controller.dto.UserResponse;
import com.example.study.dao.UserEntity;
import com.example.study.domain.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserConverter {
    User toUser(UserEntity entity);

    UserResponse toResponse(User user);
}
