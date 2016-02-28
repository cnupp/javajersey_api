package com.tw.mapper;

import com.tw.domain.Qualification;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface QualificationMapper {
    int save(@Param("userId") long userId, @Param("map") Map<String, Object> map);

    List<Qualification> findQualificationsBelongedToUser(@Param("userId") long userId);

    List<Qualification> findQualificationsBelongToUserOfProject(@Param("projectId") long projectId, @Param("userId") long userId);
}
