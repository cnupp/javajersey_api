package com.tw.mapper;

import com.tw.domain.Solution;
import com.tw.mapper.util.PrimaryKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SolutionRepository {
    int create(@Param("map") Map<String, Object> map, @Param("primaryKey") PrimaryKey primaryKey);

    List<Solution> getSolutions();

    Solution getSolutionById(long solutionId);
}
