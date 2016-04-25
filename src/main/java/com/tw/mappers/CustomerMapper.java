package com.tw.mappers;

import com.tw.core.Customer;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface CustomerMapper {
    Customer findById(@Param("id") String id);

    int save(@Param("info") Map<String, Object> body);

    Customer findByName(@Param("name") String userName);
}
