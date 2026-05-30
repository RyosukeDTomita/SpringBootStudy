package com.example.study.converter;

import com.example.study.controller.dto.UserResponse;
import com.example.study.dao.UserEntity;
import com.example.study.domain.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-27T09:57:05+0900",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class UserConverterImpl implements UserConverter {

    @Override
    public User toUser(UserEntity entity) {
        if ( entity == null ) {
            return null;
        }

        String userId = null;
        String profile = null;

        userId = entity.getUserId();
        profile = entity.getProfile();

        User user = new User( userId, profile );

        return user;
    }

    @Override
    public UserResponse toResponse(User user) {
        if ( user == null ) {
            return null;
        }

        String userId = null;
        String profile = null;

        userId = user.getUserId();
        profile = user.getProfile();

        UserResponse userResponse = new UserResponse( userId, profile );

        return userResponse;
    }
}
