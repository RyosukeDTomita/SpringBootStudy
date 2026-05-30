package com.example.study.converter;

import com.example.study.dao.AuthUserEntity;
import com.example.study.domain.AuthUser;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-27T09:57:05+0900",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class AuthUserConverterImpl implements AuthUserConverter {

    @Override
    public AuthUser toDomain(AuthUserEntity entity) {
        if ( entity == null ) {
            return null;
        }

        String userId = null;
        String password = null;
        String role = null;

        userId = entity.getUserId();
        password = entity.getPassword();
        role = entity.getRole();

        AuthUser authUser = new AuthUser( userId, password, role );

        return authUser;
    }
}
