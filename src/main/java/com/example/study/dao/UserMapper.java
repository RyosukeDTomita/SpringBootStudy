package com.example.study.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

/**
 * MyBatisが使用するMapperインターフェース。SQLはXMLファイルで定義されている。
 */
@Mapper
public interface UserMapper {
    // xmlのidがfindByUserIdのSQLが呼び出される。
    Optional<UserEntity> findByUserId(String userId);
}
