package com.example.study.dao;

import com.example.study.converter.AuthUserConverter;
import com.example.study.domain.AuthUser;
import com.example.study.domain.AuthUserRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class AuthUserRepositoryImpl implements AuthUserRepository {

  private final AuthUserMapper authUserMapper;
  private final AuthUserConverter authUserConverter;

  public AuthUserRepositoryImpl(
      AuthUserMapper authUserMapper, AuthUserConverter authUserConverter) {
    this.authUserMapper = authUserMapper;
    this.authUserConverter = authUserConverter;
  }

  @Override
  public Optional<AuthUser> findByUserId(String userId) {
    return authUserMapper.findByUserId(userId).map(authUserConverter::toDomain);
  }
}
