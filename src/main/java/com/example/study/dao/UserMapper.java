package com.example.study.dao;

import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

/** MyBatisが使用するMapperインターフェース。SQLはXMLファイルで定義されている。 */
@Mapper
public interface UserMapper {
  // xmlのidがfindByUserIdのSQLが呼び出される。
  Optional<UserEntity> findByUserId(String userId);
}
