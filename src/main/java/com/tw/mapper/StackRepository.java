package com.tw.mapper;

import com.tw.domain.Stack;
import com.tw.mapper.util.PrimaryKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StackRepository {
    int create(@Param("map") Map<String, Object> map, @Param("primaryKey") PrimaryKey primaryKey);

    List<Stack> getStacks();

    Stack getStackById(@Param("stackId") long stackId);
}
