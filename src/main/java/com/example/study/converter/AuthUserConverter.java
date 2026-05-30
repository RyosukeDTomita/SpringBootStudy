package com.example.study.converter;

import com.example.study.dao.AuthUserEntity;
import com.example.study.domain.AuthUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthUserConverter {
  AuthUser toDomain(AuthUserEntity entity);
}
