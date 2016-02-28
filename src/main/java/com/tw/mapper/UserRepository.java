package com.tw.mapper;

import com.tw.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface UserRepository {
    User findUserById(@Param("userId") long userId);

    int createUser(@Param("map") Map<String, Object> map);

    User findUserByName(@Param("name") String name);
}
