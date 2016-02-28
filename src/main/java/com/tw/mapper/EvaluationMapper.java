package com.tw.mapper;

import com.tw.domain.Evaluation;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EvaluationMapper {
    int save(@Param("projectId") long projectId, @Param("userId") long userId, @Param("map") Map<String, Object> map);

    Evaluation findById(@Param("projectId") long projectId, @Param("userId") long userId, @Param("evaluationId") long evaluationId);

    int saveResult(@Param("projectId") long projectId, @Param("userId") long userId, @Param("evaluationId") long evaluationId, @Param("map") Map<String, Object> map);

    List<Evaluation> findEvaluationsBelongedToUser(long userId);

    List<Evaluation> findEvaluationsBelongedToProjectUser(@Param("projectId") long projectId, @Param("userId") long userId);
}
